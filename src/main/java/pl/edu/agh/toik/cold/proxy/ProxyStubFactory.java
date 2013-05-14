package pl.edu.agh.toik.cold.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.UUID;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import akka.actor.ActorRef;

public class ProxyStubFactory {

	public static Object createProxyStub(Class<?> klass, final String beanId,
			final ProxyActorSystem proxyActorSystem, final String remoteHost,
			final int remotePort) {

		if (klass == null || beanId == null || proxyActorSystem == null
				|| remoteHost == null) {
			throw new InvalidParameterException("No paramaters can be null.");
		}

		ProxyFactory factory = new ProxyFactory();
		factory.setSuperclass(klass);

		factory.setFilter(new MethodFilter() {

			@Override
			public boolean isHandled(Method method) {
				return method.getReturnType() == void.class;
			}
		});

		MethodHandler handler = new MethodHandler() {
			@Override
			public Object invoke(Object self, Method thisMethod,
					Method proceed, Object[] params) throws Throwable {

				// System.out.println(String.format(
				// "Handling %s for remote bean %s", thisMethod, beanId));

				Class<?> returnType = thisMethod.getReturnType();

				if (returnType != void.class) {
					throw new UnsupportedOperationException(
							"Cannot remotely invoke method with non-void return type.");
				}

				for (int i = 0; i < params.length; i++) {

					Object param = params[i];			
					
					// given object is a stub - pass MetaProxyStub
					if (proxyActorSystem.isProxyStub(param)) {
						MetaProxyStub metaProxyStub = proxyActorSystem
								.getMetaProxyStub(param);
						params[i] = metaProxyStub;
						continue;
					}

					// given object is a skeleton - create and pass MetaProxyStub to connect
					// to that skeleton
					if (proxyActorSystem.hasProxySkeleton(param)) {
						ProxySkeleton proxySkeleton = proxyActorSystem
								.getProxySkeleton(param);

						MetaProxyStub metaProxyStub = new MetaProxyStub(
								param.getClass(), proxySkeleton.getBeanId(),
								proxyActorSystem.getHostname(),
								proxyActorSystem.getPort());

						params[i] = metaProxyStub;
						continue;
					}
					
					// given object is serializable - just leave it
					if (param instanceof Serializable) {
						continue;
					}	

					// otherwise create skeleton to stay and MetaProxyStub to pass
					String randomBeanId = UUID.randomUUID().toString();
					new ProxySkeleton(proxyActorSystem, param, randomBeanId);
					MetaProxyStub metaProxyStub = new MetaProxyStub(
							param.getClass(), randomBeanId,
							proxyActorSystem.getHostname(),
							proxyActorSystem.getPort());
					params[i] = metaProxyStub;

				}

				MethodInvocation methodInvocation = new MethodInvocation(
						thisMethod.getDeclaringClass(), thisMethod.getName(),
						beanId, thisMethod.getParameterTypes(), params);

				String remotePath = String.format(
						"akka://ColdSystem@%s:%d/user/%s", remoteHost,
						remotePort, beanId);

				ActorRef remoteActor = proxyActorSystem.getActorSystem()
						.actorFor(remotePath);
				remoteActor.tell(methodInvocation, null);

				return null;
			}
		};

		// TODO: [mjk] exceptions
		Object proxyStub = null;
		try {
			proxyStub = factory.create(new Class<?>[0], new Object[0], handler);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		MetaProxyStub metaProxyStub = new MetaProxyStub(klass, beanId,
				remoteHost, remotePort);
		proxyActorSystem.addMetaProxyStub(proxyStub, metaProxyStub);

		return proxyStub;

	}

	public static Object createProxyStub(MetaProxyStub metaProxyStub,
			ProxyActorSystem proxyActorSystem) {
		return createProxyStub(metaProxyStub.getKlass(),
				metaProxyStub.getBeanId(), proxyActorSystem,
				metaProxyStub.getRemoteHost(), metaProxyStub.getRemotePort());
	}
}

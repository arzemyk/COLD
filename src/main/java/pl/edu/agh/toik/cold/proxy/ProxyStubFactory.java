package pl.edu.agh.toik.cold.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

import akka.actor.ActorRef;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public class ProxyStubFactory {

	public static Object createProxyStub(Class<?> klass, final String beanId,
			final ProxyActorSystem proxyActorSystem, final String remotePath) {

		if (klass == null || beanId == null) {
			throw new InvalidParameterException(
					"Neither klass nor beanId can be null");
		}

		if (proxyActorSystem == null) {
			throw new InvalidParameterException(
					"proxyActorSystem cannot be null.");
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
					Method proceed, Object[] args) throws Throwable {

				System.out.println(String.format(
						"Handling %s for remote bean %s", thisMethod, beanId));

				Class<?> returnType = thisMethod.getReturnType();

				if (returnType != void.class) {
					throw new UnsupportedOperationException(
							"Cannot remotely invoke method with non-void return type.");
				}

				MethodInvocation methodInvocation = new MethodInvocation(
						thisMethod.getClass(), beanId,
						thisMethod.getParameterTypes(), args);

				ActorRef remoteActor = proxyActorSystem.getActorSystem().actorFor(remotePath); 
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

		return proxyStub;

	}
}

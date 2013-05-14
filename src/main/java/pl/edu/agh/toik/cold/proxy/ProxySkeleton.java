package pl.edu.agh.toik.cold.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;

public class ProxySkeleton {

	private Object targetObject;

	@SuppressWarnings("unused")
	private ProxyActorSystem proxyActorSystem;

	@SuppressWarnings("unused")
	private ActorRef proxySkeletonActor;

	@SuppressWarnings("unused")
	private String beanId;

	@SuppressWarnings("serial")
	public ProxySkeleton(Object targetObject,
			ProxyActorSystem proxyActorSystem, String beanId) {

		if (targetObject == null || proxyActorSystem == null || beanId == null) {
			throw new InvalidParameterException("No paramaters can be null.");
		}

		this.targetObject = targetObject;
		this.proxyActorSystem = proxyActorSystem;
		this.beanId = beanId;

		proxySkeletonActor = proxyActorSystem.getActorSystem().actorOf(
				new Props(new UntypedActorFactory() {

					@Override
					public Actor create() throws Exception {
						return new ProxySkeletonActor(ProxySkeleton.this);
					}
				}), beanId);

		proxyActorSystem.addSkeleton(targetObject, this);
	}

	public void handleMethodInvocation(MethodInvocation methodInvocation) {

		Class<?> targetClass = methodInvocation.getTargetClass();
		if (targetClass != targetObject.getClass()) {
			throw new RuntimeException(
					"MethodInvocation's target class doesn't match skeleton's target class.");
		}

		Method method;
		try {
			method = targetObject.getClass().getMethod(
					methodInvocation.getTargetMethodName(),
					methodInvocation.getParametersTypes());

			Object[] params = methodInvocation.getParametersValues();

			for (int i = 0; i < params.length; i++) {
				Object param = params[i];
				if (param instanceof MetaProxyStub) {
					MetaProxyStub metaProxyStub = (MetaProxyStub) param;

					Object proxyStub = ProxyStubFactory.createProxyStub(
							metaProxyStub, proxyActorSystem);
					params[i] = proxyStub;
				}
			}

			method.invoke(targetObject, methodInvocation.getParametersValues());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	public String getBeanId() {
		return beanId;
	}

}

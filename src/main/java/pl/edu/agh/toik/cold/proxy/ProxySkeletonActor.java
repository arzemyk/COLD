package pl.edu.agh.toik.cold.proxy;

import akka.actor.UntypedActor;

public class ProxySkeletonActor extends UntypedActor {

	private ProxySkeleton proxySkeleton;

	public ProxySkeletonActor(ProxySkeleton proxySkeleton) {
		this.proxySkeleton = proxySkeleton;
	}

	@Override
	public void onReceive(Object msg) throws Exception {

		if (msg instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation) msg;
			proxySkeleton.handleMethodInvocation(methodInvocation);

		} else {
			throw new RuntimeException("Received unexpected message type.");
		}

	}

}

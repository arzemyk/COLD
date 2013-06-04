package pl.edu.agh.toik.cold.proxy;

import org.apache.log4j.Logger;

import akka.actor.UntypedActor;

public class ProxyActorKiller extends UntypedActor {

	public static final String KILLER_ID = "proxy.actor.system.killer";
	
	private static Logger log = Logger.getLogger(ProxyActorKiller.class);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof SuicidePill) {
			log.info(((SuicidePill) message).getGoodByeMessage());
			context().system().shutdown();
		}
	}

}

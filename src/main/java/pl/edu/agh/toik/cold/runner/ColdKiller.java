package pl.edu.agh.toik.cold.runner;

import org.apache.log4j.Logger;

import pl.edu.agh.toik.cold.proxy.ProxyActorKiller;
import pl.edu.agh.toik.cold.proxy.ProxyActorSystem;
import pl.edu.agh.toik.cold.proxy.SuicidePill;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ColdKiller {

	private static Logger log = Logger.getLogger(ColdKiller.class);

	public static void main(String[] args) {

		ProxyActorSystem pas = new ProxyActorSystem("127.0.0.1", 0);

		try {
			for (String address : args) {

				String[] addrParts = address.split(":");
				if (addrParts.length != 2) {
					log.error("Bad address format: " + address);
				}

				String remotePath = String.format(
						"akka://ColdSystem@%s/user/%s", address,
						ProxyActorKiller.KILLER_ID);
				
				ActorRef remoteActor = pas.getActorSystem().actorFor(remotePath);
				remoteActor.tell(new SuicidePill(
						"Application stopped by Cold Killer"), null);
			}
		} catch (Exception e) {
			log.error("Exception thrown during shutdown process", e);
		} finally {
			pas.getActorSystem().shutdown();
		}
	}
}

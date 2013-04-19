package pl.edu.agh.toik.cold.proxy;

import akka.actor.ActorSystem;

import com.typesafe.config.ConfigFactory;

public class ProxyAgentSystem {

	private final String hostname;
	private final int port;
	private final ActorSystem actorSystem;
	
	public ProxyAgentSystem(String hostname, int port) {
		
		this.hostname = hostname;
		this.port = port;

		ConfigFactory.parseString(
				String.format("akka.remote.netty.hostname=\"%s\"", hostname))
				.withFallback(ConfigFactory.load());
		
		ConfigFactory.parseString(
				String.format("akka.remote.netty.hostname=\"%i\"", port))
				.withFallback(ConfigFactory.load());
		
		actorSystem = ActorSystem.create("ColdSystem");
		
	}

	public ActorSystem getActorSystem() {
		return actorSystem;
	}
	
	
}

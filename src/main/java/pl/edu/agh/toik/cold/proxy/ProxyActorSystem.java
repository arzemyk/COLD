package pl.edu.agh.toik.cold.proxy;

import akka.actor.ActorSystem;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ProxyActorSystem {

	private final String hostname;
	private final int port;
	private final ActorSystem actorSystem;

	public ProxyActorSystem(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;

		Config config = ConfigFactory.parseString(
				String.format("akka.remote.netty.hostname=\"%s\"\n"
						+ "akka.remote.netty.port=\"%d\"\n"
						+ "akka.actor.provider = \"akka.remote.RemoteActorRefProvider\"",
						hostname, port)).withFallback(ConfigFactory.load());

		actorSystem = ActorSystem.create("ColdSystem", config);
	}

	public ActorSystem getActorSystem() {
		return actorSystem;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

}

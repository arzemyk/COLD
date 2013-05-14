package pl.edu.agh.toik.cold.proxy;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorSystem;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ProxyActorSystem {

	private final String hostname;
	private final int port;
	private final ActorSystem actorSystem;
	private final Map<Object, MetaProxyStub> metaStubsMap = new HashMap<>();
	private final Map<Object, ProxySkeleton> skeletonsMap = new HashMap<>();

	public ProxyActorSystem(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;

		Config config = ConfigFactory.parseString(
				String.format("akka.remote.netty.hostname=\"%s\"\n"
						+ "akka.remote.netty.port=\"%d\"\n"
						+ "akka.actor.provider = \"akka.remote.RemoteActorRefProvider\"\n",
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
	
	public boolean isProxyStub(Object object) {
		return metaStubsMap.containsKey(object);
	}
	
	public MetaProxyStub getMetaProxyStub(Object object) {		
		return metaStubsMap.get(object);
	}
	
	public void addMetaProxyStub(Object object, MetaProxyStub metaProxyStub) {
		metaStubsMap.put(object, metaProxyStub);
	}
	
	public boolean hasProxySkeleton(Object object) {
		return skeletonsMap.containsKey(object);				
	}
	
	public ProxySkeleton getProxySkeleton(Object object) {
		return skeletonsMap.get(object);
	}
	
	public void addSkeleton(Object object, ProxySkeleton proxySkeleton) {
		skeletonsMap.put(object, proxySkeleton);
	}

}

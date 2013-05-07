package pl.edu.agh.toik.cold.tests;

import pl.edu.agh.toik.cold.proxy.ProxyActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class AkkaTest {

	private ProxyActorSystem pas0 = new ProxyActorSystem("127.0.0.1", 2552);
	private ProxyActorSystem pas1 = new ProxyActorSystem("127.0.0.1", 2553);
	
	public static void main(String args[]) {
		(new AkkaTest()).run();
	}
	
	
	public void run() {
		
		//pas0 = ActorSystem.create("printerAS", ConfigFactory.load().getConfig("printer"));
		final ActorRef printer = pas0.getActorSystem().actorOf(new Props(Printer.class), "printer");
		
		//pas1 = ActorSystem.create("senderAS", ConfigFactory.load().getConfig("sender"));
		//System.out.println(pas1.settings());
		final ActorRef sender = pas1.getActorSystem().actorOf(new Props(Sender.class), "sender");
		
		//printer.tell(new String());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sender.tell(new String(), null);
		
	}
}


class Printer extends UntypedActor {

	@Override
	public void onReceive(Object arg0) throws Exception {
		
		if (arg0 instanceof String) {
			System.out.println("I received: " + arg0 + ", from: " + getSender());
		}
	}
}

class Sender extends UntypedActor {

	@Override
	public void onReceive(Object arg0) throws Exception {

		System.out.println("Running");
		ActorRef actor = getContext().actorFor("akka://ColdSystem@127.0.0.1:2552/user/printer");
		System.out.println(actor);
		actor.tell(new String("secret message"), getSelf());
	}
	
}

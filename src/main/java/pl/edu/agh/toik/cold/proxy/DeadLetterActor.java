package pl.edu.agh.toik.cold.proxy;

import org.springframework.beans.BeansException;

import pl.edu.agh.toik.cold.utils.SpringApplicationContext;
import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.UntypedActor;

public class DeadLetterActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof DeadLetter) {

			DeadLetter deadLetter = (DeadLetter) message;
			ActorRef recipient = deadLetter.recipient();

			String beanId = recipient.path().name();
			String skeletonId = beanId + ".skeleton";

			try {
				Object obj = SpringApplicationContext.getContext().getBean(
						skeletonId);

				if (!(obj instanceof ProxySkeleton)) {
					throw new RuntimeException(
							"Skeleton bean exists, but is not a skeleton. That's really bad.");
				}

				ProxySkeleton skeleton = (ProxySkeleton) obj;
				skeleton.getProxySkeletonActor().tell(deadLetter.message(),
						deadLetter.sender());

			} catch (BeansException e) {
				throw new RuntimeException(
						"DeadLetter received, but target bean not found.");
			}

		}

	}

}

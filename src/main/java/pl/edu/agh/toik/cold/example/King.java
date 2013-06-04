package pl.edu.agh.toik.cold.example;

import java.util.ArrayList;
import java.util.Collection;

public class King extends WesterosCitizen {

	private boolean isPsychopathic;
	
	private Executioner executioner;
	private KingsHand kingsHand;
	
	private Collection<Head> headsGallery = new ArrayList<>();
	
	public King(boolean isPsychopathic) {
		this.isPsychopathic = isPsychopathic;
	}
	
	public King() {
		this(true);
	}

	public void judge(String information) {
		if(isPsychopathic) {
			spider.spy(getName(), "is psychopatic monster so he decides to kill " + kingsHand.getName());
			bringMeHisHead(kingsHand);
		}
	}
	
	private void bringMeHisHead(WesterosCitizen citizen) {
		Head head = executioner.cutHeadOff(citizen);
		headsGallery.add(head);
	}
	
	public void setExecutioner(Executioner executioner) {
		this.executioner = executioner;
	}
	
	public void setKingsHand(KingsHand kingsHand) {
		this.kingsHand = kingsHand;
	}
	
	
}

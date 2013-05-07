package pl.edu.agh.toik.cold.example;

public class IronThrone {

	private KingsHand kingsHand;
	private Spider spider;
	
	
	public void playGame(boolean winOrDie) {
		if(winOrDie) {
			happyScenario();
		} else {
			sadScenario();
		}
	}
	
	private void sadScenario() {
		kingsHand.punishBastard();
		kingsHand.askSmallCouncil();
		
		spider.printLittleBirdsReport();
	}
	
	private void happyScenario() {
		if(kingsHand.getName().contains("Stark")) {
			System.out.println("Sorry, Starks always die.");
			sadScenario();
		}
	}
	
	public void setKingsHand(KingsHand kingsHand) {
		this.kingsHand = kingsHand;
	}
	
	public void setSpider(Spider spider) {
		this.spider = spider;
	}
	
}

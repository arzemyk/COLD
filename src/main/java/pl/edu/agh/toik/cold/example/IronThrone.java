package pl.edu.agh.toik.cold.example;

public class IronThrone {

	private KingsHand kingsHand;
	private Spider spider;
	private Spider otherSpider = null;
	
	
	public void playGame(boolean winOrDie) {
		
		if (otherSpider != null) {
			spider.setOtherSpider(otherSpider);
		}
		
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
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		otherSpider.printLittleBirdsReport();
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

	public void setOtherSpider(Spider otherSpider) {
		this.otherSpider = otherSpider;
	}
	
}

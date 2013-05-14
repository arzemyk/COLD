package pl.edu.agh.toik.cold.example;

public class Spider extends SmallCouncilMember {

	private StringBuilder littleBirdsReport = new StringBuilder();
	private Spider otherSpider = null;
	
	public void spy(String who, String message) {
		littleBirdsReport.append(who + " : " + message + "\n");
		
		if (otherSpider != null) {
			otherSpider.spy(who, message);
		}
	}
	
	public void printLittleBirdsReport() {
		System.out.println(littleBirdsReport.toString());
	}
	
	@Override
	public void vote(String query) {
	}

	public Spider getOtherSpider() {
		return otherSpider;
	}

	public void setOtherSpider(Spider otherSpider) {
		this.otherSpider = otherSpider;
	}
	
}



package pl.edu.agh.toik.cold.example;

public class Bastard extends WesterosCitizen {
	
	
	private String reasonOfPunishment;
	
	
	public void goToTheWall() {
		spider.spy(this, "goes to the Wall, because " + reasonOfPunishment);
	}
	
	public void setReasonOfPunishment(String reasonOfPunishment) {
		this.reasonOfPunishment = reasonOfPunishment;
	}
}

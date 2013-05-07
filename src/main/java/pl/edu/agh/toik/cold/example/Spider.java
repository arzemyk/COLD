package pl.edu.agh.toik.cold.example;

public class Spider extends SmallCouncilMember {

	StringBuilder littleBirdsReport = new StringBuilder();
	
	public void spy(String who, String message) {
		littleBirdsReport.append(who + " : " + message + "\n");
	}
	
	public void printLittleBirdsReport() {
		System.out.println(littleBirdsReport.toString());
	}
	
	@Override
	public void vote(String query) {
	}
}



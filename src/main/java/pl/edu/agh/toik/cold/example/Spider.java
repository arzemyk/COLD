package pl.edu.agh.toik.cold.example;

public class Spider extends SmallCouncilMember {

	StringBuilder littleBirdsReport = new StringBuilder();
	
	public void spy(WesterosCitizen who, String message) {
		littleBirdsReport.append(who.getName() + " : " + message + "\n");
	}
	
	public String getLittleBirdsReport() {
		return littleBirdsReport.toString();
	}
	
	@Override
	public boolean vote(String query) {
		return true;
	}
}



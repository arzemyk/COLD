package pl.edu.agh.toik.cold.example;

public class Lord extends WesterosCitizen {

	private Bastard bastard;
	
	
	public void punishBastard() {
		
		if(hasBastard()) {
			bastard.goToTheWall();
		}
	}
	
	public boolean hasBastard() {
		return bastard != null;
	}
	
	public void setBastard(Bastard bastard) {
		this.bastard = bastard;
	}
	
}

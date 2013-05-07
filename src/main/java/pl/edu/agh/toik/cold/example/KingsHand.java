package pl.edu.agh.toik.cold.example;

public class KingsHand extends Lord {

	private String problemToSolve;
	private SmallCouncil council;
	
	public void askSmallCouncil() {
		spider.spy(getName(), "reveals that " + problemToSolve + " and tells it to small council.");
		council.vote(problemToSolve);
	}
	
	public void setProblemToSolve(String problemToSolve) {
		this.problemToSolve = problemToSolve;
	}
	
	public void setCouncil(SmallCouncil council) {
		this.council = council;
	}
}

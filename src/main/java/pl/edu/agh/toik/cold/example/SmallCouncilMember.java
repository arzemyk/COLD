package pl.edu.agh.toik.cold.example;

public class SmallCouncilMember extends Lord {

	
	private King king;
	
	
	public void vote(String query) {
		spider.spy(getName(), " is a hypocrite so he informs king about king's hand move");
		king.judge(query);
	}
	
	
	public void setKing(King king) {
		this.king = king;
	}
}

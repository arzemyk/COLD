package pl.edu.agh.toik.cold.example;

public class SmallCouncilMember extends Lord {

	
	private King king;
	
	
	public boolean vote(String query) {
		spider.spy(this, " is a hypocrite so he informs king about king's hand move");
		king.judge(query);
		return false;
	}
	
	
	public void setKing(King king) {
		this.king = king;
	}
}

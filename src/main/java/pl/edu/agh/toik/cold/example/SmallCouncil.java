package pl.edu.agh.toik.cold.example;

import java.util.Collection;

public class SmallCouncil {

	private Collection<SmallCouncilMember> members;

	public SmallCouncil(Collection<SmallCouncilMember> members) {
		this.members = members;
	}

	public void vote(String problem) {
		for (SmallCouncilMember member : members) {
			member.vote(problem);

		}
	}

}

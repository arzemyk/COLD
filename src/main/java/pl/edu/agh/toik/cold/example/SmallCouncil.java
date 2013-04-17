package pl.edu.agh.toik.cold.example;

import java.util.Collection;

public class SmallCouncil {

	private Collection<SmallCouncilMember> members;

	public SmallCouncil(Collection<SmallCouncilMember> members) {
		this.members = members;
	}

	public int getVotes(String problem) {
		int positiveVotes = 0;
		for (SmallCouncilMember member : members) {
			if (member.vote(problem)) {
				positiveVotes++;
			}
		}
		
		return positiveVotes;
	}

}

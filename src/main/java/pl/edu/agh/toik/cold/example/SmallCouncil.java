package pl.edu.agh.toik.cold.example;

import java.util.ArrayList;
import java.util.Collection;

public class SmallCouncil {

	private Collection<SmallCouncilMember> members;

	public SmallCouncil(Collection<SmallCouncilMember> members) {
		this.members = members;
	} 

	public SmallCouncil() {
		this(new ArrayList<SmallCouncilMember>());
	}

	public void vote(String problem) {
		for (SmallCouncilMember member : members) {
			member.vote(problem);

		}
	}

}

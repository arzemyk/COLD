package pl.edu.agh.toik.cold.example;

public class Executioner extends WesterosCitizen {

	public Head cutHeadOff(WesterosCitizen citizen) {
		spider.spy(getName(), "cuts " + citizen.getName() + " head off.");
		return new Head(citizen);
	}
}

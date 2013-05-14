package pl.edu.agh.toik.cold.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.err.println("You must pass spring XML file name as a parameter.");
			System.exit(1);
		}
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(args[0]);
		IronThrone throne = (IronThrone) ctx.getBean("example.beans.iron.throne");
		throne.playGame(false);
	}
}

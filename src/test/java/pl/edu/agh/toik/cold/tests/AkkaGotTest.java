package pl.edu.agh.toik.cold.tests;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AkkaGotTest {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err
					.println("You must pass spring XML file name as a parameter.");
			System.exit(1);
		}

		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(
				args[0]);
	}
}

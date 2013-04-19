package pl.edu.agh.toik.cold.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExampleDemo {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("Spring-GoT-Example.xml");
		IronThrone throne = (IronThrone) ctx.getBean("example.beans.iron.throne");
		throne.playGame(true);
	}
}

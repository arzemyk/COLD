package pl.edu.agh.toik.cold.example;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Spider extends SmallCouncilMember {

	private StringBuilder littleBirdsReport = new StringBuilder();
	private Spider otherSpider = null;

	private static Logger log;

	static {
		try {
			String logfileName = "GoT_"
					+ ManagementFactory.getRuntimeMXBean().getName() + ".log";
			BasicConfigurator.configure(new FileAppender(new PatternLayout(
					"%d [%c{1}] %p - %m%n"), logfileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log = Logger.getLogger(Spider.class);
	}

	synchronized public void spy(String who, String message) {
		String note = who + " : " + message;
		littleBirdsReport.append(note + "\n");

		if (otherSpider != null) {
			otherSpider.spy(who, message);
		}
		log.info(note);
	}

	public void printLittleBirdsReport() {
		System.out.println(littleBirdsReport.toString());
	}

	@Override
	public void vote(String query) {
	}

	public Spider getOtherSpider() {
		return otherSpider;
	}

	public void setOtherSpider(Spider otherSpider) {
		this.otherSpider = otherSpider;
	}

}

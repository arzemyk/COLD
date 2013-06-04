package pl.edu.agh.toik.cold.proxy;

import java.io.Serializable;

public class SuicidePill implements Serializable {

	private static final long serialVersionUID = -3362300005663386305L;
	
	private String goodByeMessage;

	
	public SuicidePill(String goodByeMessage) {
		this.goodByeMessage = goodByeMessage;
	}


	public String getGoodByeMessage() {
		return goodByeMessage;
	}

}

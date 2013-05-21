package pl.edu.agh.toik.cold.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		CONTEXT = context;
	}

	public static ApplicationContext getContext() {
		return CONTEXT;
	}
}
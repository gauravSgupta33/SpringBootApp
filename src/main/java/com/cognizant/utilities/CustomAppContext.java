package com.cognizant.utilities;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cognizant.config.AppConfig;

public class CustomAppContext {
	private static ApplicationContext context = null;
	
	public static ApplicationContext getContext() {
		if(context == null) {
			context = new AnnotationConfigApplicationContext(AppConfig.class);
		}
		return context;
	}

}

/*package com.talentica.job4j.impl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
	 
	public class SpringWebAppInitializer implements WebApplicationInitializer {
		
	    public void onStartup(ServletContext servletContext) throws ServletException {
	        XmlWebApplicationContext ctx = new XmlWebApplicationContext();
	        ctx.setConfigLocation("classpath:job4j-context.xml");
	        
			servletContext.addListener(new ContextLoaderListener(ctx));
			ctx.setServletContext(servletContext);
	    }
	 
	}
*/
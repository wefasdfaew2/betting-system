package com.org.test;

import java.io.File;
import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

public class TestTomcat {
	public static void main(String[] args) throws LifecycleException,
			InterruptedException, ServletException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		File catalinaHome = new File(".");
		

		Context rootContext = tomcat.addContext("/",
				catalinaHome.getAbsolutePath());

		// Define DefaultServlet.
		Wrapper defaultServlet = rootContext.createWrapper();
		defaultServlet.setName("default");
		defaultServlet
				.setServletClass("org.apache.catalina.servlets.DefaultServlet");
		defaultServlet.addInitParameter("debug", "0");
		defaultServlet.addInitParameter("listings", "false");
		defaultServlet.setLoadOnStartup(1);
		rootContext.addChild(defaultServlet);
		rootContext.addServletMapping("/", "default");

		// Define JspServlet.
		Wrapper jspServlet = rootContext.createWrapper();
		jspServlet.setName("jsp");
		jspServlet.setServletClass("org.apache.jasper.servlet.JspServlet");
		jspServlet.addInitParameter("fork", "false");
		jspServlet.addInitParameter("xpoweredBy", "false");
		jspServlet.setLoadOnStartup(2);
		rootContext.addChild(jspServlet);
		rootContext.addServletMapping("*.jsp", "jsp");

		Wrapper dbServlet = rootContext.createWrapper();
		dbServlet.setName("DbQuery");
		dbServlet.setServletClass("com.org.betmanager.DbQuery");
		dbServlet.setLoadOnStartup(2);
		rootContext.addChild(dbServlet);
		rootContext.addServletMapping("/BetAgentManager/DbQuery", "DbQuery");

		Wrapper ibetCaptcha = rootContext.createWrapper();
		ibetCaptcha.setName("IbetCaptcha");
		ibetCaptcha.setServletClass("com.org.betmanager.IbetCaptcha");
		ibetCaptcha.setLoadOnStartup(2);
		rootContext.addChild(ibetCaptcha);
		rootContext.addServletMapping("/BetAgentManager/IbetCaptcha",
				"IbetCaptcha");

		Wrapper loginSbo = rootContext.createWrapper();
		loginSbo.setName("LoginSbobet");
		loginSbo.setServletClass("com.org.betmanager.LoginSbobet");
		loginSbo.setLoadOnStartup(2);
		rootContext.addChild(loginSbo);
		rootContext.addServletMapping("/BetAgentManager/LoginSbobet", "LoginSbobet");

		Wrapper loginIbet = rootContext.createWrapper();
		loginIbet.setName("LoginIbet");
		loginIbet.setServletClass("com.org.betmanager.LoginIbet");
		loginIbet.setLoadOnStartup(2);
		rootContext.addChild(loginIbet);
		rootContext
				.addServletMapping("/BetAgentManager/LoginIbet", "LoginIbet");

		Wrapper refreshCaptcha = rootContext.createWrapper();
		refreshCaptcha.setName("RefreshIbetCaptcha");
		refreshCaptcha.setServletClass("com.org.betmanager.RefreshIbetCaptcha");
		refreshCaptcha.setLoadOnStartup(2);
		rootContext.addChild(refreshCaptcha);
		rootContext
				.addServletMapping("/BetAgentManager/refreshCaptcha", "RefreshIbetCaptcha");
		
		Wrapper tailer = rootContext.createWrapper();
		tailer.setName("TailerHandler");
		tailer.setServletClass("com.org.betmanager.TailerHandler");
		tailer.setLoadOnStartup(2);
		rootContext.addChild(tailer);
		rootContext
				.addServletMapping("/BetAgentManager/TailerHandler", "TailerHandler");
		
		
		tomcat.start();
		tomcat.getServer().await();

	}

}

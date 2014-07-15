package com.sirishrenukumar.mfa;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sirishrenukumar.mfa.core.MutualFundAnalyzerServlet;

public class MutualFundAnalyzer {

	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/application-config.xml");

		try {

			Properties props = new Properties();
			props.load(MutualFundAnalyzer.class.getResourceAsStream("/application.properties"));
			String port = (String)(System.getenv("PORT") == null? props.get("PORT"): System.getenv("PORT"));
			Server server = new Server(Integer.valueOf(port));
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
			context.addServlet(new ServletHolder(ctx.getBean(MutualFundAnalyzerServlet.class)),"/*");
			server.start();
			server.join();

		} finally {
			if (ctx != null) {
				((ClassPathXmlApplicationContext) ctx).close();
			}
		}
	}
}

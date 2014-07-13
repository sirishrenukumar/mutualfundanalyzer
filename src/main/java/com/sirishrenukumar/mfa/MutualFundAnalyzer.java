package com.sirishrenukumar.mfa;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sirishrenukumar.mfa.core.MutualFundPortfolioParser;
import com.sirishrenukumar.mfa.core.MutualFundSnapshotSummaryParser;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;

@ManagedBean
public class MutualFundAnalyzer extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		for(Stock stock : mutualFundAndStockManager.getStocksOrderedByNetAssets()) {
			resp.getOutputStream().println(stock.toString());
		}
	}

	@Inject
	private MutualFundSnapshotSummaryParser mutualFundSnapshotSummaryParser;

	@Inject
	private MutualFundPortfolioParser mutualFundPortfolioParser;
	
	@Inject
	private MutualFundAndStockManager mutualFundAndStockManager;

	private void parseAndInitialize() throws IOException {
			
			mutualFundSnapshotSummaryParser.parseMutualFundDetails();
			mutualFundPortfolioParser.parseMutualFundAndStockDetails();
			
	}

	public static void main(String[] args) throws Exception {
        
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/application-config.xml");
		
		try {
			ctx.getBean(MutualFundAnalyzer.class).parseAndInitialize();

	        Server server = new Server(5678);
	        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context.setContextPath("/");
	        server.setHandler(context);
	        context.addServlet(new ServletHolder(ctx.getBean(MutualFundAnalyzer.class)),"/*");
	        server.start();
	        server.join();
			
		} finally {
			if (ctx != null) {
				((ClassPathXmlApplicationContext) ctx).close();
			}
		}
	}
}

package com.sirishrenukumar.mfa;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		run();
	}

	@Inject
	private MutualFundSnapshotSummaryParser mutualFundSnapshotSummaryParser;

	@Inject
	private MutualFundPortfolioParser mutualFundPortfolioParser;
	
	@Inject
	private MutualFundAndStockManager mutualFundAndStockManager;

	private void run() throws IOException {
			
			mutualFundSnapshotSummaryParser.parseMutualFundDetails();
			mutualFundPortfolioParser.parseMutualFundAndStockDetails();
			
			for(Stock stock : mutualFundAndStockManager.getStocksOrderedByNetAssets()) {
				System.out.println(stock);
			}
	}

	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/application-config.xml");

		try {
			ctx.getBean(MutualFundAnalyzer.class).run();
			
		} finally {
			if (ctx != null) {
				((ClassPathXmlApplicationContext) ctx).close();
			}
		}
	}
}

package com.sirishrenukumar.mfa.core;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;

@ManagedBean
public class MutualFundAnalyzerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!initialized) {
			parseAndInitialize();
		}
		
		for(MutualFund mutualFund : mutualFundAndStockManager.getMutualFunds()) {
			resp.getOutputStream().println(mutualFund.toString());
		}
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
	
	private boolean initialized;

	private void parseAndInitialize() throws IOException {
			mutualFundSnapshotSummaryParser.parseMutualFundDetails();
			mutualFundPortfolioParser.parseMutualFundAndStockDetails();
			initialized = true;
	}
}

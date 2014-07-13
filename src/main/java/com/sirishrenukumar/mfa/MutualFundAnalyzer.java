package com.sirishrenukumar.mfa;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sirishrenukumar.mfa.core.MutualFundPortfolioParser;
import com.sirishrenukumar.mfa.core.MutualFundSnapshotSummaryParser;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;

@Configuration
@ComponentScan
public class MutualFundAnalyzer {
	
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

package com.sirishrenukumar.mfa;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.sirishrenukumar.mfa.core.MutualFundPortfolioParser;
import com.sirishrenukumar.mfa.core.MutualFundSnapshotSummaryParser;
import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.Stock;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;

@Configuration
@ComponentScan
@ImportResource(value = "classpath:spring/application-config.xml")
public class MutualFundAnalyzer {

	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = SpringApplication.run(
				MutualFundAnalyzer.class, args);


		try {
			MutualFundSnapshotSummaryParser mutualFundSnapshotSummaryParser = ctx.getBean(MutualFundSnapshotSummaryParser.class);
			MutualFundPortfolioParser mutualFundPortfolioParser = ctx.getBean(MutualFundPortfolioParser.class); 
			MutualFundAndStockManager mutualFundAndStockManager = ctx.getBean(MutualFundAndStockManager.class);
			
			mutualFundSnapshotSummaryParser.parseMutualFundDetails();
			mutualFundPortfolioParser.parseMutualFundAndStockDetails();
			
//			for(MutualFund mutualFund : mutualFundAndStockManager.getMutualFunds()) {
//				System.out.println(mutualFund);
//			}
			for(Stock stock : mutualFundAndStockManager.getStocksOrderedByName()) {
				System.out.println(stock);
			}
			
		} finally {
			if (ctx != null) {
				((AnnotationConfigApplicationContext) ctx).close();
			}
		}
	}
}

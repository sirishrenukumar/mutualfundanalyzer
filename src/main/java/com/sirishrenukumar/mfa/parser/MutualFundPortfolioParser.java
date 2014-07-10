package com.sirishrenukumar.mfa.parser;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sirishrenukumar.mfa.entity.MutualFund;
import com.sirishrenukumar.mfa.entity.StockMetrics;
import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;
import com.sirishrenukumar.mfa.web.constants.MappingConstants;

@ManagedBean
public class MutualFundPortfolioParser {
	
	@Inject
	private MutualFundAndStockManager mutualFundAndStockManager;
	
	
	private MutualFundPortfolioParser() {
		
	}
	
	public void parse() throws IOException {
		
		for(MutualFund mutualFund: mutualFundAndStockManager.getMutualFunds()) {
			
			Document doc = Jsoup.connect(String.format(MappingConstants.MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB,mutualFund.getCode())).get();
			Elements topHoldings = doc.select(MappingConstants.Selector.MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROWS);

//			System.out.println("Processing " + mutualFund);
			
			for(Element stockRow : topHoldings) {
				
//				System.out.println("Processing " + stockRow);
				
				String name = getName(stockRow);
				String sector = stockRow.select(MappingConstants.Selector.MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_SECTOR).get(0).ownText().trim();
				String assetPercentageString = stockRow.select(MappingConstants.Selector.MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_ASSET_PERCENTAGE).get(0).ownText().trim();
				
				mutualFundAndStockManager.associateStockWithMutualFund(name, sector, mutualFund, new StockMetrics(Float.parseFloat(assetPercentageString)));
			}
		}
	}

	private String getName(Element stockRow) {
		Elements elements = stockRow.select(MappingConstants.Selector.MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_NAME_WITH_HYPERLINK);
		if(elements.isEmpty()) {
			elements = stockRow.select(MappingConstants.Selector.MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_NAME_WITHOUT_HYPERLINK);
		}
		return elements.get(0).ownText().trim();
	}
	
}

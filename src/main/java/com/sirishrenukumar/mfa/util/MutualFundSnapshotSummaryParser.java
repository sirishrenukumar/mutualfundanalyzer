package com.sirishrenukumar.mfa.util;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sirishrenukumar.mfa.entity.managers.MutualFundAndStockManager;
import com.sirishrenukumar.mfa.web.constants.MappingConstants;

@ManagedBean
public class MutualFundSnapshotSummaryParser {
	
	@Inject
	private MutualFundAndStockManager mutualFundAndStockManager;
	
	public void parseMutualFundSummaryDetails() throws IOException {
		
		Document doc = Jsoup.connect(MappingConstants.ALL_EQUITY_FUNDS).get();
		Elements mfs = doc.select(MappingConstants.Selector.MUTUAL_FUND_ROWS);
		
		for(Element mf: mfs) {
			String idString = mf.select(MappingConstants.Selector.MUTUAL_FUND_CODE).attr(MappingConstants.AttributeName.MUTUAL_FUND_CODE).trim();
			String name = mf.select(MappingConstants.Selector.MUTUAL_FUND_NAME_COLUMN).html().trim();
			String ratingString = mf.select(MappingConstants.Selector.MUTUAL_FUND_RATING_COLUMN).attr(MappingConstants.AttributeName.MUTUAL_FUND_RATING).trim();
			String categoryString = mf.select(MappingConstants.Selector.MUTUAL_FUND_CATEGORY_COLUMN).attr(MappingConstants.AttributeName.MUTUAL_FUND_CATEGORY).trim();
			float netAssetsInCrores = Float.parseFloat(mf.select(MappingConstants.Selector.MUTUAL_FUND_NET_ASSETS_COLUMN).html().replace(",","").trim());
			mutualFundAndStockManager.storeMutualFund(idString, name, ratingString, categoryString, netAssetsInCrores);
		}
	}
}

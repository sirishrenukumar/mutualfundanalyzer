package com.sirishrenukumar.mfa.web.constants;

public class MappingConstants {
	
	private MappingConstants() {
	}
	
	/*
	 * URL for fetching all rated funds i.e excludes %2C5Star%2C4Star%2C3Star%2C1Star%2C1Star
	 */
	public static final String ALL_EQUITY_FUNDS = "http://www.valueresearchonline.com/funds/fundSelector/default.asp?amc=&cat=equityAll&exc=susp%2Cdir%2Cclose%2CnotRated&isTabChng=1&pg=";
	public static final String MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB = "http://www.valueresearchonline.com/funds/portfoliovr.asp?schemecode=%s";
	
	public static class Selector {
		
		/*
		 * Select tr tag with attribute=value as class=fundtool_cat that is the child of the tbody tag 
		 */
		public static final String MUTUAL_FUND_ROWS = "tbody tr.fundtool_cat";
		
		/*
		 * Select input tag with attribute called 'value' that is the immediate sibling of 
		 * the a tag that is the child of the td tag
		 */
		public static final String MUTUAL_FUND_CODE = String.format("td a + input[%s]", AttributeName.MUTUAL_FUND_CODE);
		
		/*
		 * Select the a tag with attribute=value as class=fundName that is the child of the td tag
		 */
		public static final String MUTUAL_FUND_NAME_COLUMN = "td a.fundName";
		
		/*
		 * Select input tag with attribute called 'value' that is the immediate sibling of the div tag that is the child of the td tag
		 */
		public static final String MUTUAL_FUND_CATEGORY_COLUMN = String.format("td div + input[%s]", AttributeName.MUTUAL_FUND_CATEGORY);
		public static final String MUTUAL_FUND_RATING_COLUMN = String.format("td[%s]", AttributeName.MUTUAL_FUND_RATING);
		public static final String MUTUAL_FUND_NET_ASSETS_COLUMN = "td:eq(8)";
		
		
		/*
		 * Match the div with class attribute using regex
		 * Fetch the table
		 * Fetch all table rows (skip past the first 2 header table rows)
		 */
		public static final String MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROWS = "div[class~=\\s*pull-left\\s+sectionHead\\s+margin_top15px\\s*] table:contains(holdings) tr:gt(1)";
		public static final String MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_NAME_WITH_HYPERLINK = "td:eq(1) a";
		public static final String MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_NAME_WITHOUT_HYPERLINK = "td:eq(1)";
		public static final String MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_SECTOR = "td:eq(2) a";
		public static final String MUTUAL_FUND_DETAILS_PAGE_PORTFOLIO_TAB_TOP_HOLDINGS_TABLE_ROW_ASSET_PERCENTAGE = "td:eq(6)";
		
		private Selector() {
		}
	}
	
	public static class AttributeName {
		
		public static final String MUTUAL_FUND_CODE = "value";
		public static final String MUTUAL_FUND_CATEGORY = "value";
		public static final String MUTUAL_FUND_RATING = "customparser";
		
		private AttributeName() {
			
		}
	}
}

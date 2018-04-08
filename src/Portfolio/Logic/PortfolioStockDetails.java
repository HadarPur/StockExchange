package Portfolio.Logic;

import java.util.ArrayList;

import Portfolio.Storage.PortfolioStockDetailsInfo;

public class PortfolioStockDetails {
	public static final String ASK = "A";
	public static final String BID = "B";

	private Integer id;
	private String kind;
	private String stockName;
	private Integer amount;
	private Integer price;
	
	public Integer getId() {
		return id;
	}

	public String getKind() {
		return kind;
	}

	public String getStockName() {
		return stockName;
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getPrice() {
		return price;
	}

	public PortfolioStockDetails(PortfolioStockDetailsInfo tmp) {
		this.id = tmp.getId();
		this.kind = tmp.getKind();
		this.stockName = tmp.getStockName();
		this.amount = tmp.getAmount();
		this.price = tmp.getPrice();
	}
	
	public ArrayList<?> loadHistory(){
		
		return null;
	}
}

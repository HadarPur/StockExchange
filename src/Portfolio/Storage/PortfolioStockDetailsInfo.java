package Portfolio.Storage;

import java.util.Scanner;

public class PortfolioStockDetailsInfo {

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

	public PortfolioStockDetailsInfo(Scanner s) {
		this.id = s.nextInt();
		this.kind = s.next();
		this.stockName = s.next();
		this.amount = s.nextInt();
		this.price = s.nextInt();
	}
}

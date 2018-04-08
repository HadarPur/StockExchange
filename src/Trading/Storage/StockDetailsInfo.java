package Trading.Storage;

import java.io.Serializable;
import java.util.Date;

import exchange.api.Order;

public class StockDetailsInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4168705455516233631L;
	private Integer id;
	private String kind;
	private String stockName;
	private Integer amount;
	private Integer price;
	private Date creationDate;

	public StockDetailsInfo() {
	}

	public StockDetailsInfo(Order o) {
		id = o.getId();
		kind = o.getKind();
		stockName = o.getStockName();
		amount = o.getAmount();
		price = o.getPrice();
		creationDate = o.getCreationDate();
		save(this);
	}

	private void save(StockDetailsInfo stockDetailsInfo) {
		TradingManager.save(stockDetailsInfo);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}

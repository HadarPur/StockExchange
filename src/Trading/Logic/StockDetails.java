package Trading.Logic;

import java.rmi.RemoteException;
import java.util.Date;

import Trading.Storage.StockDetailsInfo;
import Trading.Storage.TradingManager;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughMoneyException;
import exchange.api.NotEnoughStockException;
import exchange.api.Order;
import exchange.api.StockNotTradedException;

public class StockDetails {
	public static final String ASK = "A";
	public static final String BID = "B";
	final int DESTINATION = 3373;

	private Integer id;
	private String kind;
	private String stockName;
	private Integer amount;
	private Integer price;
	private Date creationDate;

	public StockDetails(String stockName, Integer amount, Integer price) {
		this.stockName = stockName;
		this.amount = amount;
		this.price = price;
	}

	public String getStockName() {
		return stockName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void buyStocks(String secret, Integer account, BankManager b, ExchangeManager e)
			throws RemoteException, DoesNotHaveThisAssetException, NotEnoughAssetException, WrongSecretException,
			InternalServerErrorException, NoSuchAccountException, NotEnoughStockException, StockNotTradedException,
			InternalExchangeErrorException, DoesNotHaveThisStockException, NotEnoughMoneyException, InterruptedException {
		b.transferAssets(secret, account, DESTINATION, "NIS", amount * price);
		Thread.sleep(1000);
		int buyID = e.placeBid(secret, account, stockName, amount, price);
		saveOrder(e.getOrderDetails(secret, account, buyID));
	}

	public void sellStocks(String secret, Integer account, BankManager b, ExchangeManager e) throws RemoteException,
			WrongSecretException, NoSuchAccountException, NotEnoughStockException, StockNotTradedException,
			DoesNotHaveThisStockException, InternalExchangeErrorException, NotEnoughMoneyException, DoesNotHaveThisAssetException, NotEnoughAssetException, InternalServerErrorException {
		b.transferAssets(secret, account, DESTINATION, stockName, amount);
		int sellID = e.placeAsk(secret, account, stockName, amount, price);
		saveOrder(e.getOrderDetails(secret, account, sellID));
	}

	public void saveOrder(Order o) {
		StockDetailsInfo temp =  new StockDetailsInfo(o);
	}
	
	
}

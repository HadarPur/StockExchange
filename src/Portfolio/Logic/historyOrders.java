package Portfolio.Logic;

import java.io.IOException;
import java.util.ArrayList;

import Portfolio.Storage.PortfolioManager;
import Portfolio.Storage.PortfolioStockDetailsInfo;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;

public class historyOrders {
	private BankManager b;
	private ExchangeManager e;
	private String secret;
	private Integer account;
	private PortfolioManager pManager;

	public historyOrders(BankManager b, ExchangeManager e, String secret, Integer account) throws WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException, NoSuchAccountException, InternalExchangeErrorException, IOException {
		this.b = b;
		this.e = e;
		this.secret = secret;
		this.account = account;
		pManager = new PortfolioManager(b, e, secret, account);
	}

	public ArrayList<PortfolioStockDetails> getHistoryOrders() {
		ArrayList<PortfolioStockDetailsInfo> tmp = pManager.getHistoryOrders();
		ArrayList<PortfolioStockDetails> historyOrders = new ArrayList<PortfolioStockDetails>();
		PortfolioStockDetails orderTemp;
		for (int i = 0; i < tmp.size(); i++) {
			orderTemp = new PortfolioStockDetails(tmp.get(i));
			historyOrders.add(orderTemp);
		}
		return historyOrders;
	}
}

package Portfolio.Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;

public class PortfolioManager {
	private BankManager b;
	private ExchangeManager e;
	private final String FILEPATH = "history.dat";
	private File historyFile = new File(FILEPATH);
	private ArrayList<PortfolioStockDetailsInfo> historyOrders = new ArrayList<PortfolioStockDetailsInfo>();

	public PortfolioManager(BankManager b, ExchangeManager e, String secret, Integer account)
			throws WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException,
			NoSuchAccountException, InternalExchangeErrorException, IOException {
		this.b = b;
		this.e = e;
		if (historyFile.createNewFile())
			historyFile = new File(FILEPATH);
		loadHistoryOrders(secret, account, historyFile);
	}
	
	private void loadHistoryOrders(String secret, Integer account, File historyFile) throws FileNotFoundException {
		Scanner s = new Scanner(historyFile);
		while(s.hasNext()){
			PortfolioStockDetailsInfo temp = new PortfolioStockDetailsInfo(s);
			historyOrders.add(temp);
		}
		s.close();
	}
	public ArrayList<PortfolioStockDetailsInfo> getHistoryOrders() {
		return historyOrders;
	}
}

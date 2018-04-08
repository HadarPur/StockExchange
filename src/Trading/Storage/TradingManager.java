package Trading.Storage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Scanner;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;

public class TradingManager {
	private BankManager b;
	private ExchangeManager e;
	private final static String FILEPATH = "history.dat";
	private File historyFile = new File(FILEPATH);
	
	public TradingManager(BankManager b, ExchangeManager e, String secret, Integer account)
			throws IOException, WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException {
		this.b = b;
		this.e = e;
		if (historyFile.createNewFile())
			historyFile = new File(FILEPATH);
	}

	public static void save(StockDetailsInfo o) {
		PrintStream systemPw = new PrintStream(new AppendFileStream(FILEPATH));
		systemPw.print(o.getId() + " ");
		systemPw.print(o.getKind() + " ");
		systemPw.print(o.getStockName() + " ");
		systemPw.print(o.getAmount() + " ");
		systemPw.print(o.getPrice() + " ");
	}

	public static StockDetailsInfo load(Scanner s) {
		StockDetailsInfo temp = new StockDetailsInfo();
		temp.setId(s.nextInt());
		temp.setKind(s.next());
		temp.setStockName(s.next());
		temp.setAmount(s.nextInt());
		temp.setPrice(s.nextInt());
		return temp;
	}

}

class AppendFileStream extends OutputStream {
	RandomAccessFile fd;

	public AppendFileStream(String file) {
		try {
			fd = new RandomAccessFile(file, "rw");
			fd.seek(fd.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		fd.close();
	}

	public void write(byte[] b) throws IOException {
		fd.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		fd.write(b, off, len);
	}

	public void write(int b) throws IOException {
		fd.write(b);
	}

}

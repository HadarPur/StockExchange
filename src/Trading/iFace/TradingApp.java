package Trading.iFace;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Trading.Logic.StockDetails;
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
import exchange.api.StockNotTradedException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class TradingApp extends JPanel {

	private static final long serialVersionUID = 1L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	public final Color MYBLUE = new Color(49, 79, 107);
	ExchangeManager e;
	BankManager b;
	String secret;
	Integer account;
	TitledBorder tborder = BorderFactory.createTitledBorder("");
	JPanel supplyPanel = new JPanel();
	JPanel demandPanel = new JPanel();
	JPanel ownedStocksPanel = new JPanel();
	JPanel newOrderPanel = new JPanel();
	TradingManager tManger;

	public TradingApp(String secret, Integer account, BankManager b, ExchangeManager e, KeyListener tradingKeyListener)
			throws NotBoundException, WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException,
			IOException {
		this.e = e;
		this.b = b;
		this.secret = secret;
		this.account = account;
		tManger = new TradingManager(b, e, secret, account);
		setSize(screenSize);
		setOpaque(false);
		setBorder(new LineBorder(Color.white));
		setLayout(new GridLayout(4, 1));
		setFocusable(true);
		addKeyListener(tradingKeyListener);
		panelsOptions(tradingKeyListener);
		JScrollPane scrollPane_1 = new JScrollPane(supplyPanel);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_1);

		JScrollPane scrollPane_2 = new JScrollPane(demandPanel);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_2);

		JScrollPane scrollPane_3 = new JScrollPane(ownedStocksPanel);
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_3);

		newOrderPanel.setFocusable(true);
		newOrderPanel.addKeyListener(tradingKeyListener);
		add(newOrderPanel);
	}

	private void panelsOptions(KeyListener tradingKeyListener) throws RemoteException {
		supplyPanel.setPreferredSize(screenSize);
		supplyPanel.setBackground(MYBLUE);
		supplyPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		supplyPanel.setBorder(
				BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "Market's Supply", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
		supplyPanel.add(new StockMenuPanel());
		for (String stockName : e.getStockNames()) {
			Map<Integer, Integer> supply = e.getSupply(stockName);
			for (Map.Entry<Integer, Integer> q : supply.entrySet()) {
				supplyPanel.add(new StockInfoPanel(stockName, q.getKey(), q.getValue()));
			}
		}
		demandPanel.setPreferredSize(screenSize);
		demandPanel.setBackground(MYBLUE);
		demandPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		demandPanel.setBorder(
				BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "Market's Demand", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
		demandPanel.add(new StockMenuPanel());
		for (String stockName : e.getStockNames()) {
			Map<Integer, Integer> demand = e.getDemand(stockName);
			for (Map.Entry<Integer, Integer> q : demand.entrySet()) {
				demandPanel.add(new StockInfoPanel(stockName, q.getKey(), q.getValue()));
			}
		}
		ownedStocksPanel.setPreferredSize(screenSize);
		ownedStocksPanel.setBackground(MYBLUE);
		ownedStocksPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		ownedStocksPanel
				.setBorder(BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "Stocks I Own", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
		ownedStocksPanel.add(new StockMenuPanel());
		try {
			for (String stockName : b.getAssets(secret, account)) {
				if (!stockName.equals("NIS")) {
					Integer amount = b.getQuantityOfAsset(secret, account, stockName);
					ownedStocksPanel.add(new StockInfoPanel(stockName, -1, amount));
				}
			}
		} catch (RemoteException | WrongSecretException | InternalServerErrorException
				| DoesNotHaveThisAssetException e) {
			e.printStackTrace();
		}
		newOrderPanel.setPreferredSize(screenSize);
		newOrderPanel.setBackground(MYBLUE);
		newOrderPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		newOrderPanel.setVisible(true);
		JTextArea currMoney = new JTextArea();
		currMoney.setOpaque(false);
		currMoney.setEditable(false);
		currMoney.setForeground(Color.white);
		currMoney.setLineWrap(true);
		currMoney.setWrapStyleWord(true);
		currMoney.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
		currMoney.setPreferredSize(new Dimension(width, height));
		currMoney.setFocusable(true);
		currMoney.addKeyListener(tradingKeyListener);
		try {
			currMoney.setText("You currently have: " + b.getQuantityOfAsset(secret, account, "NIS")
					+ " NIS in the Bank\nPress on this panel and then \"Esc\" to return");
		} catch (RemoteException | WrongSecretException | DoesNotHaveThisAssetException
				| InternalServerErrorException e1) {
			currMoney.setText("Error: " + e1.toString());
		}
		JComboBox<String> stockNamesCB = new JComboBox<String>();
		try {
			for (String stockName : e.getStockNames())
				stockNamesCB.addItem(stockName);
		} catch (RemoteException e1) {
			currMoney.setText("Error: " + e1.toString());
		}
		stockNamesCB.setBackground(MYBLUE);
		stockNamesCB.setForeground(Color.white);
		stockNamesCB.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		stockNamesCB.setPreferredSize(new Dimension(200, 30));
		JTextField amountTF = new JTextField("Amount");
		amountTF.setBackground(MYBLUE);
		amountTF.setForeground(Color.white);
		amountTF.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		amountTF.setPreferredSize(new Dimension(200, 30));
		amountTF.setHorizontalAlignment(JTextField.CENTER);
		JTextField priceTF = new JTextField("Price");
		priceTF.setBackground(MYBLUE);
		priceTF.setForeground(Color.white);
		priceTF.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		priceTF.setPreferredSize(new Dimension(200, 30));
		priceTF.setHorizontalAlignment(JTextField.CENTER);
		ButtonGroup btnGrp = new ButtonGroup();
		JRadioButton buyBtn = new JRadioButton("Bid Request");
		buyBtn.setOpaque(false);
		buyBtn.setSelected(true);
		buyBtn.setForeground(Color.white);
		buyBtn.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		JRadioButton sellBtn = new JRadioButton("Ask Request");
		sellBtn.setOpaque(false);
		sellBtn.setForeground(Color.white);
		sellBtn.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		btnGrp.add(buyBtn);
		btnGrp.add(sellBtn);
		JButton orderButton = new JButton("Send Order Request");
		orderButton.setPreferredSize(new Dimension(200, 30));
		orderButton.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		orderButton.setBackground(MYBLUE);
		orderButton.setForeground(Color.white);
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				StockDetails temp = new StockDetails((String) stockNamesCB.getSelectedItem(),
						Integer.parseInt(amountTF.getText()), Integer.parseInt(priceTF.getText()));
				if (buyBtn.isSelected())
					try {
						temp.buyStocks(secret, account, b, e);
						currMoney.setText(currMoney.getText() + " Action Successful!");
					} catch (RemoteException | DoesNotHaveThisAssetException | NotEnoughAssetException
							| WrongSecretException | InternalServerErrorException | NoSuchAccountException
							| NotEnoughStockException | StockNotTradedException | InternalExchangeErrorException
							| DoesNotHaveThisStockException | NotEnoughMoneyException | InterruptedException e) {
						currMoney.setText(currMoney.getText() + " - Error: " + e.toString());
					}
				else
					try {
						temp.sellStocks(secret, account, b, e);
						currMoney.setText(currMoney.getText() + " Action Successful!");
					} catch (RemoteException | WrongSecretException | NoSuchAccountException | NotEnoughStockException
							| StockNotTradedException | DoesNotHaveThisStockException | InternalExchangeErrorException
							| NotEnoughMoneyException | DoesNotHaveThisAssetException | NotEnoughAssetException | InternalServerErrorException e) {
						currMoney.setText(currMoney.getText() + " - Error: " + e.toString());
					}
			}
		});
		newOrderPanel.add(stockNamesCB);
		newOrderPanel.add(amountTF);
		newOrderPanel.add(priceTF);
		newOrderPanel.add(buyBtn);
		newOrderPanel.add(sellBtn);
		newOrderPanel.add(orderButton);
		newOrderPanel.add(currMoney);
	}

	class StockMenuPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		JTextField name = new JTextField("Stock Name");
		JTextField amount = new JTextField("Amount");
		JTextField price = new JTextField("Price");

		public StockMenuPanel() {
			setSize(new Dimension(width, 20));
			setPreferredSize(new Dimension(width, 20));
			setVisible(true);
			setOpaque(false);
			setLayout(new FlowLayout((FlowLayout.LEADING), 0, 0));
			name.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			name.setOpaque(false);
			name.setForeground(Color.white);
			name.setPreferredSize(new Dimension((width) / 3, 20));
			name.setEditable(false);
			name.setHorizontalAlignment(JTextField.CENTER);
			add(name);
			amount.setOpaque(false);
			amount.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			amount.setForeground(Color.white);
			amount.setPreferredSize(new Dimension((width) / 3, 20));
			amount.setEditable(false);
			amount.setHorizontalAlignment(JTextField.CENTER);
			add(amount);
			price.setOpaque(false);
			price.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			price.setForeground(Color.white);
			price.setBackground(Color.WHITE);
			price.setPreferredSize(new Dimension((width) / 3, 20));
			price.setEditable(false);
			price.setHorizontalAlignment(JTextField.CENTER);
			add(price);
		}
	}

	class StockInfoPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		JTextField name = new JTextField();
		JTextField amount = new JTextField();
		JTextField price = new JTextField();

		public StockInfoPanel(String stockName, Integer stockPrice, Integer stockAmount) {
			setSize(new Dimension(width, 20));
			setPreferredSize(new Dimension(width, 20));
			setVisible(true);
			setOpaque(false);
			setLayout(new FlowLayout((FlowLayout.LEADING), 0, 0));
			name.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			name.setOpaque(false);
			name.setText(stockName);
			name.setForeground(Color.white);
			name.setPreferredSize(new Dimension((width) / 3, 20));
			name.setEditable(false);
			name.setHorizontalAlignment(JTextField.CENTER);
			add(name);
			amount.setOpaque(false);
			amount.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			amount.setText(stockAmount.toString());
			amount.setForeground(Color.white);
			amount.setPreferredSize(new Dimension((width) / 3, 20));
			amount.setEditable(false);
			amount.setHorizontalAlignment(JTextField.CENTER);
			add(amount);
			price.setOpaque(false);
			price.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			price.setForeground(Color.white);
			if (stockPrice != -1) {
				price.setText(stockPrice.toString());
			}
			price.setBackground(Color.WHITE);
			price.setPreferredSize(new Dimension((width) / 3, 20));
			price.setEditable(false);
			price.setHorizontalAlignment(JTextField.CENTER);
			add(price);
		}

	}

	class StockHistoryPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		JTextField name = new JTextField();
		JTextField amount = new JTextField();
		JTextField price = new JTextField();
		JTextField amountToBid = new JTextField();
		JTextField bidPrice = new JTextField();
		JButton buySell = new JButton();

		public StockHistoryPanel(StockDetails s) {
			setSize(new Dimension(width, 20));
			setPreferredSize(new Dimension(width, 20));
			setVisible(true);
			setOpaque(false);
			setLayout(new FlowLayout((FlowLayout.LEADING), 0, 0));
			name.setOpaque(false);
			name.setForeground(Color.white);
			name.setText(s.getStockName());
			name.setPreferredSize(new Dimension((width - 100) / 5, 20));
			name.setEditable(false);
			name.setHorizontalAlignment(JTextField.CENTER);
			add(name);
			amount.setOpaque(false);
			amount.setForeground(Color.white);
			amount.setText(s.getAmount().toString());
			amount.setPreferredSize(new Dimension((width - 100) / 5, 20));
			amount.setEditable(false);
			amount.setHorizontalAlignment(JTextField.CENTER);
			add(amount);
			price.setOpaque(false);
			price.setForeground(Color.white);
			price.setBackground(Color.WHITE);
			price.setPreferredSize(new Dimension((width - 100) / 5, 20));
			price.setEditable(false);
			price.setHorizontalAlignment(JTextField.CENTER);
			add(price);
			amountToBid.setOpaque(false);
			amountToBid.setForeground(Color.white);
			amountToBid.setBackground(Color.WHITE);
			amountToBid.setPreferredSize(new Dimension((width - 100) / 5, 20));
			amountToBid.setHorizontalAlignment(JTextField.CENTER);
			add(amountToBid);
			bidPrice.setOpaque(false);
			bidPrice.setForeground(Color.white);
			bidPrice.setPreferredSize(new Dimension((width - 100) / 5, 20));
			bidPrice.setHorizontalAlignment(JTextField.CENTER);
			add(bidPrice);
			buySell.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			buySell.setPreferredSize(new Dimension(90, 20));
			buySell.setHorizontalAlignment(JTextField.CENTER);
			add(buySell);
		}
	}

}

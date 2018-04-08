package Portfolio.iFace;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Portfolio.Logic.PortfolioStockDetails;
import Portfolio.Logic.historyOrders;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.Order;

public class PortfolioApp extends JPanel {

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
	JPanel openOrdersPanels = new JPanel();
	JPanel soldOrders = new JPanel();
	JPanel boughtOrders = new JPanel();
	JPanel overallInfo = new JPanel();
	historyOrders hOrders;
	Integer IBM = 0;
	Integer TEVA = 0;
	Integer INTEL = 0;
	
	public PortfolioApp(String secret, Integer account, BankManager b, ExchangeManager e, KeyListener tradingKeyListener) throws NotBoundException,
			WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException {
		this.e = e;
		this.b = b;
		this.secret = secret;
		this.account = account;
		setSize(screenSize);
		setOpaque(false);
		setFocusable(true);
		addKeyListener(tradingKeyListener);
		setBorder(new LineBorder(Color.white));
		setLayout(new GridLayout(4, 1));
		
		panelsOptions(tradingKeyListener);
		
		JScrollPane scrollPane_1 = new JScrollPane(openOrdersPanels);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_1);

		JScrollPane scrollPane_2 = new JScrollPane(soldOrders);
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_2);

		JScrollPane scrollPane_3 = new JScrollPane(boughtOrders);
		scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_3);

		JScrollPane scrollPane_4 = new JScrollPane(overallInfo);
		scrollPane_4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane_4);
	}

	private void panelsOptions(KeyListener tradingKeyListener) {
		try {
			hOrders = new historyOrders(b, e, secret, account);
		} catch (WrongSecretException | InternalServerErrorException | DoesNotHaveThisAssetException
				| NoSuchAccountException | InternalExchangeErrorException | IOException e2) {
			e2.printStackTrace();
		}
		ArrayList<PortfolioStockDetails> ordersList = hOrders.getHistoryOrders();
		openOrdersPanels.setPreferredSize(screenSize);
		openOrdersPanels.setBackground(MYBLUE);
		openOrdersPanels.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		openOrdersPanels.setBorder(
				BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "My Open Orders", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
		openOrdersPanels.add(new PortfolioMenuPanel());
		try {
			for (Integer stockID : e.getOpenOrders(secret, account)) {
					for (int i = 0; i < ordersList.size(); i++) {
						if (stockID.equals(ordersList.get(i).getId()))
							ordersList.remove(i);
					}
					openOrdersPanels.add(new PortfolioInfoPanel(e.getOrderDetails(secret, account, stockID)));
			}
		} catch (RemoteException | NoSuchAccountException | WrongSecretException | InternalExchangeErrorException e1) {
			e1.printStackTrace();
		}
		soldOrders.setPreferredSize(screenSize);
		soldOrders.setBackground(MYBLUE);
		soldOrders.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		soldOrders.setBorder(
				BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "Sold Stocks History", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
		for (int i = 0; i < ordersList.size(); i++) {
			PortfolioStockDetails temp = ordersList.get(i);
			if(temp.getKind().equals("A")){
				soldOrders.add(new PortfolioInfoPanel(ordersList.get(i)));
				if(temp.getStockName().equals("IBM"))
					IBM += (temp.getAmount()*temp.getPrice());
				else if(temp.getStockName().equals("INTEL"))
					INTEL += (temp.getAmount()*temp.getPrice());
				else if(temp.getStockName().equals("TEVA"))
					TEVA += (temp.getAmount()*temp.getPrice());				
			}
		}
		boughtOrders.setPreferredSize(screenSize);
		boughtOrders.setBackground(MYBLUE);
		boughtOrders.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		boughtOrders
				.setBorder(BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "Bought Stocks History", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
		for (int i = 0; i < ordersList.size(); i++) {
			PortfolioStockDetails temp = ordersList.get(i);
			if(temp.getKind().equals("B"))
			{
				boughtOrders.add(new PortfolioInfoPanel(ordersList.get(i)));
				if(temp.getStockName().equals("IBM"))
					IBM -= (temp.getAmount()*temp.getPrice());
				else if(temp.getStockName().equals("INTEL"))
					INTEL -= (temp.getAmount()*temp.getPrice());
				else if(temp.getStockName().equals("TEVA"))
					TEVA -= (temp.getAmount()*temp.getPrice());				
			}
		}
		overallInfo.setPreferredSize(screenSize);
		overallInfo.setBackground(MYBLUE);
		overallInfo.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		overallInfo.setFocusable(true);
		overallInfo.addKeyListener(tradingKeyListener);
		overallInfo.setBorder(
				BorderFactory.createTitledBorder(new LineBorder(MYBLUE), "Overall Info", TitledBorder.CENTER,
						TitledBorder.DEFAULT_POSITION, new Font("Serif", Font.BOLD, 24), Color.WHITE));
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
					+ " NIS in the Bank\nPress on this panel and then \"Esc\" to return"
					+ "\nGeneral Returns: "
					+ "\nGeneral Profit\\Loss: "+(TEVA+INTEL+IBM)
					+ "\nTEVA Profit\\Loss: " + TEVA
					+ "\nINTEL Profit\\Loss: " + INTEL
					+ "\nIBM Profit\\Loss: " + IBM);
		} catch (RemoteException | WrongSecretException | DoesNotHaveThisAssetException
				| InternalServerErrorException e1) {
			currMoney.setText("Error: " + e1.toString());
		}
		overallInfo.add(currMoney);
	}
	
	class PortfolioMenuPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		JTextField name = new JTextField("Stock Name");
		JTextField amount = new JTextField("Amount");
		JTextField price = new JTextField("Price");
		JTextField kind = new JTextField("Kind");
		JTextField creationDate = new JTextField("Creation Date");

		public PortfolioMenuPanel() {
			setSize(new Dimension(width, 20));
			setPreferredSize(new Dimension(width, 20));
			setVisible(true);
			setOpaque(false);
			setLayout(new FlowLayout((FlowLayout.LEADING), 0, 0));
			name.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			name.setOpaque(false);
			name.setForeground(Color.white);
			name.setPreferredSize(new Dimension((width) / 5, 20));
			name.setEditable(false);
			name.setHorizontalAlignment(JTextField.CENTER);
			add(name);
			amount.setOpaque(false);
			amount.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			amount.setForeground(Color.white);
			amount.setPreferredSize(new Dimension((width) / 5, 20));
			amount.setEditable(false);
			amount.setHorizontalAlignment(JTextField.CENTER);
			add(amount);
			price.setOpaque(false);
			price.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			price.setForeground(Color.white);
			price.setBackground(Color.WHITE);
			price.setPreferredSize(new Dimension((width) / 5, 20));
			price.setEditable(false);
			price.setHorizontalAlignment(JTextField.CENTER);
			add(price);
			kind.setOpaque(false);
			kind.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			kind.setForeground(Color.white);
			kind.setBackground(Color.WHITE);
			kind.setPreferredSize(new Dimension((width) / 5, 20));
			kind.setEditable(false);
			kind.setHorizontalAlignment(JTextField.CENTER);
			add(kind);
			creationDate.setOpaque(false);
			creationDate.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			creationDate.setForeground(Color.white);
			creationDate.setBackground(Color.WHITE);
			creationDate.setPreferredSize(new Dimension((width) / 5, 20));
			creationDate.setEditable(false);
			creationDate.setHorizontalAlignment(JTextField.CENTER);
			add(creationDate);
		}
	}
	
	class PortfolioInfoPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		JTextField name = new JTextField();
		JTextField amount = new JTextField();
		JTextField price = new JTextField();
		JTextField kind = new JTextField();
		JTextField creationDate = new JTextField();
		
		public PortfolioInfoPanel(PortfolioStockDetails o) {
			setSize(new Dimension(width, 20));
			setPreferredSize(new Dimension(width, 20));
			setVisible(true);
			setOpaque(false);
			setLayout(new FlowLayout((FlowLayout.LEADING), 0, 0));
			name.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			name.setOpaque(false);
			name.setText(o.getStockName());
			name.setForeground(Color.white);
			name.setPreferredSize(new Dimension((width) / 4, 20));
			name.setEditable(false);
			name.setHorizontalAlignment(JTextField.CENTER);
			add(name);
			amount.setOpaque(false);
			amount.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			amount.setText(o.getAmount().toString());
			amount.setForeground(Color.white);
			amount.setPreferredSize(new Dimension((width) / 4, 20));
			amount.setEditable(false);
			amount.setHorizontalAlignment(JTextField.CENTER);
			add(amount);
			price.setOpaque(false);
			price.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			price.setForeground(Color.white);
			price.setText(o.getPrice().toString());
			price.setBackground(Color.WHITE);
			price.setPreferredSize(new Dimension((width) / 4, 20));
			price.setEditable(false);
			price.setHorizontalAlignment(JTextField.CENTER);
			add(price);
			kind.setOpaque(false);
			kind.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			kind.setForeground(Color.white);
			kind.setText(o.getKind());
			kind.setBackground(Color.WHITE);
			kind.setPreferredSize(new Dimension((width) / 4, 20));
			kind.setEditable(false);
			kind.setHorizontalAlignment(JTextField.CENTER);
			add(kind);
//			creationDate.setOpaque(false);
//			creationDate.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
//			creationDate.setForeground(Color.white);
//			creationDate.setText("NULL");
//			creationDate.setBackground(Color.WHITE);
//			creationDate.setPreferredSize(new Dimension((width) / 5, 20));
//			creationDate.setEditable(false);
//			creationDate.setHorizontalAlignment(JTextField.CENTER);
//			add(creationDate);
		}
		
		public PortfolioInfoPanel(Order o) {
			setSize(new Dimension(width, 20));
			setPreferredSize(new Dimension(width, 20));
			setVisible(true);
			setOpaque(false);
			setLayout(new FlowLayout((FlowLayout.LEADING), 0, 0));
			name.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			name.setOpaque(false);
			name.setText(o.getStockName());
			name.setForeground(Color.white);
			name.setPreferredSize(new Dimension((width) / 5, 20));
			name.setEditable(false);
			name.setHorizontalAlignment(JTextField.CENTER);
			add(name);
			amount.setOpaque(false);
			amount.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			amount.setText(o.getAmount().toString());
			amount.setForeground(Color.white);
			amount.setPreferredSize(new Dimension((width) / 5, 20));
			amount.setEditable(false);
			amount.setHorizontalAlignment(JTextField.CENTER);
			add(amount);
			price.setOpaque(false);
			price.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			price.setForeground(Color.white);
			price.setText(o.getPrice().toString());
			price.setBackground(Color.WHITE);
			price.setPreferredSize(new Dimension((width) / 5, 20));
			price.setEditable(false);
			price.setHorizontalAlignment(JTextField.CENTER);
			add(price);
			kind.setOpaque(false);
			kind.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			kind.setForeground(Color.white);
			kind.setText(o.getKind());
			kind.setBackground(Color.WHITE);
			kind.setPreferredSize(new Dimension((width) / 5, 20));
			kind.setEditable(false);
			kind.setHorizontalAlignment(JTextField.CENTER);
			add(kind);
			creationDate.setOpaque(false);
			creationDate.setFont(new Font("Serif", Font.HANGING_BASELINE, 16));
			creationDate.setForeground(Color.white);
			creationDate.setText(o.getCreationDate().toString());
			creationDate.setBackground(Color.WHITE);
			creationDate.setPreferredSize(new Dimension((width) / 5, 20));
			creationDate.setEditable(false);
			creationDate.setHorizontalAlignment(JTextField.CENTER);
			add(creationDate);
		}
	}
}

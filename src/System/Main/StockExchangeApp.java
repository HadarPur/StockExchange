package System.Main;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;	
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Portfolio.iFace.PortfolioApp;
import Trading.iFace.TradingApp;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class StockExchangeApp {
	private JFrame frmHst;
	public final int LOGINX_SIZE = 500, LOGINY_SIZE = 250;
	public final Color MYBLUE = new Color(49, 79, 107);
	public final String SECRET = "exjyht";
	public final int ACCOUNT = 105;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	private BankManager b = (BankManager) Naming.lookup("rmi://13.59.120.241/Bank");
	private ExchangeManager e = (ExchangeManager) Naming.lookup("rmi://13.59.120.241/Exchange");

	private JPanel loginWindow = new JPanel();
	private JPanel loginPanel = new JPanel();
	private JPanel loginWelcomePanel = new JPanel();
	private String welcomeText = "Welcome to HST Technologies! " + "List of authors:\nHadar Pur , ID: 308248533\n"
			+ "Shiran Sofer , ID: 308535483\n" + "Tzah Mazuz , ID: 308107192\n" + "";
	private JLabel logoLabel = new JLabel(new ImageIcon(StockExchangeApp.class.getResource("/HST-150-75.png")));
	private JLabel miniLogoLabel = new JLabel(new ImageIcon(StockExchangeApp.class.getResource("/HST-MINI.jpg")));
	private JTextField userTF = new JTextField("Username");
	private JPasswordField passwordTF = new JPasswordField("Password");
	private JButton loginButton = new JButton("Login");
	private JButton registerButton = new JButton("Register");
	private JTextArea welcomeTextArea = new JTextArea(welcomeText);
	private JTextField errorTextField = new JTextField();
	private TraderStorage loginStorage = new TraderStorage();
	private boolean firstTime = true;
	private KeyAdapter enterPressed = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == 10) // 10 = "Enter"
				loginButton.doClick();
		}
	};
	private JPanel selectionPanel = new JPanel();
	private JButton btnLogout = new JButton("Logout & Exit");
	private JTextArea marketTA = new JTextArea();
	private JButton btnMarket = new JButton("Market");
	private JButton btnPortfolio = new JButton("Portfolio");
	private JTextArea portfolioTA = new JTextArea();
	private KeyListener tradingKeyListener = new KeyListener() {		
		@Override
		public void keyTyped(KeyEvent e) {			
		}
		@Override
		public void keyReleased(KeyEvent e) {			
		}	
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				tradingPanel.setVisible(false);
				selectionState();				
			}
		}
	};
	private TradingApp tradingPanel = new TradingApp(SECRET, ACCOUNT, b, e, tradingKeyListener);
	private PortfolioApp portfolioPanel = new PortfolioApp(SECRET, ACCOUNT, b, e, tradingKeyListener);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StockExchangeApp window = new StockExchangeApp();
					window.frmHst.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 * @throws DoesNotHaveThisAssetException
	 * @throws InternalServerErrorException
	 * @throws WrongSecretException
	 * @throws NotBoundException
	 */
	public StockExchangeApp() throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException,
			DoesNotHaveThisAssetException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frmHst = new JFrame();
		loginState();
	}

	private void loginPanelOptions() {
		loginPanel.setBackground(Color.white);
		loginPanel.setLayout(new FlowLayout());
		loginPanel.setPreferredSize(new Dimension((int) (LOGINX_SIZE * 0.5), LOGINY_SIZE));
		logoLabelOptions();
		loginPanel.add(logoLabel);
		textFieldOptions();
		loginPanel.add(userTF);
		loginPanel.add(passwordTF);
		loginRegButtonsOptions();
		loginPanel.add(loginButton);
		loginPanel.add(registerButton);
		loginPanel.add(miniLogoLabel);
	}

	private void logoLabelOptions() {
		logoLabel.setPreferredSize(new Dimension((int) (LOGINX_SIZE * 0.45), (int) (LOGINY_SIZE * 0.3)));
		logoLabel.setVisible(true);
		miniLogoLabel.setPreferredSize(new Dimension((int) (LOGINX_SIZE * 0.1), (int) (LOGINY_SIZE * 0.18)));
		miniLogoLabel.setToolTipText("© - Tzah Mazuz");
		miniLogoLabel.setVisible(true);
	}

	private void textFieldOptions() {
		userTF.setBackground(MYBLUE);
		userTF.setEditable(true);
		userTF.setForeground(Color.white);
		userTF.setPreferredSize(new Dimension(220, 30));
		userTF.setHorizontalAlignment(JTextField.CENTER);
		userTF.setFont(new Font("Serif", Font.HANGING_BASELINE, 20));
		userTF.setBorder(null);
		userTF.setVisible(true);
		userTF.addKeyListener(enterPressed);
		userTF.setFocusable(true);
		passwordTF.setEditable(true);
		passwordTF.setBackground(MYBLUE);
		passwordTF.setForeground(Color.white);
		passwordTF.setPreferredSize(new Dimension(220, 30));
		passwordTF.setFont(new Font("Serif", Font.HANGING_BASELINE, 12));
		passwordTF.setBorder(null);
		passwordTF.setHorizontalAlignment(JTextField.CENTER);
		passwordTF.setVisible(true);
		passwordTF.addKeyListener(enterPressed);
		passwordTF.setFocusable(true);
	}

	private void loginRegButtonsOptions() {
		loginButton.setPreferredSize(new Dimension(65, 25));
		loginButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (loginStorage.checkUser(userTF.getText(), passwordTF.getText())) {
					selectionState();
				} else {
					errorTextField.setForeground(Color.red);
					errorTextField.setText("Username/Password is wrong");
				}
			}
		});
		registerButton.setPreferredSize(new Dimension(85, 25));
		registerButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean registerSuccess = false;
				try {
					registerSuccess = loginStorage.addUser(new TraderDetails(userTF.getText(), passwordTF.getText()));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				if (!registerSuccess) {
					errorTextField.setForeground(Color.red);
					errorTextField.setText("Username is taken");
				} else {
					errorTextField.setForeground(Color.green);
					errorTextField.setText("Registeration Successful");
				}
			}
		});
	}

	private void loginWelcomePanelOptions() {
		loginWelcomePanel.setBackground(Color.white);
		loginWelcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		loginWelcomePanel.setPreferredSize(new Dimension((int) (LOGINX_SIZE * 0.5), LOGINY_SIZE));
		welcomeTextAreaOptions();
		loginWelcomePanel.add(welcomeTextArea);
		errorTextFieldOptions();
		loginWelcomePanel.add(errorTextField);
	}

	private void welcomeTextAreaOptions() {
		welcomeTextArea.setEditable(false);
		welcomeTextArea.setBackground(Color.white);
		welcomeTextArea.setForeground(MYBLUE);
		welcomeTextArea.setPreferredSize(new Dimension(220, 175));
		welcomeTextArea.setFont(new Font("Serif", Font.ITALIC, 16));
		welcomeTextArea.setLineWrap(true);
		welcomeTextArea.setWrapStyleWord(true);
		welcomeTextArea.setVisible(true);
	}

	private void errorTextFieldOptions() {
		errorTextField.setEditable(false);
		errorTextField.setBackground(Color.white);
		errorTextField.setForeground(Color.red);
		errorTextField.setPreferredSize(new Dimension((int) (LOGINX_SIZE * 0.45), (int) (LOGINY_SIZE * 0.1)));
		errorTextField.setFont(new Font("Serif", Font.ITALIC, 12));
		errorTextField.setBorder(null);
		errorTextField.setHorizontalAlignment(JTextField.CENTER);
		errorTextField.setVisible(true);
	}

	private void loginState() {
		frmHst.getContentPane().setBackground(MYBLUE);
		frmHst.setAlwaysOnTop(true);
		frmHst.setIconImage(Toolkit.getDefaultToolkit().getImage(StockExchangeApp.class.getResource("/HST-icon.jpg")));
		frmHst.setTitle("HST Technologies");
		frmHst.setResizable(false);
		frmHst.getContentPane().setLayout(new CardLayout(0, 0));
		frmHst.setBounds(0, 0, 525, 260);
		frmHst.setLocationRelativeTo(null);
		frmHst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmHst.setUndecorated(false);
		frmHst.setVisible(true);
		loginWindow.setBackground(Color.WHITE);
		loginWindow.addKeyListener(enterPressed);
		loginWindow.setFocusable(true);
		loginPanelOptions();
		loginWindow.add(loginPanel, BorderLayout.WEST);
		loginWelcomePanelOptions();
		loginWindow.add(loginWelcomePanel, BorderLayout.EAST);
		frmHst.getContentPane().add(loginWindow, "name_105299747108477");
	}

	private void selectionState() {
		if(firstTime){
		frmHst.getContentPane().removeAll();
		frmHst.dispose();
		frmHst.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmHst.setUndecorated(true);
		frmHst.setVisible(true);
		frmHst.setLayout(new BorderLayout());
		selectionPanelOptions();
		frmHst.getContentPane().add(selectionPanel, BorderLayout.CENTER);
		firstTime = false;
		}else{
			frmHst.getContentPane().removeAll();
			frmHst.getContentPane().add(selectionPanel, BorderLayout.CENTER);
			selectionPanel.setVisible(true);
		}
	}

	private void selectionPanelOptions() {
		selectionPanel.setLayout(null);
		selectionPanel.setOpaque(false);
		selectionPanel.setVisible(true);
		selectionPanel.setPreferredSize(new Dimension(0, 0));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnLogout.setFont(new Font("Serif", Font.ITALIC, 20));
		btnLogout.setBounds(width - 255, height - 100 * 2, 200, 100);
		btnMarket.setBounds(width / 2 - 130 * 3, height / 2, 130, 108);
		btnMarket.setFont(new Font("Serif", Font.ITALIC, 20));
		btnMarket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				marketState();
			}

		});
		btnPortfolio.setBounds(width / 2 - 130 * 3, height / 2 + 130, 130, 108);
		btnPortfolio.setFont(new Font("Serif", Font.ITALIC, 20));
		btnPortfolio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				portfolioState();
			}
		});
		selectionPanel.add(btnLogout);
		selectionPanel.add(btnMarket);
		selectionPanel.add(portfolioTA);
		textAreaOptions();
		selectionPanel.add(marketTA);
		selectionPanel.add(btnPortfolio);

	}

	private void textAreaOptions() {
		marketTA.setText("Press here to go into \"Stocks Exchange\""
				+ "\nBid for stocks currently for sale and Ask for stocks you own" + " with ease");
		marketTA.setBackground(MYBLUE);
		marketTA.setEditable(false);
		marketTA.setForeground(Color.white);
		marketTA.setLineWrap(true);
		marketTA.setWrapStyleWord(true);
		marketTA.setFont(new Font("Serif", Font.ITALIC, 16));
		marketTA.setBounds(width / 2 - 312 / 2, height / 2 + 10, 312, 108);
		portfolioTA.setText("Press here to go into your \"Portfolio\""
				+ "\nMange stocks, monitor overall and specific profit / loss" + " for a selected period of time");
		portfolioTA.setBackground(MYBLUE);
		portfolioTA.setEditable(false);
		portfolioTA.setOpaque(false);
		portfolioTA.setForeground(Color.white);
		portfolioTA.setLineWrap(true);
		portfolioTA.setWrapStyleWord(true);
		portfolioTA.setFont(new Font("Serif", Font.ITALIC, 16));
		portfolioTA.setBounds(width / 2 - 312 / 2, height / 2 + 140, 312, 108);
	}

	private void marketState() {
		selectionPanel.setVisible(false);
		tradingPanel.addKeyListener(tradingKeyListener);
		frmHst.add(tradingPanel);
		frmHst.addKeyListener(tradingKeyListener);
		frmHst.setFocusable(true);
		frmHst.requestFocusInWindow();
		frmHst.getContentPane().setFocusable(true);
		tradingPanel.setVisible(true);
	}

	private void portfolioState() {
		selectionPanel.setVisible(false);
		frmHst.add(portfolioPanel);
		portfolioPanel.setVisible(true);
	}

}

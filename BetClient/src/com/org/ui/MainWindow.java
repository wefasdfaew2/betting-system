package com.org.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JDesktopPane;
import java.awt.Color;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JList;
import javax.swing.UIManager;

import java.awt.Choice;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JPasswordField;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow {

	private JFrame frmBettingMachine;
	private JTextField txtUsername;
	private JTextField textField_2;
	private JTextField txtMinBet;
	private JTextField txtMaxBet;
	private JTextField txtConstantBet;
	private JPasswordField txtPassword;
	private JTextField txtTimeout;
	private ButtonGroup group;
	private JRadioButton rdbtnRandomBet;
	private JRadioButton rdbtnConstantBet;
	private Choice choice;
	private JButton btnNewButton;
	private JLabel lblPassword;
	private JLabel lblNewLabel;
	private JLabel lblDomain;
	private JButton btnSave;
	private JButton btnSave_1;
	private JLabel lblNewLabel_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmBettingMachine.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	private String validation() {
		String reuslt = "OK";
		if (txtUsername.getText().equals("")) {
			return "user name is null";
		}
		if (txtPassword.getPassword().length == 0) {
			return "password is null";
		}
		try {
			Long.parseLong(txtTimeout.getText());
		} catch (NumberFormatException e) {
			return "time out value is invalid";
		}
		try {
			if (rdbtnRandomBet.isSelected()) {
				Float.parseFloat(txtMaxBet.getText());
				Float.parseFloat(txtMinBet.getText());
			}
			if (rdbtnConstantBet.isSelected())
				Float.parseFloat(txtConstantBet.getText());
		} catch (NumberFormatException e) {
			return "bet value is invalid";
		}
		return reuslt;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		frmBettingMachine = new JFrame();
		frmBettingMachine.setTitle("Betting Machine");
		frmBettingMachine.setResizable(false);
		frmBettingMachine.setBounds(100, 100, 741, 550);
		frmBettingMachine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBettingMachine.getContentPane()
				.setLayout(new GridLayout(0, 1, 0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmBettingMachine.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("New account", null, panel, null);
		panel.setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		desktopPane.setBounds(10, 11, 284, 138);
		panel.add(desktopPane);

		txtUsername = new JTextField();
		txtUsername.setBounds(75, 8, 168, 20);
		desktopPane.add(txtUsername);
		txtUsername.setColumns(10);

		btnNewButton = new JButton("Login");
		btnNewButton
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("fuck");
			}
		});
		btnNewButton.setBounds(9, 105, 83, 23);
		desktopPane.add(btnNewButton);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(9, 42, 46, 14);
		desktopPane.add(lblPassword);

		lblNewLabel = new JLabel("User");
		lblNewLabel.setBounds(10, 11, 46, 14);
		desktopPane.add(lblNewLabel);

		choice = new Choice();
		choice.setBounds(75, 65, 86, 20);
		choice.add("Sbo");
		choice.add("3in");
		desktopPane.add(choice);

		lblDomain = new JLabel("Domain");
		lblDomain.setBounds(9, 67, 46, 14);
		desktopPane.add(lblDomain);

		btnSave = new JButton("Reset");
		btnSave.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose.gif")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText("");
				txtPassword.setText("");
				txtTimeout.setText("3000");
			}
		});
		btnSave.setBounds(108, 105, 73, 23);
		desktopPane.add(btnSave);

		btnSave_1 = new JButton("Save");
		btnSave_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String validaString = validation();
					if (!validaString.equals("OK"))
						JOptionPane.showMessageDialog(frmBettingMachine,
								validaString, "Critical error",
								JOptionPane.ERROR_MESSAGE);
					else
						this.saveConfiguration();
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private void saveConfiguration() throws ConfigurationException {
				UIProperty u_property = new UIProperty("conf.xml");
				XMLConfiguration config = u_property.getConfig();
				config.addProperty("player", "");
				config.addProperty("player.username", txtUsername.getText());
				StringBuffer bf = new StringBuffer();
				bf.append(txtPassword.getPassword());
				config.addProperty("player.password", bf.toString());
				config.addProperty("player.domain", choice.getSelectedItem());
				config.setProperty("player.timeout", txtTimeout.getText());
				if (rdbtnConstantBet.isSelected()) {
					config.setProperty("player.betvalue",
							txtConstantBet.getText());
				} else if (rdbtnRandomBet.isSelected()) {
					String[] bet = { txtMinBet.getText(), txtMaxBet.getText() };
					config.setProperty("player.betvalue", bet);
				}

				config.save();
			}
		});
		btnSave_1
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		btnSave_1.setBounds(191, 105, 83, 23);
		desktopPane.add(btnSave_1);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(75, 39, 168, 20);
		desktopPane.add(txtPassword);

		lblNewLabel_4 = new JLabel("Timeout(s)");
		lblNewLabel_4.setBounds(167, 65, 76, 14);
		desktopPane.add(lblNewLabel_4);

		txtTimeout = new JTextField();
		txtTimeout.setText("3000");
		txtTimeout.setBounds(223, 65, 51, 20);
		desktopPane.add(txtTimeout);
		txtTimeout.setColumns(10);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setForeground(Color.LIGHT_GRAY);
		panel_1.setBounds(304, 11, 146, 250);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblAccountInfo = new JLabel("Account Info");
		lblAccountInfo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAccountInfo.setBounds(10, 11, 126, 14);
		panel_1.add(lblAccountInfo);

		JLabel lblNewLabel_1 = new JLabel("Locked");
		lblNewLabel_1.setBounds(10, 35, 46, 14);
		panel_1.add(lblNewLabel_1);

		JLabel lblSuspended = new JLabel("Suspended");
		lblSuspended.setBounds(10, 60, 62, 14);
		panel_1.add(lblSuspended);

		JLabel lblNewLabel_2 = new JLabel("False");
		lblNewLabel_2.setBounds(92, 35, 69, 14);
		panel_1.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("False");
		lblNewLabel_3.setBounds(92, 60, 69, 14);
		panel_1.add(lblNewLabel_3);

		JLabel lblMaxBet = new JLabel("Max Bet");
		lblMaxBet.setBounds(10, 85, 46, 14);
		panel_1.add(lblMaxBet);

		JLabel lblMinBet = new JLabel("Min Bet");
		lblMinBet.setBounds(10, 110, 46, 14);
		panel_1.add(lblMinBet);

		JLabel label = new JLabel("50");
		label.setBounds(92, 85, 69, 14);
		panel_1.add(label);

		JLabel label_1 = new JLabel("10");
		label_1.setBounds(92, 110, 69, 14);
		panel_1.add(label_1);

		JLabel lblRealMoney = new JLabel("Real Money");
		lblRealMoney.setBounds(10, 174, 62, 14);
		panel_1.add(lblRealMoney);

		JLabel label_2 = new JLabel("50$");
		label_2.setBounds(92, 174, 46, 14);
		panel_1.add(label_2);

		JLabel lblCredit = new JLabel("Credit");
		lblCredit.setBounds(10, 211, 46, 14);
		panel_1.add(lblCredit);

		JLabel label_3 = new JLabel("100");
		label_3.setBounds(90, 211, 46, 14);
		panel_1.add(label_3);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.LIGHT_GRAY);
		panel_2.setBounds(10, 160, 284, 101);
		panel.add(panel_2);
		panel_2.setLayout(null);

		textField_2 = new JTextField();
		textField_2.setBounds(79, 11, 184, 20);
		panel_2.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblBetServer = new JLabel("Bet Server");
		lblBetServer.setBounds(10, 14, 52, 14);
		panel_2.add(lblBetServer);

		JButton btnConnect = new JButton("Connect");
		btnConnect
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/expanded.gif")));
		btnConnect.setBounds(10, 67, 114, 23);
		panel_2.add(btnConnect);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(10, 42, 46, 14);
		panel_2.add(lblStatus);

		JLabel lblNotConnected = new JLabel("Not connected");
		lblNotConnected.setBounds(79, 42, 97, 14);
		panel_2.add(lblNotConnected);

		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/com/sun/java/swing/plaf/motif/icons/Error.gif")));
		btnDisconnect.setBounds(152, 67, 122, 23);
		panel_2.add(btnDisconnect);

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBettingMachine.dispose();
			}
		});
		btnLogout.setIcon(new ImageIcon(MainWindow.class
				.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		btnLogout.setBounds(605, 421, 115, 41);
		panel.add(btnLogout);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.LIGHT_GRAY);
		panel_3.setBounds(460, 11, 241, 250);
		panel.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblBetList = new JLabel("Bet List");
		lblBetList.setBounds(47, 5, 62, 20);
		lblBetList.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_3.add(lblBetList);

		JTextArea txtrHdp = new JTextArea();
		txtrHdp.setText("HDP190601105   11/17 15:01:28\r\nHandicapSoccer\r\nThailand U23 -vs- Singapore U23\r\nSEA GAMES INDONESIA 2011 - MEN SOCCER\r\nBet:3.00Thailand U23\r\nrunning-0.5 @0.720");
		txtrHdp.setBounds(10, 31, 221, 196);
		panel_3.add(txtrHdp);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(Color.LIGHT_GRAY);
		panel_8.setBounds(10, 272, 440, 68);
		panel.add(panel_8);
		panel_8.setLayout(null);

		txtMinBet = new JTextField();
		txtMinBet.setText("10");
		txtMinBet.setBounds(192, 10, 86, 20);
		panel_8.add(txtMinBet);
		txtMinBet.setColumns(10);

		txtMaxBet = new JTextField();
		txtMaxBet.setText("50");
		txtMaxBet.setBounds(332, 10, 86, 20);
		panel_8.add(txtMaxBet);
		txtMaxBet.setColumns(10);

		JLabel lblMin = new JLabel("Min");
		lblMin.setBounds(160, 13, 46, 14);
		panel_8.add(lblMin);

		JLabel lblMax = new JLabel("Max");
		lblMax.setBounds(289, 13, 46, 14);
		panel_8.add(lblMax);

		rdbtnRandomBet = new JRadioButton("Random bet");
		rdbtnRandomBet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rdbtnRandomBet.isSelected()) {
					txtMinBet.setEnabled(true);
					txtMaxBet.setEnabled(true);
				} else {
					txtMinBet.setEnabled(false);
					txtMaxBet.setEnabled(false);
				}
			}
		});
		rdbtnRandomBet.setSelected(true);
		rdbtnRandomBet.setBounds(16, 7, 109, 23);
		panel_8.add(rdbtnRandomBet);

		rdbtnConstantBet = new JRadioButton("Constant bet");
		rdbtnConstantBet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (rdbtnConstantBet.isSelected()) {
					txtConstantBet.setEnabled(true);
				} else {
					txtConstantBet.setEnabled(false);
				}
			}
		});
		rdbtnConstantBet.setBounds(16, 37, 109, 23);
		panel_8.add(rdbtnConstantBet);

		group = new ButtonGroup();
		group.add(rdbtnRandomBet);
		group.add(rdbtnConstantBet);

		txtConstantBet = new JTextField();
		txtConstantBet.setEnabled(false);
		txtConstantBet.setBounds(192, 38, 226, 20);
		panel_8.add(txtConstantBet);
		txtConstantBet.setColumns(10);

		JLabel lblValue = new JLabel("Value");
		lblValue.setBounds(160, 41, 46, 14);
		panel_8.add(lblValue);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Account Manager", null, panel_4, null);
		panel_4.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.LIGHT_GRAY);
		panel_5.setBounds(23, 11, 215, 263);
		panel_4.add(panel_5);
		panel_5.setLayout(null);

		JLabel lblSbobet = new JLabel("Sbobet");
		lblSbobet.setBounds(63, 5, 57, 20);
		lblSbobet.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_5.add(lblSbobet);

		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 38, 46, 14);
		panel_5.add(lblName);

		JLabel lblStatus_1 = new JLabel("Status");
		lblStatus_1.setBounds(101, 38, 46, 14);
		panel_5.add(lblStatus_1);

		JLabel lblMaj = new JLabel("maj3259001");
		lblMaj.setBounds(10, 76, 90, 14);
		panel_5.add(lblMaj);

		JLabel lblOk = new JLabel("OK");
		lblOk.setBounds(101, 76, 46, 14);
		panel_5.add(lblOk);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose-pressed.gif")));
		btnNewButton_1.setBounds(169, 67, 33, 23);
		panel_5.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		btnNewButton_2.setBounds(136, 67, 33, 23);
		panel_5.add(btnNewButton_2);

		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.LIGHT_GRAY);
		panel_6.setBounds(248, 11, 226, 263);
		panel_4.add(panel_6);
		panel_6.setLayout(null);

		JLabel lblinbet = new JLabel("3inBet");
		lblinbet.setBounds(65, 5, 53, 20);
		lblinbet.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_6.add(lblinbet);

		JLabel label_4 = new JLabel("Status");
		label_4.setBounds(101, 36, 46, 14);
		panel_6.add(label_4);

		JLabel label_5 = new JLabel("OK");
		label_5.setBounds(101, 74, 46, 14);
		panel_6.add(label_5);

		JButton button = new JButton("");
		button.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose-pressed.gif")));
		button.setBounds(183, 65, 33, 23);
		panel_6.add(button);

		JLabel label_6 = new JLabel("maj3259001");
		label_6.setBounds(10, 74, 90, 14);
		panel_6.add(label_6);

		JLabel label_7 = new JLabel("Name");
		label_7.setBounds(10, 36, 46, 14);
		panel_6.add(label_7);

		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		button_2.setBounds(150, 65, 33, 23);
		panel_6.add(button_2);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.LIGHT_GRAY);
		panel_7.setBounds(483, 11, 228, 263);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblIbet = new JLabel("Ibet");
		lblIbet.setBounds(75, 5, 34, 20);
		lblIbet.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_7.add(lblIbet);

		JLabel label_8 = new JLabel("Status");
		label_8.setBounds(101, 36, 46, 14);
		panel_7.add(label_8);

		JLabel label_9 = new JLabel("OK");
		label_9.setBounds(101, 74, 46, 14);
		panel_7.add(label_9);

		JButton button_1 = new JButton("");
		button_1.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose-pressed.gif")));
		button_1.setBounds(185, 64, 33, 24);
		panel_7.add(button_1);

		JLabel label_10 = new JLabel("maj3259001");
		label_10.setBounds(10, 74, 90, 14);
		panel_7.add(label_10);

		JLabel label_11 = new JLabel("Name");
		label_11.setBounds(10, 36, 46, 14);
		panel_7.add(label_11);

		JButton button_3 = new JButton("");
		button_3.setIcon(new ImageIcon(
				MainWindow.class
						.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		button_3.setBounds(154, 64, 33, 24);
		panel_7.add(button_3);

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(622, 300, 89, 23);
		panel_4.add(btnExit);

		JMenuBar menuBar = new JMenuBar();
		frmBettingMachine.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNewAccount = new JMenuItem("New Account");
		mntmNewAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel panel = new JPanel();
				tabbedPane.addTab("New account", null, panel, null);
				panel.setLayout(null);
			}
		});
		mnFile.add(mntmNewAccount);

		JMenuItem menuItem = new JMenuItem("---------------");
		mnFile.add(menuItem);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("fuck");
			}
		});
		mnFile.add(mntmExit);
	}
}

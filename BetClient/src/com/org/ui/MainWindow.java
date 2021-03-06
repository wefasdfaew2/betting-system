package com.org.ui;

import java.awt.EventQueue;

import javax.jms.JMSException;
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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JPasswordField;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.org.betmanager.TailerReader;
import com.org.odd.Odd;
import com.org.odd.OddElement;
import com.org.odd.OddSide;
import com.org.player.IPlayer;
import com.org.player.SbobetPlayer;
import com.org.player.StackTraceUtil;
import com.org.player.ThreeInOnePlayer;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class MainWindow {

	private JFrame frmBettingMachine;
	private JTextField txtUsername;
	private JTextField txtBetServer;
	private JTextField txtMinBet;
	private JTextField txtMaxBet;
	private JTextField txtConstantBet;
	private JPasswordField txtPassword;
	private JTextField txtTimeout;
	private ButtonGroup group;
	private JRadioButton rdbtnRandomBet;
	private JRadioButton rdbtnConstantBet;
	private Choice choiceDomain;
	private JButton btnNewButton;
	private JLabel lblPassword;
	private JLabel lblNewLabel;
	private JLabel lblDomain;
	private JButton btnSave;
	private JButton btnSave_1;
	private JLabel lblNewLabel_4;
	private JTextArea txtConsole;
	private Choice choiceSide;
	private JTable table;
	private DefaultTableModel table_model;
	private JButton btnConnect;
	private IPlayer player;
	private JCheckBox chckbxAutoConnect;

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

	class LogFileTailer extends TailerListenerAdapter {
		public void handle(String line) {
			txtConsole.append("\n" + line);
		}
	}

	private void initLogTailer() {
		LogFileTailer listener = new LogFileTailer();
		Tailer tailer = new Tailer(new File(".\\log\\player.log"), listener,
				500, true);
		(new Thread(tailer)).start();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
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
		frmBettingMachine.setBounds(100, 100, 926, 720);
		frmBettingMachine.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBettingMachine.getContentPane()
				.setLayout(new GridLayout(0, 1, 0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmBettingMachine.getContentPane().add(tabbedPane);

		group = new ButtonGroup();

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New account", null, panel_2, null);
		panel_2.setLayout(null);

		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		desktopPane.setBounds(10, 11, 284, 250);
		panel_2.add(desktopPane);

		txtUsername = new JTextField();
		txtUsername.setBounds(75, 8, 199, 20);
		desktopPane.add(txtUsername);
		txtUsername.setColumns(10);

		btnNewButton = new JButton("Login");
		btnNewButton
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String validaString = validation();
				if (!validaString.equals("OK"))
					JOptionPane.showMessageDialog(frmBettingMachine,
							validaString, "Critical error",
							JOptionPane.ERROR_MESSAGE);
				else {
					if (choiceDomain.getSelectedItem().equals("3in")) {
						StringBuffer bf = new StringBuffer();
						bf.append(txtPassword.getPassword());
						OddSide side = OddSide.LIVE;
						if (choiceSide.getSelectedItem().equals("NON-LIVE")) {
							side = OddSide.NON_LIVE;
						} else if (choiceSide.getSelectedItem().equals("TODAY")) {
							side = OddSide.TODAY;
						}
						try {
							player = new ThreeInOnePlayer(
									txtUsername.getText(), bf.toString(), side);
							player.homePage();
							HashMap<String, OddElement> map_odds = player
									.getCurrent_map_odds();
							for (OddElement o : map_odds.values()) {
								Odd odd = o.getOdd();
								table_model.addRow(new Object[] {
										odd.getHome(), odd.getAway(),
										odd.getHandicap(), odd.getType(),
										odd.getOdd_home(), odd.getOdd_away() });
							}
							if (chckbxAutoConnect.isSelected()) {
								player.startConnection();
								btnConnect.setText("Disconect");
							}
							btnConnect.setEnabled(true);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					if (choiceDomain.getSelectedItem().equals("Sbo")) {
						StringBuffer bf = new StringBuffer();
						bf.append(txtPassword.getPassword());
						OddSide side = OddSide.LIVE;
						if (choiceSide.getSelectedItem().equals("NON-LIVE")) {
							side = OddSide.NON_LIVE;
						} else if (choiceSide.getSelectedItem().equals("TODAY")) {
							side = OddSide.TODAY;
						}
						try {
							player = new SbobetPlayer(txtUsername.getText(), bf
									.toString(), side, false);
							player.homePage();
							HashMap<String, OddElement> map_odds = player
									.getCurrent_map_odds();
							for (OddElement o : map_odds.values()) {
								Odd odd = o.getOdd();
								table_model.addRow(new Object[] {
										odd.getHome(), odd.getAway(),
										odd.getHandicap(), odd.getType(),
										odd.getOdd_home(), odd.getOdd_away() });
							}
							if (chckbxAutoConnect.isSelected()) {
								player.startConnection();
								btnConnect.setText("Disconect");
							}
							btnConnect.setEnabled(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		});
		btnNewButton.setBounds(9, 216, 83, 23);
		desktopPane.add(btnNewButton);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(9, 42, 46, 14);
		desktopPane.add(lblPassword);

		lblNewLabel = new JLabel("User");
		lblNewLabel.setBounds(10, 11, 46, 14);
		desktopPane.add(lblNewLabel);

		choiceDomain = new Choice();
		choiceDomain.setBounds(75, 65, 86, 20);
		choiceDomain.add("Sbo");
		choiceDomain.add("3in");
		desktopPane.add(choiceDomain);

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
		btnSave.setBounds(108, 216, 73, 23);
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
				config.addProperty("player.domain",
						choiceDomain.getSelectedItem());
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
		btnSave_1.setBounds(191, 216, 83, 23);
		desktopPane.add(btnSave_1);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(75, 39, 199, 20);
		desktopPane.add(txtPassword);

		lblNewLabel_4 = new JLabel("Timeout(s)");
		lblNewLabel_4.setBounds(167, 65, 76, 14);
		desktopPane.add(lblNewLabel_4);

		txtTimeout = new JTextField();
		txtTimeout.setText("3000");
		txtTimeout.setBounds(223, 65, 51, 20);
		desktopPane.add(txtTimeout);
		txtTimeout.setColumns(10);

		JLabel lblSide = new JLabel("Side");
		lblSide.setBounds(9, 92, 46, 14);
		desktopPane.add(lblSide);

		choiceSide = new Choice();
		choiceSide.setBounds(75, 92, 86, 20);
		choiceSide.add("LIVE");
		choiceSide.add("NON-LIVE");
		choiceSide.add("TODAY");
		desktopPane.add(choiceSide);

		JLabel lblBetServer = new JLabel("Bet Server");
		lblBetServer.setBounds(9, 123, 52, 14);
		desktopPane.add(lblBetServer);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(75, 121, 199, 23);
		desktopPane.add(scrollPane_2);

		txtBetServer = new JTextField();
		scrollPane_2.setViewportView(txtBetServer);
		txtBetServer
				.setText("tcp://localhost:61616?jms.useAsyncSend=true&wireFormat.maxInactivityDuration=0");
		txtBetServer.setColumns(10);

		chckbxAutoConnect = new JCheckBox("Auto connect");
		chckbxAutoConnect.setSelected(true);
		chckbxAutoConnect.setBounds(75, 155, 97, 23);
		desktopPane.add(chckbxAutoConnect);

		btnConnect = new JButton("Connect");
		btnConnect.setEnabled(false);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					player.startConnection();
					btnConnect.setText("Disconect");
				} catch (JMSException e1) {
					// TODO Auto-generated catch block
					player.getLogger().error(StackTraceUtil.getStackTrace(e1));
				}
			}
		});
		btnConnect.setBounds(184, 155, 97, 23);
		desktopPane.add(btnConnect);
		btnConnect
				.setIcon(new ImageIcon(
						MainWindow.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/expanded.gif")));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setForeground(Color.LIGHT_GRAY);
		panel_1.setBounds(304, 11, 146, 250);
		panel_2.add(panel_1);
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

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBettingMachine.dispose();
			}
		});
		btnLogout.setIcon(new ImageIcon(MainWindow.class
				.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		btnLogout.setBounds(773, 598, 107, 34);
		panel_2.add(btnLogout);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.LIGHT_GRAY);
		panel_3.setBounds(460, 11, 440, 160);
		panel_2.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblBetList = new JLabel("Bet List");
		lblBetList.setBounds(47, 5, 62, 20);
		lblBetList.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_3.add(lblBetList);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 31, 420, 115);
		panel_3.add(scrollPane_1);

		JTextArea txtrHdp = new JTextArea();
		scrollPane_1.setViewportView(txtrHdp);
		txtrHdp.setEditable(false);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(Color.LIGHT_GRAY);
		panel_8.setBounds(460, 182, 440, 79);
		panel_2.add(panel_8);
		panel_8.setLayout(null);

		txtMinBet = new JTextField();
		txtMinBet.setText("10");
		txtMinBet.setBounds(192, 10, 86, 20);
		panel_8.add(txtMinBet);
		txtMinBet.setColumns(10);

		txtMaxBet = new JTextField();
		txtMaxBet.setText("50");
		txtMaxBet.setBounds(332, 10, 98, 20);
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
		group.add(rdbtnRandomBet);
		group.add(rdbtnConstantBet);

		txtConstantBet = new JTextField();
		txtConstantBet.setEnabled(false);
		txtConstantBet.setBounds(192, 38, 238, 20);
		panel_8.add(txtConstantBet);
		txtConstantBet.setColumns(10);

		JLabel lblValue = new JLabel("Value");
		lblValue.setBounds(160, 41, 46, 14);
		panel_8.add(lblValue);

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.LIGHT_GRAY);
		panel_9.setBounds(10, 435, 890, 152);
		panel_2.add(panel_9);
		panel_9.setLayout(null);

		JLabel lblConsole = new JLabel("Console");
		lblConsole.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblConsole.setBounds(10, 11, 82, 14);
		panel_9.add(lblConsole);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 870, 105);
		panel_9.add(scrollPane);

		txtConsole = new JTextArea();
		scrollPane.setViewportView(txtConsole);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.LIGHT_GRAY);
		panel_10.setBounds(10, 271, 890, 153);
		panel_2.add(panel_10);
		panel_10.setLayout(null);

		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 11, 870, 131);
		panel_10.add(scrollPane_3);

		table = new JTable();
		table.setRowSelectionAllowed(false);
		scrollPane_3.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table_model = new DefaultTableModel(new Object[][] {}, new String[] {
				"home", "away", "handicap", "type", "odd home", "odd away" });
		table.setModel(table_model);
		table.getColumn("odd home").setCellRenderer(new ButtonRenderer());
		table.getColumn("odd away").setCellRenderer(new ButtonRenderer());
		table.getColumn("odd home").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		table.getColumn("odd away").setCellEditor(
				new ButtonEditor(new JCheckBox()));

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
		initLogTailer();
	}
}

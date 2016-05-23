package GUI;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import bluetooth.BluetoothConnection;
import emotiv.EmoProfileManagement;
import emotiv.EmoRawData;

import javax.swing.JMenu;
import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JProgressBar;

public class RobotControlWindow extends JFrame {

	private BluetoothConnection BTConnection;
	private MainWindow mw;
	private RobotControlWindow rbc;
	private JTextPane commandTextPane;
	private EmoRawData ERD;
	private long lastCommandTime;
	private long currentCommandTime;
	private JComboBox usersComboBox;
	private JProgressBar progressBar;
	private Thread prog;

	/**
	 * Create the application.
	 */
	public RobotControlWindow(MainWindow mw) {

		this.mw = mw;
		rbc = this;

		BTConnection = new BluetoothConnection();
		initialize();
		lastCommandTime = System.currentTimeMillis();
		prog = new Thread(new Runnable() {
			public void run() {
				Random r = new Random(System.currentTimeMillis());
				while (true) {
					for (int i = 0; i < 500000000; i++) {
						
					}
					int value = r.nextInt(60);
					progressBar.setForeground(Color.gray);
					progressBar.setValue(value);
					
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		this.setTitle("Robot Control");
		this.setBounds(100, 100, 425, 481);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel centerPanel = new JPanel();
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(null);

		JPanel userPanel = new JPanel();
		userPanel.setBounds(32, 11, 354, 73);
		centerPanel.add(userPanel);
		userPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel userLable = new JLabel("Choose User:");
		userLable.setFont(new Font("Tahoma", Font.PLAIN, 20));
		userLable.setHorizontalAlignment(SwingConstants.CENTER);
		userPanel.add(userLable);

		usersComboBox = new JComboBox();
		usersComboBox.addItem("omri");
		userPanel.add(usersComboBox);

		JLabel lblChooseBluetooth = new JLabel("Choose Bluetooth:");
		userPanel.add(lblChooseBluetooth);
		lblChooseBluetooth.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChooseBluetooth.setHorizontalAlignment(SwingConstants.CENTER);

		final JComboBox COMportComboBox = new JComboBox();
		userPanel.add(COMportComboBox);

		String[] COMList = BTConnection.getCOMPorts();

		JPanel connectPanel = new JPanel();
		connectPanel.setBounds(32, 95, 354, 73);
		centerPanel.add(connectPanel);
		connectPanel.setLayout(new GridLayout(2, 2, 0, 0));

		JButton btnNewButton = new JButton("Connect");

		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		connectPanel.add(btnNewButton);

		JLabel errorLable = new JLabel("Offline");
		errorLable.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorLable.setForeground(Color.RED);
		errorLable.setHorizontalAlignment(SwingConstants.CENTER);
		connectPanel.add(errorLable);

		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				errorLable.setForeground(new Color(255, 0, 0));
				errorLable.setText("Disconnected");
				btnNewButton.setEnabled(true);
				BTConnection.closeConnection();
				prog.stop();
				ERD.stop();
			}
		});
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
		connectPanel.add(btnDisconnect);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BTConnection.setCOMPort(COMportComboBox.getSelectedIndex());
				if (BTConnection.connectToBluetooth().equals("Success")) {
					errorLable.setText("Connected");
					errorLable.setForeground(new Color(0, 255, 0));
					ERD = new EmoRawData(rbc);
					ERD.start();
					btnNewButton.setEnabled(false);

					prog.start();
					
				} else {
					errorLable.setForeground(new Color(255, 0, 0));
					errorLable.setText("Failed");
				}

			}
		});
		for (String s : COMList) {
			COMportComboBox.addItem(s);
		}

		commandTextPane = new JTextPane();
		commandTextPane.setBounds(32, 288, 354, 122);
		JScrollPane jsp = new JScrollPane(commandTextPane);
		jsp.setBounds(32, 267, 354, 143);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		centerPanel.add(jsp);

		JLabel lblCommand = new JLabel("Command:");
		lblCommand.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCommand.setBounds(32, 230, 109, 26);
		centerPanel.add(lblCommand);

		progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(105, 193, 281, 26);
		progressBar.setValue(0);
		centerPanel.add(progressBar);

		JLabel lblPower = new JLabel("Power:");
		lblPower.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPower.setBounds(32, 193, 89, 26);
		centerPanel.add(lblPower);

		JMenuBar menuBar = new JMenuBar();
		this.getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmMainWindow = new JMenuItem("Main Window");
		mnFile.add(mntmMainWindow);
		mntmMainWindow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					BTConnection.closeConnection();
				} catch (Exception ex) {};
				
				rbc.dispose();
				mw.setVisible(true);
			}
		});

		JMenuItem mntmSetting = new JMenuItem("Setting");
		mnFile.add(mntmSetting);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
	}

	public void sendBluetoothCommand(String command) {
		currentCommandTime = System.currentTimeMillis();
		if ((currentCommandTime - lastCommandTime) > 1500) {
			lastCommandTime = currentCommandTime;
			progressBar.setValue(100);
			progressBar.setForeground(Color.red);
			String message;
			switch (command) {
			case "f":
				message = "Brain Command: Forward";
				break;

			case "b":
				message = "Brain Command: Backward";
				break;

			case "l":
				message = "Brain Command: Left Hand";
				break;
			case "r":
				message = "Brain Command: Right Hand";
				break;
			default:
				message = "Failed sending a commaned";
				break;
			}

			try {
				BTConnection.sendCommand(command);
			} catch (Exception ex) {
				message = "Failed sending a commaned";
			}
			StyledDocument document = (StyledDocument) commandTextPane.getDocument();
			try {
				document.insertString(document.getLength(), message + "\n", null);
			} catch (BadLocationException e) {
				message = "Failed sending a commaned";
			}
		}
	}
}

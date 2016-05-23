package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import emotiv.EmoProfileManagement;
import emotiv.EmoTrainNewUser;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.Color;

public class TrainNewUserWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private TrainNewUserWindow trainNewUserRef = this;
	private JComboBox comboBox;
	private JLabel instroctionLabel;
	private JButton btnStartTraining;

	/**
	 * Create the frame.
	 * @param mainWindowRef 
	 */
	public TrainNewUserWindow(MainWindow mainWindowRef) {
		setTitle("Train New User");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 312, 303);
		
		EmoTrainNewUser TNU = new EmoTrainNewUser(this);
		TNU.start();
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmMainWindow = new JMenuItem("Main Window");
		mntmMainWindow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindowRef.setVisible(true);
				trainNewUserRef.dispose();
				
			}
		});
		mnFile.add(mntmMainWindow);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUserName.setBounds(10, 11, 114, 23);
		mainPanel.add(lblUserName);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setBounds(125, 14, 146, 20);
		mainPanel.add(textField);
		textField.setColumns(10);
		
		btnStartTraining = new JButton("Start Training");

		btnStartTraining.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnStartTraining.setBounds(10, 69, 261, 39);
		mainPanel.add(btnStartTraining);
		
		instroctionLabel = new JLabel("Clear Your Mind");
		instroctionLabel.setBackground(Color.WHITE);
		instroctionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instroctionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		instroctionLabel.setBounds(10, 106, 261, 79);
		mainPanel.add(instroctionLabel);
		
	    String [] options = {"Neutral","Forward","Backward","Left","Right"};
	    comboBox = new JComboBox(options);
		comboBox.setBounds(125, 38, 146, 20);
		mainPanel.add(comboBox);
		
		btnStartTraining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = comboBox.getSelectedIndex();
				textField.setEnabled(false);
				SetUIEnable(false);
				TNU.StartTraining(index);
			}
		});
		JLabel lblChooseAction = new JLabel("Choose Action:");
		lblChooseAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblChooseAction.setBounds(10, 41, 127, 14);
		mainPanel.add(lblChooseAction);
		
		JButton btnSave = new JButton("Save User Data");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmoProfileManagement.SaveCurrentProfile();
				EmoProfileManagement.SaveProfilesToFile();
				TNU.stop();
				trainNewUserRef.dispose();
				mainWindowRef.setVisible(true);
			}
		});
		btnSave.setBounds(10, 186, 261, 36);
		mainPanel.add(btnSave);
	}
	public void SetUIEnable(Boolean flag){
		btnStartTraining.setEnabled(flag);
		comboBox.setEnabled(flag);
		
	}
}
	
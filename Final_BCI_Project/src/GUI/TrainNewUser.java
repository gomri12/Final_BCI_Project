package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class TrainNewUser extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private TrainNewUser trainNewUserRef = this;

	/**
	 * Create the frame.
	 * @param mainWindowRef 
	 */
	public TrainNewUser(MainWindow mainWindowRef) {
		setTitle("Train New User");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 312, 303);
		
		
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
		textField.setBounds(118, 14, 153, 20);
		mainPanel.add(textField);
		textField.setColumns(10);
		
		JButton btnStartTraining = new JButton("Start Training");
		btnStartTraining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnStartTraining.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnStartTraining.setBounds(10, 56, 261, 39);
		mainPanel.add(btnStartTraining);
		
		JLabel lblNewLabel = new JLabel("Instructions");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel.setBounds(10, 106, 261, 79);
		mainPanel.add(lblNewLabel);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 196, 261, 28);
		mainPanel.add(progressBar);
	}
}

package GUI;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import emotiv.EmotivRawData;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private MainWindow MainWindowRef;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		MainWindowRef = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewUser = new JMenuItem("New User");
		mnFile.add(mntmNewUser);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Offline Data");
		mnFile.add(mntmNewMenuItem);
		
		JMenuItem mntmSetting = new JMenuItem("Setting");
		mnFile.add(mntmSetting);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 1, 5, 5));
		
		JButton btnTrain = new JButton("Train New User");
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TrainNewUser tnu = new TrainNewUser(MainWindowRef);
				tnu.setVisible(true);
				MainWindowRef.setVisible(false);
			}
		});
		panel.add(btnTrain);
		
		JButton btnControl = new JButton("Control Your Robot");
		btnControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				RobotControlWindow rcw = new RobotControlWindow(MainWindowRef);
				rcw.setVisible(true);
				MainWindowRef.setVisible(false);

			}
		});
		panel.add(btnControl);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(btnExit);
	}

}

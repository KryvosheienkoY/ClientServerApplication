package ui;

import client.HttpServiceClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LogingIn {
	// sizes of frame
	public final static int W_HEIGHT = 160;
	public final static int W_WIDTH = 350;
	private JFrame frame;
	//private UsersRegister reg;
	private LabaRegister labaR;
	private JTextField fieldL;
	private JPasswordField fieldP;
	private HttpServiceClient http;

	// constructor

	public LogingIn(LabaRegister labaR, HttpServiceClient http) {
//		this.reg = reg;
		this.http=http;
		this.labaR = labaR;
	}
	/**
	 * initialize all components
	 */
	public void logIn() {
		this.frame = new JFrame("LOG IN!");
		JPanel p = new JPanel();
		JLabel lLogin = new JLabel(" Login");
		JLabel lPassword = new JLabel(" Password");
		this.fieldL = new JTextField(25);
		this.fieldP = new JPasswordField(25);
		JButton button = new JButton("Log in");

		frame.setResizable(false);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.getContentPane().add(p, BorderLayout.NORTH);
		p.setLayout(new GridLayout(2, 2, 0, 20));
		p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		p.add(lLogin);
		p.add(fieldL);
		p.add(lPassword);
		p.add(fieldP);
		frame.setSize(W_WIDTH, W_HEIGHT);
		frame.setVisible(true);

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loggingUser();
			}
		});
	}

	private void loggingUser() {
		// send request on server
		try {
			System.out.println("log - " +fieldL.getText()+"  pas - " +String.valueOf(fieldP.getPassword()));
			http.sendGetLogin(fieldL.getText(), String.valueOf(fieldP.getPassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addErrorWindow() {
		JOptionPane.showMessageDialog(null, "Wrong input", "ERROR!", JOptionPane.ERROR_MESSAGE);
	}

	public void openMenu() {
		this.frame.dispose();
		labaR.openMenu();

	}
}

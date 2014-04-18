import javax.crypto.Cipher;
import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;


public class Login extends JFrame {
	private JLabel lblPin;
	private JLabel lblPleaseLoginTo;
	private JLabel lblCscVotingSystem;
	private JPanel panel_2;
	private JTextField username;
	private JPasswordField ssn1;
	private JPasswordField ppin;
	private JPasswordField ssn2;
	private JPasswordField ssn3;
	private JLabel label_1;
	private JButton btnLogin;
	
	private Connection connector;
	
	public Login() {
		initializeLogin();
	}
	
	private void initializeLogin(){
		setTitle("Voting System");
		setSize(450,300);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(81, 76, 67, 14);
		getContentPane().add(lblUsername);
		
		JLabel lblSsn = new JLabel("SSN");
		lblSsn.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSsn.setBounds(102, 101, 46, 14);
		getContentPane().add(lblSsn);
		
		lblPin = new JLabel("Pin");
		lblPin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPin.setBounds(102, 126, 46, 14);
		getContentPane().add(lblPin);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(163, 155, 118, 23);
		getContentPane().add(btnLogin);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBounds(0, 0, 444, 39);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		lblPleaseLoginTo = new JLabel("2016 Election");
		lblPleaseLoginTo.setBounds(168, 8, 107, 22);
		panel_1.add(lblPleaseLoginTo);
		lblPleaseLoginTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseLoginTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		panel_2 = new JPanel();
		panel_2.setBorder(null);
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(163, 72, 118, 71);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		username = new JTextField();
		username.setToolTipText("Please enter your issued username.");
		username.setText("");
		username.setColumns(10);
		username.setBounds(0, 0, 118, 20);
		panel_2.add(username);
		
		ssn1 = new JPasswordField();
		ssn1.setHorizontalAlignment(SwingConstants.CENTER);
		ssn1.setToolTipText("Please enter your Social Security Number.");
		ssn1.setBounds(0, 25, 33, 20);
		panel_2.add(ssn1);
		ssn1.setDocument(new JTextFieldLimit(3));
		
		ppin = new JPasswordField();
		ppin.setToolTipText("Please enter your issued pin number.");
		ppin.setBounds(0, 50, 118, 20);
		panel_2.add(ppin);
		
		ssn2 = new JPasswordField();
		ssn2.setHorizontalAlignment(SwingConstants.CENTER);
		ssn2.setToolTipText("Please enter your Social Security Number.");
		ssn2.setBounds(42, 25, 26, 20);
		panel_2.add(ssn2);
		
		ssn3 = new JPasswordField();
		ssn3.setHorizontalAlignment(SwingConstants.CENTER);
		ssn3.setToolTipText("Please enter your Social Security Number.");
		ssn3.setBounds(78, 25, 40, 20);
		panel_2.add(ssn3);
		
		SizeDocumentFilter sf1 = new SizeDocumentFilter(3);
		sf1.installFilter(ssn1);
		SizeDocumentFilter sf2 = new SizeDocumentFilter(2);
		sf2.installFilter(ssn2);
		SizeDocumentFilter sf3 = new SizeDocumentFilter(4);
		sf3.setAutoTab(false);
		sf3.installFilter(ssn3);
		
		label_1 = new JLabel("-");
		label_1.setBounds(71, 28, 4, 14);
		panel_2.add(label_1);
		
		JLabel label = new JLabel("-");
		label.setBounds(35, 28, 4, 14);
		panel_2.add(label);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		panel.setBounds(0, 248, 444, 23);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblCscVotingSystem = new JLabel("CSC250 Voting System\u00A9\u00AE\u2122");
		lblCscVotingSystem.setBounds(139, -1, 165, 25);
		panel.add(lblCscVotingSystem);
		lblCscVotingSystem.setHorizontalAlignment(SwingConstants.CENTER);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		setVisible(true);
		
		actionLogin();
	}
	
	public void actionLogin(){
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String un = new String(username.getText());
				String pssn = new String(ssn1.getPassword()) + "-" + new String(ssn2.getPassword()) + "-" + new String(ssn3.getPassword());
				String tpin = new String(ppin.getPassword());
				//StringBuffer hexString = null;
				
				//String js = String.format("{ \"state\": \"register\", \"vid_hash\": \"%s\", \"userInfo\": { \"vid\": \"%s\", \"ssn\": \"%s\", \"pin\": \"%s\"} }", hexString.toString(), username.getText(), pssn, tpin);
				
				String[] js = {un, pssn, tpin};
				
				//start connection
				connector = new Connection();
				connector.start(js);
				
				if(un.equals("265jMeges") && pssn.equals("700-33-6870") && tpin.equals("1234")) {
					VotingBallot vb = new VotingBallot(connector);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null,"Wrong Password / Username / Pin");
					username.setText("");
					ssn1.setText(""); ssn2.setText(""); ssn3.setText("");
					ppin.setText("");
					username.requestFocus();
				}
				//System.out.println(js);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
	
}

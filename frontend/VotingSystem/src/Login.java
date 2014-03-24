import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.SystemColor;


public class Login extends JFrame {
	private JLabel lblPin;
	private JLabel lblPleaseLoginTo;
	private JLabel lblCscVotingSystem;
	private JPanel panel_2;
	private JTextField username;
	private JPasswordField ssn1;
	private JPasswordField passwordField_3;
	private JPasswordField ssn2;
	private JPasswordField ssn3;
	private JLabel label;
	private JLabel label_1;
	public Login() {
		setTitle("Voting System");
		setSize(450,300);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		label = new JLabel("-");
		label.setBounds(210, 98, 4, 14);
		getContentPane().add(label);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(98, 76, 67, 14);
		getContentPane().add(lblUsername);
		
		JLabel lblSsn = new JLabel("SSN");
		lblSsn.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSsn.setBounds(119, 101, 46, 14);
		getContentPane().add(lblSsn);
		
		lblPin = new JLabel("Pin");
		lblPin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPin.setBounds(119, 126, 46, 14);
		getContentPane().add(lblPin);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		panel.setBounds(0, 238, 434, 23);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblCscVotingSystem = new JLabel("CSC250 Voting System\u00A9\u00AE\u2122");
		lblCscVotingSystem.setBounds(163, 0, 145, 25);
		panel.add(lblCscVotingSystem);
		lblCscVotingSystem.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(175, 155, 118, 23);
		getContentPane().add(btnLogin);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBounds(0, 0, 434, 39);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		lblPleaseLoginTo = new JLabel("2016 Election");
		lblPleaseLoginTo.setBounds(174, 11, 107, 22);
		panel_1.add(lblPleaseLoginTo);
		lblPleaseLoginTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseLoginTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		panel_2 = new JPanel();
		panel_2.setBorder(null);
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(175, 72, 118, 71);
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
		
		passwordField_3 = new JPasswordField();
		passwordField_3.setToolTipText("Please enter your issued pin number.");
		passwordField_3.setBounds(0, 50, 118, 20);
		panel_2.add(passwordField_3);
		
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
		label_1.setBounds(70, 26, 4, 14);
		panel_2.add(label_1);
		setVisible(true);
	}
}

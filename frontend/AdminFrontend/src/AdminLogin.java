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
import javax.swing.UIManager;


public class AdminLogin extends JFrame {
	private JLabel lblPin;
	private JLabel lblPleaseLoginTo;
	private JLabel lblCscVotingSystem;
	private JPanel panel_2;
	private JTextField username;
	private JPasswordField passwordField_3;
	public AdminLogin() {
		setTitle("Registration System");
		setSize(450,300);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(107, 109, 67, 14);
		getContentPane().add(lblUsername);
		
		lblPin = new JLabel("Password");
		lblPin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPin.setBounds(107, 129, 67, 14);
		getContentPane().add(lblPin);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 255, 255));
		panel.setBounds(0, 255, 450, 23);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		lblCscVotingSystem = new JLabel("CSC250 Voting System\u00A9\u00AE\u2122");
		lblCscVotingSystem.setBounds(138, 0, 205, 25);
		panel.add(lblCscVotingSystem);
		lblCscVotingSystem.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(175, 155, 120, 23);
		getContentPane().add(btnLogin);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(153, 204, 255));
		panel_1.setBounds(0, 0, 450, 39);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		lblPleaseLoginTo = new JLabel("Admin Login");
		lblPleaseLoginTo.setBackground(new Color(153, 204, 255));
		lblPleaseLoginTo.setBounds(89, 11, 300, 22);
		panel_1.add(lblPleaseLoginTo);
		lblPleaseLoginTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseLoginTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		panel_2 = new JPanel();
		panel_2.setBorder(null);
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(177, 76, 118, 71);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		username = new JTextField();
		username.setToolTipText("Please enter your issued username.");
		username.setText("");
		username.setColumns(10);
		username.setBounds(0, 29, 118, 20);
		panel_2.add(username);
		
		passwordField_3 = new JPasswordField();
		passwordField_3.setToolTipText("Please enter your issued pin number.");
		passwordField_3.setBounds(0, 50, 118, 20);
		panel_2.add(passwordField_3);
		
		SizeDocumentFilter sf1 = new SizeDocumentFilter(3);
		sf1.installFilter(ssn1);
		SizeDocumentFilter sf2 = new SizeDocumentFilter(2);
		sf2.installFilter(ssn2);
		SizeDocumentFilter sf3 = new SizeDocumentFilter(4);
		sf3.setAutoTab(false);
		sf3.installFilter(ssn3);
		setVisible(true);
	}
}

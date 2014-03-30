import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;


public class VoterRegistration extends JFrame {
	public VoterRegistration() {
		setSize(800,600);
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 19, 692, 529);
		getContentPane().add(panel);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setBounds(553, 472, 117, 29);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panel.setLayout(null);
		panel.add(btnNewButton);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(24, 24, 81, 16);
		panel.add(lblFirstName);
	}
}

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.GridBagLayout;


public class VotingBallot extends JFrame {
	public VotingBallot() {
		setSize(800,600);
		setTitle("Voting Ballot");
		getContentPane().setBackground(new Color(255, 255, 255));
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(0, 0, 784, 561);
		getContentPane().add(panel);
		panel.setLayout(null);
	}
}

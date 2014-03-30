import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;

import java.awt.Font;

import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;


public class VotingBallot extends JFrame implements ActionListener {
	private JParser jp;
	
	public VotingBallot() {
		jp = new JParser();
		this.setSize(800,600);
		this.setTitle("Voting Ballot");
		this.getContentPane().setBackground(new Color(255, 255, 255));
		this.getContentPane().setLayout(null);
		
		JPanel main_frame = new JPanel();
		main_frame.setBackground(new Color(255, 255, 255));
		main_frame.setBounds(0, 0, 794, 571);
		this.getContentPane().add(main_frame);
		main_frame.setLayout(null);
		
		JPanel msg_panel = new JPanel();
		msg_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		msg_panel.setBounds(0, 0, 794, 42);
		main_frame.add(msg_panel);
		msg_panel.setLayout(null);
		
		JLabel ballot_msg = new JLabel("Please Hit The Submit Button When You Are Done...");
		ballot_msg.setBounds(260, 14, 320, 14);
		msg_panel.add(ballot_msg);
		
		JPanel submit_panel = new JPanel();
		submit_panel.setBounds(0, 538, 794, 34);
		main_frame.add(submit_panel);
		submit_panel.setLayout(null);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(347, 5, 89, 23);
		submit_panel.add(btnSubmit);
		
		JPanel ballot_panel = new JPanel();
		GridBagLayout gbl_ballot_panel = new GridBagLayout();
		ballot_panel.setLayout(gbl_ballot_panel);
		GridBagConstraints c = new GridBagConstraints();
		
		int sizeOfProps = jp.getProposition().size();
		JPanel[] propsPanels = new JPanel[sizeOfProps];
		List<Proposition> props = jp.getProposition();
		
		for(int i = 0; i<sizeOfProps; i++){
			JPanel ppanel = new JPanel();
			ppanel.setBorder(new LineBorder(new Color(0, 0, 0), 1));
			ppanel.setPreferredSize(new Dimension(790, 100));
			
			JLabel pnum = new JLabel(props.get(i).getPropositionNumber() + ": ");
			JLabel msg = new JLabel(props.get(i).getQuestion());
			msg.setBounds(260, 14, 263, 14);
			ppanel.add(pnum);
			ppanel.add(msg);
			
			JRadioButton yesButton = new JRadioButton("Yes");
			yesButton.setActionCommand("Yes");
			yesButton.setSelected(true);

		    JRadioButton noButton = new JRadioButton("No");
		    noButton.setActionCommand("No");

		    JRadioButton abButton = new JRadioButton("Abstain");
		    abButton.setActionCommand("Abstain");
		    
		    ButtonGroup group = new ButtonGroup();
		    group.add(yesButton);
		    group.add(noButton);
		    group.add(abButton);
		    abButton.setSelected(true);
		    
		    yesButton.addActionListener(this);
		    noButton.addActionListener(this);
		    abButton.addActionListener(this);
		    
		    ppanel.add(yesButton);
		    ppanel.add(noButton);
		    ppanel.add(abButton);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = i;
			ballot_panel.add(ppanel, c);
			propsPanels[i] = ppanel;
		}
		
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(0, 0));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = sizeOfProps;
		ballot_panel.add(panel1, c);
		
		JScrollPane scroller = new JScrollPane(ballot_panel);
		scroller.setSize(794, 497);
		scroller.setLocation(0, 41);
		ballot_panel.setAutoscrolls(true);
		
		main_frame.add(scroller);
		
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
}

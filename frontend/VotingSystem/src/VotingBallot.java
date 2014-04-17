import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;

import java.awt.Font;

import javax.swing.border.LineBorder;
import javax.swing.text.View;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.List;


public class VotingBallot extends JFrame implements ActionListener {
	private JParser jp;
	private int row = 0;
	private ButtonGroup[] propsButtons;
	private JRadioButton[] selectedPres;
	
	public VotingBallot() {
		initializeBallot();
	}
	
	private void initializeBallot(){
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
		btnSubmit.addActionListener(this);
		submit_panel.add(btnSubmit);
		
		JPanel ballot_panel = new JPanel();
		GridBagLayout gbl_ballot_panel = new GridBagLayout();
		ballot_panel.setLayout(gbl_ballot_panel);
		GridBagConstraints c = new GridBagConstraints();
		
		//prespanel
		JPanel pres_panel = new JPanel();
		pres_panel.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		pres_panel.setPreferredSize(new Dimension(790, 25));
		
		JLabel pres_msg = new JLabel("Presdential Candidates:");
		pres_msg.setBounds(260, 14, 320, 14);
		pres_panel.add(pres_msg);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = row;
		ballot_panel.add(pres_panel, c);
		row++;
		
		//presidential candidates
		int sizeOfPres = jp.getPresidentialCandidates().size();
		JPanel[] presPanels = new JPanel[sizeOfPres];
		List<PresidentialCandidates> pres = jp.getPresidentialCandidates();
		ButtonGroup group = new ButtonGroup();
		selectedPres = new JRadioButton[sizeOfPres];
		
		for(int i = 0; i<sizeOfPres; i++){
			JPanel ppanel = new JPanel();
			ppanel.setPreferredSize(new Dimension(790, 25));
			
			JLabel pnum = new JLabel(pres.get(i).getPartyAffiliation() + ": ");
			JLabel msg = new JLabel(pres.get(i).getFullName());
			msg.setBounds(260, 14, 263, 14);
			ppanel.add(pnum);
			ppanel.add(msg);
			
			selectedPres[i] = new JRadioButton("Yes");
			selectedPres[i].setActionCommand("Yes");
			group.add(selectedPres[i]);
			selectedPres[i].setSelected(false);
		    
		    //yesButton.addActionListener(this);
		    
		    ppanel.add(selectedPres[i]);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = row;
			ballot_panel.add(ppanel, c);
			presPanels[i] = ppanel;
			row++;
		}
		
		//propspanel
		JPanel props_panel = new JPanel();
		props_panel.setBorder(new LineBorder(new Color(0, 0, 0), 1));
		props_panel.setPreferredSize(new Dimension(790, 25));
		
		JLabel props_msg = new JLabel("Propositions:");
		props_msg.setBounds(260, 14, 320, 14);
		props_panel.add(props_msg);
				
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = row;
		ballot_panel.add(props_panel, c);
		row++;
		
		//propositions
		int sizeOfProps = jp.getProposition().size();
		JPanel[] propsPanels = new JPanel[sizeOfProps];
		List<Proposition> props = jp.getProposition();
		propsButtons = new ButtonGroup[sizeOfProps];
		
		for(int j = 0; j<sizeOfProps; j++){
			JPanel ppanel = new JPanel();
			ppanel.setPreferredSize(new Dimension(790, 30));
			
			JLabel pnum = new JLabel(props.get(j).getPropositionNumber() + ": ");
			JLabel msg = new JLabel(props.get(j).getQuestion());
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
		    
		    propsButtons[j] = new ButtonGroup();
		    propsButtons[j].add(yesButton);
		    propsButtons[j].add(noButton);
		    propsButtons[j].add(abButton);
		    abButton.setSelected(true);
		    
		    /*yesButton.addActionListener(this);
		    noButton.addActionListener(this);
		    abButton.addActionListener(this);*/
		    
		    ppanel.add(yesButton);
		    ppanel.add(noButton);
		    ppanel.add(abButton);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = row;
			ballot_panel.add(ppanel, c);
			propsPanels[j] = ppanel;
			row++;
		}
		
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(0, 0));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = row;
		ballot_panel.add(panel1, c);
		
		JScrollPane scroller = new JScrollPane(ballot_panel);
		scroller.setSize(794, 497);
		scroller.setLocation(0, 41);
		ballot_panel.setAutoscrolls(true);
		
		main_frame.add(scroller);
		
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for(int i = 0; i < selectedPres.length; i++){
			if(selectedPres[i].isSelected()){
				System.out.println(i);
			}
		}
		
		for(int i = 0; i < propsButtons.length; i++){
			String s = getSelectedButtonText(propsButtons[i]);
			System.out.println(s);
		}
	}
	
	public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
}

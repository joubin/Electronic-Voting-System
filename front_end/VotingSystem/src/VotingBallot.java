import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;

import java.awt.Font;

import javax.swing.border.LineBorder;
import javax.swing.text.View;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


public class VotingBallot extends JFrame implements ActionListener {
	//private JParser jp;
	private int row = 0;
	private ButtonGroup[] propsButtons;
	private JRadioButton[] selectedPres;
	private Connection connector;
	private JSONObject ballot;
	
	public VotingBallot(Connection con, JSONObject b) {
		connector = con;
		ballot = b;
		initializeBallot();
	}
	
	private void initializeBallot(){
		//jp = new JParser();
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
		ArrayList presidents = (ArrayList) ballot.get("presidential_candidates");
		int sizeOfPres = presidents.size();
		JPanel[] presPanels = new JPanel[sizeOfPres];
		ButtonGroup group = new ButtonGroup();
		selectedPres = new JRadioButton[sizeOfPres];
		
		int i = 0;
		for(Object o : presidents){
			JPanel ppanel = new JPanel();
			ppanel.setPreferredSize(new Dimension(790, 25));
			JSONObject p = stringToJson(o.toString());
			
			JTextArea msg = new JTextArea(p.get("party_affiliation") + ": "+p.get("full_name"));
			msg.setBounds(260, 14, 263, 14);
			ppanel.add(msg);
			
			selectedPres[i] = new JRadioButton("Yes");
			selectedPres[i].setActionCommand("Yes");
			group.add(selectedPres[i]);
			selectedPres[i].setSelected(false);
		    
		    ppanel.add(selectedPres[i]);
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = row;
			ballot_panel.add(ppanel, c);
			presPanels[i] = ppanel;
			row++; i++;
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
		ArrayList propositions = (ArrayList) ballot.get("proposition");
		//Iterator<String> props = propositions.iterator();
		int sizeOfProps = propositions.size();
		JPanel[] propsPanels = new JPanel[sizeOfProps];
		propsButtons = new ButtonGroup[sizeOfProps];
		propsButtons = new ButtonGroup[sizeOfProps];
		
		int j=0;
		for(Object o : propositions){
			JPanel ppanel = new JPanel();
			ppanel.setPreferredSize(new Dimension(790, 400));
			
			JSONObject p = stringToJson(o.toString());
			//JLabel pnum = new JLabel();
			JTextArea msg = new JTextArea(p.get("id").toString() + ": "+p.get("question").toString(), 20, 65);
			
			//msg.setBounds(260, 14, 263, 14);
			msg.setFont(new Font("Times New Roman", 14, 14));
	        msg.setLineWrap(true);
	        msg.setWrapStyleWord(true);
	        msg.setOpaque(false);
	        msg.setEditable(false);
			//ppanel.add(pnum);
			//ppanel.add(msg);
			
			JLabel givenProp = new JLabel("Proposition number "+(j+1)+":");
			
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
		    
		    ppanel.add(givenProp);
		    ppanel.add(yesButton);
		    ppanel.add(noButton);
		    ppanel.add(abButton);
			
		    JScrollPane bScroller = new JScrollPane(msg, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			bScroller.setSize(790, 150);
			ppanel.setAutoscrolls(false);
		    
			ppanel.add(bScroller);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = row;
			ballot_panel.add(ppanel, c);
			propsPanels[j] = ppanel;
			j++;
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

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JSONObject finalBallot = new JSONObject();
		ArrayList pres = new ArrayList();
		ArrayList props = new ArrayList();
		
		ArrayList presidents = (ArrayList) ballot.get("presidential_candidates");
		int i = 0;
		for(Object o : presidents){
			JSONObject pr = stringToJson(o.toString());
			JSONObject a = new JSONObject();
			if(selectedPres[i].isSelected()){
				a.put("pick", "true");
			}else{
				a.put("pick", "false");
			}
			a.put("id", Integer.parseInt(pr.get("id").toString()));
			a.put("full_name", pr.get("full_name"));
			pres.add(a);
			i++;
		}
		
		ArrayList propositions = (ArrayList) ballot.get("proposition");
		int j = 0;
		for(Object o : propositions){
			String s = getSelectedButtonText(propsButtons[j]);
			JSONObject pr = stringToJson(o.toString());
			JSONObject a = new JSONObject();
			a.put("proposition_number", pr.get("proposition_number"));
			if(s.equals("Yes")){
				a.put("answer", "1");
			}else if(s.equals("No")){
				a.put("answer", "2");
			}else{
				a.put("answer", "3");
			}
			a.put("id", pr.get("id"));
			props.add(a);
			j++;
		}
		
		finalBallot.put("state", "ballot_response");
		finalBallot.put("proposition", props);
		finalBallot.put("presidential_candidates", pres);
		System.out.println("calling connector" + finalBallot.toJSONString());
		if(connector.sendBallot(finalBallot) == true){
			JOptionPane.showMessageDialog(this, "Vote Accepted!");
		}else{
			JOptionPane.showMessageDialog(this, "Already Voted!");
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
	
	public static JSONObject stringToJson(String s){
        JSONObject myNewString = null;
        try {
            myNewString =   (JSONObject)new JSONParser().parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not read json");
        }
        return myNewString;
    }
}

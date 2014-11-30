package csci201.finalproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChatPanel extends JPanel{
	
	//DATA
	private boolean enterIsValidSubmission, shiftIsDown, recipientsExist;
	private JTextArea displayArea;
	private JTextArea chatArea;
	private JPanel buttonPanel, enterToSendPanel;
	private JButton clearButton, submitButton;
	
	private ButtonListener buttonListener;
	private ChatListener chatListener;
	private EnterListener enterListener;
	private CheckBoxListener checkBoxListener;
	
	private JCheckBox pressEnterToSend;
	private JScrollPane chatScroll, displayScroll;
	private JPanel recipientSelectionPanel;
	private ArrayList<JCheckBox> checkBoxes;
	
	private BSClient.NetworkThread networkThread;
	private int numRecipients;
	
	//METHODS
	//constructor
	public ChatPanel(BSClient.NetworkThread nt){
		networkThread = nt;
		//Panel Setup
		//set vertical boxlayout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setMaximumSize(new Dimension(200,700));
		
		//Data member instantiation
		enterIsValidSubmission = true;
		shiftIsDown = false;
		recipientsExist = true;
		
		numRecipients = 0;
		
		displayArea = new JTextArea();
		displayArea.setMinimumSize(new Dimension(200,300));
		displayArea.setEditable(false);
		
		//add borders to display area
		displayArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10,10,10,10)));
		displayArea.setBackground(new Color(193,232,228));
		displayArea.setForeground(new Color(99,99,99));
		
		//allow text to wrap (by word) in display area and set default welcome message
		displayArea.setLineWrap(true);
		displayArea.setWrapStyleWord(true);
		displayArea.setText("[Chat] Hi there! To chat, just type your message in the white box below, select a recipient, and send it off!");
		displayArea.setForeground(Color.black);
		
		displayScroll = new JScrollPane(displayArea);
		displayScroll.setPreferredSize((new Dimension(200,300)));
		
		//set borders and line wrapping for chat box
		chatArea = new JTextArea();
		chatArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10,10,10,10)));
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatScroll = new JScrollPane(chatArea);
		
		//add listeners for typed words and for the "enter key"
		chatListener = new ChatListener();
		enterListener = new EnterListener();
		chatArea.getDocument().addDocumentListener(chatListener);
		chatArea.addKeyListener(enterListener);
		
		//panel to house option to use "enter" to submit message
		enterToSendPanel= new JPanel();
		enterToSendPanel.setLayout(new BoxLayout(enterToSendPanel, BoxLayout.X_AXIS));
		pressEnterToSend = new JCheckBox("Press enter to send? ");
		pressEnterToSend.setSelected(true);
		pressEnterToSend.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if (pressEnterToSend.getModel().isSelected()){
					enterIsValidSubmission = true;
				}
				else{
					enterIsValidSubmission = false;
				}
			}
		});
		enterToSendPanel.add(Box.createGlue());
		enterToSendPanel.add(pressEnterToSend);
		
		buttonListener = new ButtonListener();
		
		//create bottom panel with clear/send buttons and add listeners
		buttonPanel = new JPanel();
		
		clearButton = new JButton("Clear");
		clearButton.setEnabled(false);
		clearButton.addActionListener(buttonListener);
		
		submitButton = new JButton("Send");
		submitButton.setEnabled(false);
		submitButton.addActionListener(buttonListener);
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(clearButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(submitButton);
		
		recipientSelectionPanel = new JPanel();
		recipientSelectionPanel.setLayout(new BoxLayout(recipientSelectionPanel,BoxLayout.Y_AXIS));
		JPanel checkBoxWrapper = new JPanel();
		checkBoxWrapper.setLayout(new BoxLayout(checkBoxWrapper, BoxLayout.Y_AXIS));
		JPanel labelWrapper = new JPanel();
		checkBoxes = new ArrayList<JCheckBox>();
		checkBoxListener = new CheckBoxListener();
		for (int i=0;i<3;i++){
			JCheckBox cb = new JCheckBox("User" + (i+1));
			cb.setSelected(false);
			cb.addActionListener(checkBoxListener);
			checkBoxes.add(cb);
			checkBoxWrapper.add(cb);
			if (i >= numRecipients){
				cb.setVisible(false);
			}
		}
		JLabel recipientLabel = new JLabel("Desired Recipients");
		labelWrapper.add(recipientLabel);
		recipientSelectionPanel.add(labelWrapper);
		recipientSelectionPanel.add(checkBoxWrapper);
		
		//add all components to jpanel
		this.add(displayScroll);
		this.add(recipientSelectionPanel);
		this.add(chatScroll);
		this.add(enterToSendPanel);
		this.add(buttonPanel);
	}
	
	public void updateUserCheckBoxes(String users){
		users = users.trim();
		String userArray[] = users.split(" ");
		for (int i=0;i<userArray.length;i++){
			if (userArray[i].equals(networkThread.username)){
				continue;
			}
			checkBoxes.get(i).setText(userArray[i]);
			checkBoxes.get(i).setVisible(true);
		}
	}
	
	public void addMessage(String s, String origin){
		if (origin.equals(networkThread.username)) return;
		s = "[" + origin + "] " + s;
		displayArea.setText(displayArea.getText() + "\n" + s);
	}
	
	public void send(){
		String toBeSent = "";
		boolean recipientsExist = false;
		for (JCheckBox cb : checkBoxes){
			if (cb.isSelected()){
				recipientsExist = true;
				toBeSent = cb.getText() + " " + toBeSent;
			}
		}
		if (! recipientsExist){
			return;
		}
		toBeSent = "[" + toBeSent;
		toBeSent = toBeSent.trim();
		toBeSent = toBeSent + "]";
		String actualMessage = chatArea.getText();
		toBeSent = toBeSent + actualMessage;
		networkThread.send(toBeSent);
		addMessage(actualMessage, "Me");
		chatArea.setText("");
	}
	
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			if (ae.getSource() == clearButton){
				chatArea.setText("");
			}
			else if(ae.getSource() == submitButton){
				send();
			}
		}
	}
	
	class ChatListener implements DocumentListener{

		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
			if (recipientsExist){
				submitButton.setEnabled(true);
				clearButton.setEnabled(true);
			}
		}

		public void removeUpdate(DocumentEvent e) {
			if (chatArea.getText().equals("")){
				submitButton.setEnabled(false);
				clearButton.setEnabled(false);
			}
		}
	}
	
	class EnterListener implements KeyListener{

		public void keyPressed(KeyEvent k) {
			if (k.getKeyCode() == KeyEvent.VK_ENTER){
				if ((! shiftIsDown) && enterIsValidSubmission){
					k.consume();
					if (! chatArea.getText().equals("")){
						send();
					}
				}
				else if(enterIsValidSubmission){
					chatArea.setText(chatArea.getText() + "\n");
				}
			}
			else if(k.getKeyCode() == KeyEvent.VK_SHIFT){
				shiftIsDown = true;
			}
		}

		public void keyReleased(KeyEvent k) {
			if(k.getKeyCode() == KeyEvent.VK_SHIFT){
				shiftIsDown = false;
			}
		}

		public void keyTyped(KeyEvent k) {}
	}
	
	class CheckBoxListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			recipientsExist = false;
			for (JCheckBox cb : checkBoxes){
				if (cb.isSelected()){
					recipientsExist = true;
				}
			}
			if (recipientsExist){
				if (! chatArea.getText().equals("")){
					submitButton.setEnabled(true);
					clearButton.setEnabled(true);
				}
			}
			else{
				submitButton.setEnabled(false);
				clearButton.setEnabled(false);
			}
		}
	}
}

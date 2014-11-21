package csci201.finalproject;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChatPanel extends JPanel{
	
	//DATA
	private boolean enterIsValidSubmission;
	private JTextArea displayArea;
	private JTextArea chatArea;
	private JPanel buttonPanel, enterToSendPanel;
	private JButton clearButton, submitButton;
	
	private ButtonListener buttonListener;
	private ChatListener chatListener;
	private EnterListener enterListener;
	
	private JCheckBox pressEnterToSend;
	
	//METHODS
	//constructor
	public ChatPanel(){	
		//Panel Setup
		//set vertical boxlayout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Data member instantiation
		enterIsValidSubmission = false;
		
		displayArea = new JTextArea();
		displayArea.setPreferredSize(new Dimension(300,400));
		displayArea.setEditable(false);
		
		//add borders to display area
		displayArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10,10,10,10)));
		displayArea.setBackground(new Color(193,232,228));
		displayArea.setForeground(new Color(99,99,99));
		
		//allow text to wrap (by word) in display area and set default welcome message
		displayArea.setLineWrap(true);
		displayArea.setWrapStyleWord(true);
		displayArea.setText("Hi there! To chat, just type your message in the white box below, select a recipient, and send it off!");
		
		//set borders and line wrapping for chat box
		chatArea = new JTextArea();
		chatArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10,10,10,10)));
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		
		//add listeners for typed words and for the "enter key"
		chatListener = new ChatListener();
		enterListener = new EnterListener();
		chatArea.getDocument().addDocumentListener(chatListener);
		chatArea.addKeyListener(enterListener);
		
		//panel to house option to use "enter" to submit message
		enterToSendPanel= new JPanel();
		enterToSendPanel.setLayout(new BoxLayout(enterToSendPanel, BoxLayout.X_AXIS));
		pressEnterToSend = new JCheckBox("Press enter to send? ");
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
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(clearButton);
		buttonPanel.add(Box.createGlue());
		buttonPanel.add(submitButton);
		
		//add all components to jpanel
		this.add(displayArea);
		this.add(chatArea);
		this.add(enterToSendPanel);
		this.add(buttonPanel);
	}
	
	public void addMessage(String s){
		displayArea.setText(displayArea.getText() + "\n" + s);
	}
	
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			if (ae.getSource() == clearButton){
				chatArea.setText("");
			}
		}
	}
	
	class ChatListener implements DocumentListener{

		public void changedUpdate(DocumentEvent e) {}

		public void insertUpdate(DocumentEvent e) {
			submitButton.setEnabled(true);
			clearButton.setEnabled(true);
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
				if (enterIsValidSubmission){
					k.consume();
					//submit string
					chatArea.setText("");
				}
			}
		}

		public void keyReleased(KeyEvent k) {}

		public void keyTyped(KeyEvent k) {}
	}
}

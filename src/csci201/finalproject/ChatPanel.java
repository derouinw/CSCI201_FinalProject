package csci201.finalproject;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChatPanel extends JPanel{
	
	//DATA
	private boolean enterIsValidSubmission, shiftIsDown;
	private JTextArea displayArea;
	private JTextArea chatArea;
	private JPanel buttonPanel, enterToSendPanel;
	private JButton clearButton, submitButton;
	
	private ButtonListener buttonListener;
	private ChatListener chatListener;
	private EnterListener enterListener;
	
	private JCheckBox pressEnterToSend;
	private JScrollPane chatScroll;
	
	//METHODS
	//constructor
	public ChatPanel(){	
		//Panel Setup
		//set vertical boxlayout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Data member instantiation
		enterIsValidSubmission = true;
		shiftIsDown = false;
		
		displayArea = new JTextArea();
		displayArea.setPreferredSize(new Dimension(300,325));
		displayArea.setMinimumSize(new Dimension(300,325));
		displayArea.setEditable(false);
		
		//add borders to display area
		displayArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),BorderFactory.createEmptyBorder(10,10,10,10)));
		displayArea.setBackground(new Color(193,232,228));
		displayArea.setForeground(new Color(99,99,99));
		
		//allow text to wrap (by word) in display area and set default welcome message
		displayArea.setLineWrap(true);
		displayArea.setWrapStyleWord(true);
		displayArea.setText("[Chat] Hi there! To chat, just type your message in the white box below, select a recipient, and send it off!");
		
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
		
		//add all components to jpanel
		this.add(displayArea);
		this.add(chatScroll);
		this.add(enterToSendPanel);
		this.add(buttonPanel);
	}
	
	public void addMessage(String s){
		displayArea.setText(displayArea.getText() + "\n" + s);
	}
	
	public void send(){
		String toBeSent = chatArea.getText();
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
				if ((! shiftIsDown) && enterIsValidSubmission){
					k.consume();
					send();
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
}

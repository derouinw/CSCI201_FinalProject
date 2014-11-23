package csci201.finalproject;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LobbyGUI extends JPanel {
	JPanel WaitingLobby;
	JLabel WaitingLabel;
	JPanel WaitingButtonPanel;
	JButton InstructionsButton;
	JButton StartButton;
	//TODO:
	//arrayList of player's names to update the label
	//dialog box for the instructions
	//actionListener for the JButtons
	
	public LobbyGUI(){
		WaitingLobby = new JPanel(new BorderLayout());
		
		
		BufferedImage LobbyImage;
		try {
			LobbyImage = ImageIO.read(new File("treasureBackground.jpg"));
			JLabel LobbyImageLabel = new JLabel(new ImageIcon(LobbyImage));
			WaitingLobby.add(LobbyImageLabel,BorderLayout.NORTH );
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WaitingLabel = new JLabel("Connected players:");
		WaitingLobby.add(WaitingLabel,BorderLayout.CENTER);
		
		WaitingButtonPanel = new JPanel();
		InstructionsButton = new JButton("Instructions");
		StartButton = new JButton("Start");
		WaitingButtonPanel.add(InstructionsButton);
		WaitingButtonPanel.add(StartButton);
		WaitingLobby.add(WaitingButtonPanel,BorderLayout.SOUTH);
		
		
		add(WaitingLobby,BorderLayout.CENTER);
	}
}

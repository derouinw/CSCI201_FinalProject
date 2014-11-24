package csci201.finalproject;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main extends JFrame {
	SplashGUI splashScreen;
	LobbyGUI lobby;
	public Main() {
		super("Buccaneer Battles");
		setSize(800,800);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		splashScreen = new SplashGUI(this);
		//lobby = new LobbyGUI();
		add(lobby, BorderLayout.CENTER);
		setSize(lobby.getPreferredSize());
	}
	
	public static void main(String[] args) {
		Main main = new Main();
	}

}

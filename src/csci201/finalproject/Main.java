package csci201.finalproject;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main extends JFrame {
	SplashGUI splashScreen;
	LobbyGUI lobby;
	FleetGUI fleet;
	public Main() {
		super("Buccaneer Battles");
		setSize(800,800);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		//splashScreen = new SplashGUI(this);
		//lobby = new LobbyGUI();
		//fleet = new FleetGUI();
		add(fleet, BorderLayout.CENTER);
		setSize(fleet.getPreferredSize());
		setSize(800,600);
	}
	
	public static void main(String[] args) {
		Main main = new Main();
	}

}

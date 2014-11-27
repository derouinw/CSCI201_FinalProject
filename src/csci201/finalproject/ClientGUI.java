package csci201.finalproject;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import csci201.finalproject.BSClient.NetworkThread;

public class ClientGUI extends JFrame {
	// Gui elements
	CardLayout pages;
	JPanel container;
	
	// Gui pages for each game state
	SplashGUI splash;
	LobbyGUI lobby;
	FleetGUI fleet;
	GameGUI game;
	GameOverGUI gameOver;
	
	// options: â€œsplashâ€� â€œlobbyâ€� â€œfleet selectionâ€� â€œplayingâ€� â€œgame overâ€�
	String gameState;
	
	// From BSClient, interaction with server
	NetworkThread nt;
	
	// Main constructor
	public ClientGUI(NetworkThread nt) {
		// super constructor
		super("Buccaneer Battles");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// page switches between gamestates
		pages = new CardLayout();
		container = new JPanel(pages);
		
		// different game panels
		splash = new SplashGUI();
		lobby = new LobbyGUI();
		fleet = new FleetGUI();
		//game = new GameGUI();
		gameOver = new GameOverGUI();
		
		// add them to the CardLayout
		container.add("splash", splash);
		container.add("lobby", lobby);
		container.add("fleet selection", fleet);
		container.add("playing", game);
		container.add("game over", gameOver);
		
		// NetworkThread
		this.nt = nt;
		
		// finally...
		setPage("splash");
		setVisible(true);
	}
	
	// Sets the current page based on the string
	// Changes visible GUI panel and window size
	public void setPage(String page) {
		// string is equivalent to game state and CardLayout strings
		pages.show(container, page);
		
		// figure out other specifics
		// like resizing
		if (page.equals("splash")) {
			
		} else if (page.equals("lobby")) {
			// pass in IP address
		} else if (page.equals("fleet selection")) {
			
		} else if (page.equals("playing")) {
			
		} else if (page.equals("game over")) {
			
		}
	}
	
	// receive a message from the server
	public void receive(String msg) {
		// certain messages will stay in ClientGUI
		// such as changing game state
		
		// otherwise, the message will be sent along to
		// respective panel of current game state
		if (gameState.equals("splash")) {
			
		} else if (gameState.equals("lobby")) {
	
		} else if (gameState.equals("fleet selection")) {
			
		} else if (gameState.equals("playing")) {
			
		} else if (gameState.equals("game over")) {
			
		} else {
			
		}
	}
	
	private JPanel getCurPanel() {
		if (gameState.equals("splash")) {
			return splash;
		} else if (gameState.equals("lobby")) {
			return lobby;
		} else if (gameState.equals("fleet selection")) {
			return fleet;
		} else if (gameState.equals("playing")) {
			return game;
		} else if (gameState.equals("game over")) {
			return gameOver;
		} else {
			return null;
		}
	}

	class SplashGUI extends JPanel {
		
	}
	
	class LobbyGUI extends JPanel {
		
	}
	
	class FleetGUI extends JPanel {
		
		class ShipPlacementPanel extends JPanel {
			
		}
	}
	
	class GameOverGUI extends JPanel {
		
	}
}

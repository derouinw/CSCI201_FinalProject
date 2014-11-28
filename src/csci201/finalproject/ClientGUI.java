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
		setSize(700,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// page switches between gamestates
		pages = new CardLayout();
		container = new JPanel(pages);
		
		// different game panels
		splash = new SplashGUI(nt);
		lobby = new LobbyGUI(nt);
		fleet = new FleetGUI(nt);
		game = new GameGUI(nt);
		gameOver = new GameOverGUI();
		
		// add them to the CardLayout
		container.add("splash", splash);
		container.add("lobby", lobby);
		container.add("fleet selection", fleet);
		container.add("playing", game);
		container.add("game over", gameOver);
		add(container);
		
		// NetworkThread
		this.nt = nt;
		nt.client = this;
		
		// finally...
		gameState = "splash";
		setPage("splash");
		setVisible(true);
	}
	
	// Sets the current page based on the string
	// Changes visible GUI panel and window size
	// Only goes one way - forwards
	// In some cases, grab data from previous page
	// to use in next page
	public void setPage(String page) {
		// string is equivalent to game state and CardLayout strings
		pages.show(container, page);
		gameState = page;
		
		// figure out other specifics
		// like resizing
		if (page.equals("splash")) {
			
		} else if (page.equals("lobby")) {
			// pass in IP address
			lobby.setup();
			setSize(800,600);
		} else if (page.equals("fleet selection")) {
			
		} else if (page.equals("playing")) {
			// get data from FleetGUI and ClientGUI
			game.load(nt.players, nt.username, null);
		} else if (page.equals("game over")) {
			
		}
	}
	
	// receive a message from the server
	public void receive(String msg) {
		msg = msg.trim();
		//System.out.println("received " + msg);
		if (gameState.equals("splash")) {
			if (msg.equals("ready splash")) {
				setPage("lobby");
			}
		} else if (gameState.equals("lobby")) {
			if (msg.equals("ready lobby")) {
				setPage("fleet selection");
				System.out.println("switching to fleet");
			} else if (msg.startsWith("users")) {
				//System.out.println("wtf " + msg);
				String users = msg.substring(6);
				lobby.getUsernames(users);
			} else if (msg.equals("ready")) {
				// only for host
				lobby.StartButton.setEnabled(true);
			}
		} else if (gameState.equals("fleet selection")) {
			if (msg.equals("ready fleet")) {
				setPage("playing");
				//System.out.println();
			}
		} else if (gameState.equals("playing")) {
			if (msg.equals("enable")) {
				game.startTurn();
			} else if (msg.equals("disable")) {
				game.endTurn();
			}
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
	
	class GameOverGUI extends JPanel {
		
	}
}

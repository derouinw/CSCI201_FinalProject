package csci201.finalproject;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import csci201.finalproject.BSClient.NetworkThread;

public class ClientGUI extends JFrame {
	// Gui elements
	CardLayout pages;
	JPanel container;
	ChatPanel chatPanel;

	// Gui pages for each game state
	SplashGUI splash;
	LobbyGUI lobby;
	FleetGUI fleet;
	GameGUI game;
	GameOverGUI gameOver;

	// options: â€œsplashâ€� â€œlobbyâ€� â€œfleet selectionâ€� â€œplayingâ€�
	// â€œgame overâ€�
	String gameState;

	// From BSClient, interaction with server
	NetworkThread nt;

	// Main constructor
	public ClientGUI(NetworkThread nt) {
		// super constructor
		super("Buccaneer Battles");
		setSize(950, 700);
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
		chatPanel = new ChatPanel(nt);

		// add them to the CardLayout
		container.add("splash", splash);
		container.add("lobby", lobby);
		container.add("fleet selection", fleet);
		container.add("playing", game);
		container.add("game over", gameOver);
		add(container, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.WEST);

		// NetworkThread
		this.nt = nt;
		nt.client = this;

		// finally...
		gameState = "splash";
		setPage("splash");
		setResizable(false);
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
			chatPanel.setVisible(false);
		} else if (page.equals("lobby")) {
			// pass in IP address
			chatPanel.setVisible(true);
			lobby.setup();
		} else if (page.equals("fleet selection")) {

		} else if (page.equals("playing")) {
			// get data from FleetGUI and ClientGUI
			ArrayList<String> usernames = new ArrayList<String>(4); // fake data
			usernames.add("Eshed");
			usernames.add("Bill");
			usernames.add("Max");
			usernames.add("Cara");

			ArrayList<Shot> shotsList = new ArrayList<Shot>(5); // fake data
			shotsList.add(new Shot("Eshed", new Coordinate(0, 0), "Cara"));
			shotsList.add(new Shot("Bill", new Coordinate(1, 1), "Eshed"));
			shotsList.add(new Shot("Eshed", new Coordinate(0, 1), "Cara"));
			shotsList.add(new Shot("Max", new Coordinate(0, 2), "Eshed"));
			shotsList.add(new Shot("Eshed", new Coordinate(0, 3), "Cara"));
			shotsList.add(new Shot("Eshed", new Coordinate(1, 2), "Max"));
			shotsList.add(new Shot("Eshed", new Coordinate(5, 5), "Max"));
			shotsList.add(new Shot("Eshed", new Coordinate(9, 9), "Max"));

			Board board = fleet.shipPlacementGridPanel.board;
			game.load(usernames, nt.username, board);
			game.addShotsList(shotsList);
			
		} else if (page.equals("game over")) {

		}
	}

	// receive a message from the server
	public void receive(Message msg) {
		if (msg.value == null) {
			// disconnect
			JDialog popup = new JDialog(this, "Disconnected from server");
			dispose();
		}
		switch (msg.type) {
		case Message.TYPE_STRING:
			String sMsg;
			sMsg = ((String) msg.value).trim();
			if (sMsg.startsWith("chat")) {
				int space = sMsg.indexOf(" ");
				String message = sMsg.substring(space+1);
				chatPanel.addMessage(message, msg.source);
			} else if (gameState.equals("splash")) {
				if (sMsg.equals("ready splash")) {
					setPage("lobby");
				}
			} else if (gameState.equals("lobby")) {
				if (sMsg.equals("ready lobby")) {
					setPage("fleet selection");
				} else if (sMsg.startsWith("users")) {
					String users = sMsg.substring(6);
					lobby.getUsernames(users);
					chatPanel.updateUserCheckBoxes(users);
				} else if (sMsg.equals("ready")) {
					// only for host
					lobby.StartButton.setEnabled(true);
				}
			} else if (gameState.equals("fleet selection")) {
				if (sMsg.equals("ready fleet")) {
					setPage("playing");
				}
			} else if (gameState.equals("playing")) {
				if (sMsg.equals("enable")) {
					game.startTurn();
				} else if (sMsg.equals("disable")) {
					game.endTurn();
				}
			} else if (gameState.equals("game over")) {

			} else {

			}
			break;
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

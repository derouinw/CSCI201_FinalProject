package csci201.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class BSServer {
	// Port doesn't change
	final static int PORT = 2000;

	// There should only be one ServerThread in a server
	ServerThread st;

	// Store the number of players here too
	int numPlayers;
	
	boolean allConnected = false;

	// Constructor
	public BSServer(int numPlayers) {
		this.numPlayers = numPlayers;

		try {
			// ServerSocket accepts connections over internet
			ServerSocket ss = new ServerSocket(PORT);
			st = new ServerThread(numPlayers);

			int connections = 0;
			System.out.println("Accepting connections");
			while (connections < numPlayers) {
				// once the first player has connected (the host)
				// start the ServerThread
				if (connections == 1)
					st.start();

				// accept the connection from the ServerSocket
				Socket s = ss.accept();
				st.addPlayer(s);

				connections++;
			}
			
			// this point is reached once all players have connected
			st.playerThreads.get(0).send("ready");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ServerThread stores the connections to the players
	// and handles logic necessary for communicating with them
	// i.e. most server logic
	class ServerThread extends Thread {
		// Store a PlayerThread for each player's connection
		ArrayList<PlayerThread> playerThreads;

		// Max number of players allowed to connect
		int numPlayers;

		// Corresponds to game state in ClientGUI
		// options: “lobby” “fleet selection” “playing” “game over”
		String gameState;
		
		// during fleet selection, how many players are ready
		int fleetsFinished = 0;
		
		// during playing, the index of current player firing
		int curPlayer = 0; // default host

		// Constructor
		public ServerThread(int numPlayers) {
			this.numPlayers = numPlayers;
			playerThreads = new ArrayList<PlayerThread>();
			gameState = "lobby";
		}

		// Creates a connection with socket passed in,
		// creating a PlayerThread to handle it
		public void addPlayer(Socket s) {
			PlayerThread pt = new PlayerThread(s, this);
			playerThreads.add(pt);
			pt.start();
			
			String msg = "users ";
			for (int i = 0; i < playerThreads.size(); i++) {
				msg += playerThreads.get(i).username + " ";
			}
			broadcast(msg);
		}

		// Receive a message from a PlayerThread
		// (send from within PlayerThread.receive)
		public void receive(String msg, String src) {
			// take message from player and do what needs to be done
			System.out.println(msg);
			if (gameState.equals("lobby")) {
				// messages during lobby
				if (msg.equals("ready lobby")) {
					broadcast("ready lobby");
					gameState = "fleet selection";
					System.out.println("ready lobby");
				}
			} else if (gameState.equals("fleet selection")) {
				// if player hits ready
				if (msg.trim().equals("ready")) {
					fleetsFinished++;
					System.out.println("got ready");
				} else if (msg.trim().equals("unready")) {
					fleetsFinished--;
				}
			} else if (gameState.equals("playing")) {
				// TODO: receive a message while playing
			} else if (gameState.equals("game over")) {
				// there should be no messages received during game over
			}
		}

		// Thread.run
		// I.e. main loop for thread
		public void run() {
			while (true) { // TODO: real exit case
				// server logic here
				//System.out.println("ddone: " + fleetsFinished + ", total: " + playerThreads.size());
				System.out.println(gameState); // TODO: things break without this idk why
				if (gameState.equals("lobby")) {
					// send out message with player names
					// WRONG, this actually should only
					// get sent once, when PlayerThread
					// receives username
					
					// so nothing gets done here
				} else if (gameState.equals("fleet selection")) {
					// wait until all players are ready
					System.out.println("done: " + fleetsFinished + ", total: " + playerThreads.size());

					if (fleetsFinished == playerThreads.size()) {
						gameState = "playing";
						broadcast("ready fleet");
						System.out.println("switching to game");
						
						// start with host
						for (int i = 0; i < playerThreads.size(); i++) {
							if (i == curPlayer) playerThreads.get(i).send("enable");
							else playerThreads.get(i).send("disable");
						}
					}
				} else if (gameState.equals("playing")) {
					// TODO: playing part of run()
					
					// current player gets "enable" while the other
					// players get "disable"
					/*for (int i = 0; i < playerThreads.size(); i++) {
						if (i == curPlayer) playerThreads.get(i).send("enable");
						else playerThreads.get(i).send("disable");
					}*/
				} else if (gameState.equals("game over")) {
					// send out statistics data then kill server
					// TODO: send statistics data
					break;
				}
			}
		}

		// sends out a message to all players
		private void broadcast(String msg) {
			for (int i = 0; i < playerThreads.size(); i++) {
				playerThreads.get(i).send(msg);
			}
		}
		
		// get index of PlayerThread from the username
		private int ptNum(String name) {
			for (int i = 0; i < playerThreads.size(); i++) {
				if (playerThreads.get(i).username.equals(name)) {
					return i;
				}
			}
			
			// if reaches here, no player found with that username
			return -1;
		}
	}

	// PlayerThread is the connection to a single player
	class PlayerThread extends Thread {
		Socket s;

		// To communicate with player
		BufferedReader receive;
		PrintWriter send;

		// To send messages received from player to server
		ServerThread st;

		// Whether the player has been killed or not
		boolean active;

		// the user's handle
		String username;

		// Constructor
		public PlayerThread(Socket s, ServerThread st) {
			this.s = s;
			try {
				s.setSoTimeout(2000);
				receive = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				send = new PrintWriter(s.getOutputStream());
				this.st = st;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Send a message to the player
		public void send(String s) {
			send.write(s.length());
			send.write(s);
			send.flush();
		}
		
		private String receive() {
			int l = -1;
			String msg = "";
			try {
				l = receive.read();
				if (l == -1) System.out.println("disconnected");
				//else System.out.println(l);
				//if (l < 37) {
					// l is length of msg
					for (int i = 0; i < l; i++) {
						msg += (char)receive.read();
					}
				//}
			} catch (SocketTimeoutException ste) {
				//System.out.println("timeout nbd");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return msg;
		}

		// Thread.run
		public void run() {
			// when initially connected, send ready msg
			
			while (true) {
				try {
					String input = receive();
					//System.out.println(input);
					// logic with received message

					// set username
					if (input.startsWith("connect")) {
						username = input.substring(input.trim().indexOf(" "))
								.trim();
						//System.out.println(username + " received");
						send("gotUser");
						send("ready splash");
						
						String msg = "users ";
						for (int i = 0; i < st.playerThreads.size(); i++) {
							msg += st.playerThreads.get(i).username + " ";
						}
						st.broadcast(msg);
						// otherwise everything is handled in the ServerThread
					} else if (!input.startsWith("waiting")) {
						//System.out.println("receiving message");
						st.receive(input, username);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

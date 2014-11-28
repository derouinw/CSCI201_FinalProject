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
			st.playerThreads.get(0).send(new Message("ready"));
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
		boolean running = true;

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
			broadcast(new Message(msg));
		}

		// Receive a message from a PlayerThread
		// (send from within PlayerThread.receive)
		public void receive(Message msg, String src) {
			if (msg.type == -1) {
				// disconnect
				
			}
			// take message from player and do what needs to be done
			switch (msg.type) {
			case Message.TYPE_STRING:
				String sMsg = ((String) msg.value).trim();
				if (gameState.equals("lobby")) {
					// messages during lobby
					if (sMsg.equals("ready lobby")) {
						broadcast(new Message("ready lobby"));
						gameState = "fleet selection";
					}
				} else if (gameState.equals("fleet selection")) {
					// if player hits ready
					if (sMsg.equals("ready")) {
						fleetsFinished++;
					} else if (sMsg.equals("unready")) {
						fleetsFinished--;
					}
				} else if (gameState.equals("playing")) {
					// TODO: receive a message while playing
				} else if (gameState.equals("game over")) {
					// there should be no messages received during game over
				}
				break;
			case Message.TYPE_SHOTS:
				// TODO: receive shots;
				break;
			case Message.TYPE_BOARD:
				// TODO: receive board
				break;
			}
		}

		// Thread.run
		// I.e. main loop for thread
		public void run() {
			while (running) { // TODO: real exit case
				// server logic here
				System.out.print(""); // TODO: things break without this idk why
				if (gameState.equals("lobby")) {
					// send out message with player names
					// WRONG, this actually should only
					// get sent once, when PlayerThread
					// receives username

					// so nothing gets done here
				} else if (gameState.equals("fleet selection")) {
					// wait until all players are ready
					if (fleetsFinished == playerThreads.size()) {
						gameState = "playing";
						broadcast(new Message("ready fleet"));

						// start first turn of game
						// start with host
						for (int i = 0; i < playerThreads.size(); i++) {
							if (i == curPlayer)
								playerThreads.get(i)
										.send(new Message("enable"));
							else
								playerThreads.get(i).send(
										new Message("disable"));
						}
					}
				} else if (gameState.equals("playing")) {
					// TODO: playing part of run()
				} else if (gameState.equals("game over")) {
					// send out statistics data then kill server
					// TODO: this ^
					break;
				}
			}
		}

		// sends out a message to all players
		private void broadcast(Message msg) {
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
		boolean ptrunning = true;

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

		// Send a message to the player (TLV protocol)
		public void send(Message msg) {
			send.write(msg.type);
			send.write(msg.length);
			switch (msg.type) {
			case Message.TYPE_STRING:
				send.write((String) msg.value);
				break;
			case Message.TYPE_SHOTS:
				// TODO: send shots
				break;
			case Message.TYPE_BOARD:
				// TODO: send board
				break;
			}
			send.flush();
		}

		private Message receive() {
			int t = -1, l = -1;
			Message msg = new Message();
			try {
				t = receive.read();
				msg.type = t;
				if (t == -1) {
					st.running = false;
					ptrunning = false;
					s.close();
					
					// tell all users disconnect
					st.broadcast(new Message());
					return new Message();
				}

				l = receive.read();
				msg.length = l;

				// t is type of message
				switch (t) {
				case Message.TYPE_STRING:
					String value = "";

					for (int i = 0; i < l; i++) {
						value += (char) receive.read();
					}

					msg.value = value;
					break;
				case Message.TYPE_SHOTS:
					// TODO: receive shots
					break;
				case Message.TYPE_BOARD:
					// TODO: receive board
					break;
				}

			} catch (SocketTimeoutException ste) {
				// timeout, will happen if no messages for a second
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("received message at server:"
					+ (String) msg.value);
			return msg;
		}

		// Thread.run
		public void run() {
			// when initially connected, send ready msg

			while (ptrunning) {
				Message msg;
				try {
					msg = receive();
					if (msg == null)
						continue;
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				// logic with received message
				switch (msg.type) {
				case Message.TYPE_STRING:
					String input = ((String) msg.value).trim();
					if (input.startsWith("connect")) {
						username = input.substring(input.trim().indexOf(" "))
								.trim();
						send(new Message("gotUser"));
						send(new Message("ready splash"));

						String broadcast = "users ";
						for (int i = 0; i < st.playerThreads.size(); i++) {
							broadcast += st.playerThreads.get(i).username + " ";
						}
						st.broadcast(new Message(broadcast));
						// otherwise everything is handled in the
						// ServerThread
					}
					break;
				}

				st.receive(msg, username);

			}
		}
	}
}

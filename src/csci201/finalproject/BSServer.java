package csci201.finalproject;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
		// options: â€œlobbyâ€� â€œfleet selectionâ€� â€œplayingâ€� â€œgame overâ€�
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
			broadcast(msg);
		}

		// Receive a message from a PlayerThread
		// (send from within PlayerThread.receive)
		public void receive(Message msg, String src) {
			// take message from player and do what needs to be done
			switch (msg.type) {
			case Message.TYPE_STRING:
				String sMsg = ((String) msg.value).trim();
				if (sMsg.startsWith("[")) {
					// chat message
					int bracket = sMsg.indexOf("]");
					String users = sMsg.substring(1, bracket);
					String[] usersArr = users.split(" ");
					String message = sMsg.substring(bracket + 1);
					Message newMsg = new Message("chat " + message, src);
					for (int i = 0; i < usersArr.length; i++) {
						playerThreads.get(ptNum(usersArr[i].trim())).send(
								newMsg);
					}
				} else if (gameState.equals("lobby")) {
					// messages during lobby
					if (sMsg.equals("ready lobby")) {
						broadcast("ready lobby");
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
					// player sends updated ship number
					if (sMsg.startsWith("ships")) {
						int ships = Integer.valueOf(sMsg.substring(5).trim());
						broadcast("ships " + src + " " + ships);
						if (ships == 0) {
							playerThreads.get(ptNum(src)).active = false;
							playerThreads.get(ptNum(src)).send("game over");
							if (onlyOnePlayerRemaining()){ //should I end the game?
								gameState = "game over";
								broadcast(new Message("ready game over","Server"));
							}
						}
					}
				} else if (gameState.equals("game over")) {
					// there should be no messages received during game over
				}
				break;
			case Message.TYPE_SHOTS:
				ArrayList<Shot> shots = (ArrayList<Shot>) msg.value;
				
				for (Shot s : shots) {
					String user = s.getTargetPlayer();
					
					// send shot
					playerThreads.get(ptNum(user)).send(new Message(s));
				}
				
				// TODO: receive updated data from players
				// will happen in another receive
				
				// TODO: send updated data to players
				// will happen once that happens

				// next player leggo
				curPlayer = nextPlayer();
				for (int i = 0; i < playerThreads.size(); i++) {
					if ( i == curPlayer ){
						playerThreads.get(curPlayer).send("enable");
					}
					else{
						playerThreads.get(i).send("disable " + playerThreads.get(curPlayer).username);
					}
				}
				break;
			case Message.TYPE_BOARD:
				// TODO: receive board
				break;
			case Message.TYPE_SHOT:
				Shot s = (Shot)msg.value;
				//playerThreads.get(ptNum(s.getOriginPlayer())).send(new Message(s));
				
				broadcast(new Message(s, true));
			}
		}
		
		private boolean onlyOnePlayerRemaining(){
			int numPlayersOut = 0;
			for (int i=0;i<playerThreads.size();i++){
				if (playerThreads.get(i).active == false){
					numPlayersOut++;
				}
			}
			if (numPlayersOut == playerThreads.size()-1){
				return true;
			}
			return false;
		}

		private int nextPlayer() {
			//curPlayer++;
			//while (!playerThreads.get(curPlayer).active) {
				curPlayer++;
				if (curPlayer >= playerThreads.size())
					curPlayer = 0; // loop around
				//System.out.println("yeah the game is over bud")
			//}
			return curPlayer;
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
						broadcast("ready fleet");

						// start first turn of game
						// start with host
						//curPlayer = nextPlayer();
						for (int i = 0; i < playerThreads.size(); i++) {
							if ( i == curPlayer ){
								playerThreads.get(curPlayer).send("enable");
							}
							else{
								playerThreads.get(i).send("disable " + playerThreads.get(curPlayer).username);
							}
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

		private void broadcast(String msg) {
			broadcast(new Message(msg, "Server"));
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
		Socket s, s2;

		// To communicate with player
		// BufferedReader receive;
		ObjectInputStream receive;
		// PrintWriter send;
		ObjectOutputStream send;

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
				send = new ObjectOutputStream(s.getOutputStream());
				receive = new ObjectInputStream(s.getInputStream());
				this.st = st;
				active = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void send(String msg) {
			send(new Message(msg, "Server"));
		}

		// Send a message to the player (TLV protocol)
		public void send(Message msg) {
			if (!active) {
				boolean okay = false;
				// TODO: messages to send while inactive
				if (msg.type == Message.TYPE_STRING && ((String)msg.value).startsWith("chat")) {
					okay = true; // send chat messages
				}
				if (!okay) {
					return;					
				}
			}
			try {
				send.writeObject(msg);
				send.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private Message receive() {
			Message msg = new Message();

			try {
				msg = (Message) receive.readObject();
			} catch (EOFException eofe) {
				// TODO: handle disconnect
				return new Message();
			} catch (SocketTimeoutException ste) {
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (msg.type == Message.TYPE_STRING)
				System.out.println("received message at server: " + msg.value);
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
						send("gotUser");
						send("ready splash");

						String broadcast = "users ";
						for (int i = 0; i < st.playerThreads.size(); i++) {
							broadcast += st.playerThreads.get(i).username + " ";
						}
						st.broadcast(broadcast);
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

// why eshed

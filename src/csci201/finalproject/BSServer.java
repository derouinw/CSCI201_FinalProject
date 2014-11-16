package csci201.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BSServer {
	// Port doesn't change
	final static int PORT = 2000;
	
	// There should only be one ServerThread in a server
	ServerThread st;
	
	// Store the number of players here too
	int numPlayers;
	
	// Constructor
	public BSServer(int numPlayers) {
		this.numPlayers = numPlayers;
		
		try {
			// ServerSocket accepts connections over internet
			ServerSocket ss = new ServerSocket(PORT);
			st = new ServerThread(numPlayers);
			
			int connections = 0;
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
		
		// Constructor
		public ServerThread(int numPlayers) {
			this.numPlayers = numPlayers;
			playerThreads = new ArrayList<PlayerThread>();
		}
		
		// Creates a connection with socket passed in,
		// creating a PlayerThread to handle it
		public void addPlayer(Socket s) {
			PlayerThread pt = new PlayerThread(s, this);
			playerThreads.add(pt);
			pt.start();
		}
		
		// Receive a message from a PlayerThread
		// (send from within PlayerThread.receive)
		public void receive(String msg, String src) {
			// take message from player and do what needs to be done
		}
		
		// Thread.run
		// I.e. main loop for thread
		public void run() {
			// server logic here
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
		
		// Constructor
		public PlayerThread(Socket s, ServerThread st) {
			this.s = s;
			try {
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
			send.println(s);
		}
		
		// Thread.run
		public void run() {
			while (s.isConnected()) {
				try {
					String input = receive.readLine();
					
					// logic with received message
					// e.g. 
					// if (!input.contains("waiting")) st.receive(s)
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

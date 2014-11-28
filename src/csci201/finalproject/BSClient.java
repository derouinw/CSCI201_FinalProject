package csci201.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class BSClient {
	// Connection to the server
	NetworkThread nt;

	// Actual gui
	ClientGUI gui;

	// main entry point
	public static void main(String[] args) {
		// Create an instance because of static
		BSClient client = new BSClient();

		// Start the gui (ClientGUI is the JFrame)
		client.gui = new ClientGUI(client.nt);
	}

	public BSClient() {
		// create network thread to pass to ClientGUI instance
		nt = new NetworkThread();
	}

	// NetworkThread contains the connection to
	// the server and provides capabilities to
	// send and receive messages
	class NetworkThread extends Thread {
		// How many times to attempt connecting to the server before failing
		final int MAX_CONNECT_ATTEMPTS = 15;

		// Connection to server
		Socket s;

		// Send and receive messages using Socket
		BufferedReader receive;
		PrintWriter send;

		// Information about server
		String host;

		// Success on setupConnection()
		boolean connected;
		boolean gotUser;

		boolean isHost;

		// Handle displayed to other players and server
		String username;
		ArrayList<String> players;

		// In order to send along messages from receive
		ClientGUI client;

		// Constructor doesn't do much
		// The bulk happens in connect
		public NetworkThread() {
			super();

			host = "";
			connected = false;
			username = "";
			gotUser = false;

			players = new ArrayList<String>();
		}

		// Called from ClientGUI when the user chooses to connect
		// Host and username come from user-entered data
		public boolean connect(String host, String username, boolean isHost) {
			this.username = username;
			this.isHost = isHost;

			for (int i = 0; i < MAX_CONNECT_ATTEMPTS; i++) {
				if (setupConnection(host, BSServer.PORT)) {
					// if succeeded, start thread and exit constructor
					connected = true;
					start();
					return true;
				}
			}

			// reached max connect attempts
			connected = false;
			return false;
		}

		// A single attempt to connect to the server
		// returns false on failure (i.e. exception)
		private boolean setupConnection(String host, int port) {
			try {
				s = new Socket(host, port);
				s.setSoTimeout(1000);
				receive = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				send = new PrintWriter(s.getOutputStream());
			} catch (IOException e) {
				return false;
			}

			return true;
		}

		// TLV protocol
		public void send(Message msg) {
			send.write(msg.type);
			send.write(msg.length);
			switch (msg.type) {
			case Message.TYPE_STRING:
				send.write((String)msg.value);
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

		public void run() {

			// inital state, waiting for handshake
			send(new Message("connect " + username));
			while (!gotUser) {

				// messsages should only be strings at this point
				Message msg = receive();
				switch (msg.type) {
				case Message.TYPE_STRING:
					if (receive().value.equals("gotUser")) {
						gotUser = true;
					}
					break;
				}
				
			}

			// let's get going!
			Message msg;

			while (connected) {
				msg = receive();
				if (msg != null)
					client.receive(msg);
			}

		}

		private void parseUsers(String s) {
			players.clear();

			s = s.trim();
			int index = s.indexOf(",");
			players.add(s.substring(0, index));
			s = s.substring(index + 1).trim();
			index = s.indexOf(",");
			while (index != -1) {
				players.add(s.substring(0, index));
				s = s.substring(index + 1).trim();
				index = s.indexOf(",");
			}
		}

		public Message receive() {
			int t = -1, l = -1;
			Message msg = new Message();
			try {
				t = receive.read();
				msg.type = t;
				
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

			if (msg.value == null) {
				try { s.close(); } catch (IOException e) {}
				connected = false;
				return new Message();
			}
			
			System.out.println("received message at client:" + (String)msg.value);
			return msg;
		}
	}

	// RunServerThread exists solely to start
	// an asynchronous thread to run an instance
	// of the server on so the main thread doesn't hang
	static class RunServerThread extends Thread {
		BSServer server;
		int numPlayers;

		public RunServerThread(int numPlayers) {
			this.numPlayers = numPlayers;
			start();
		}

		public void run() {
			server = new BSServer(numPlayers);
		}
	}
}

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
		String curMsg, prevMsg;

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
		public void connect(String host, String username, boolean isHost) {
			this.username = username;
			this.isHost = isHost;

			for (int i = 0; i < MAX_CONNECT_ATTEMPTS; i++) {
				if (setupConnection(host, BSServer.PORT)) {
					// if succeeded, start thread and exit constructor
					System.out.println("connected");

					connected = true;
					start();
					return;
				}
			}

			// reached max connect attempts
			connected = false;
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

		// Send a message to the server
		public void send(String msg) {
			send.write(msg.length());
			send.write(msg);
			send.flush();
			//System.out.println("sending \"" + msg + "\"");
			curMsg = msg;
		}

		public void run() {

			// inital state, waiting for handshake
			while (!gotUser) {
				send("connect " + username);

				if (receive().equals("gotUser")) {
					gotUser = true;
					System.out.println("got user");
				}
			}

			System.out.println("ok");
			
//			try {
//				receive = new BufferedReader(new InputStreamReader(
//						s.getInputStream()));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

			// let's get going!
			String msg;
			
			while (true) {
				msg = receive();
				if (msg != null) client.receive(msg);
				//send();
			}

		}
		
		private void parseUsers(String s) {
			players.clear();
			
			s = s.trim();
			int index = s.indexOf(",");
			players.add(s.substring(0, index));
			s = s.substring(index+1).trim();
			index = s.indexOf(",");
			while (index != -1) {
				players.add(s.substring(0, index));
				s = s.substring(index+1).trim();
				index = s.indexOf(",");
			}
		}

		public String receive() {
			int l = -1;
			String msg = "";
			try {
				l = receive.read();
				if (l == -1) System.out.println("disconnected");
				//else System.out.println(l);
				//else System.out.println(l);
				//if (l < 37) {
					// l is length of msg
					for (int i = 0; i < l; i++) {
						msg += (char)receive.read();
					}
				//}
			} catch (SocketTimeoutException ste) {
				//System.out.println("timeout nbd");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("received \"" + msg + "\"");
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

package csci201.finalproject;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
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
		ObjectInputStream receive;
		ObjectOutputStream send;

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
				send = new ObjectOutputStream(s.getOutputStream());
				receive = new ObjectInputStream(s.getInputStream());
			} catch (SocketTimeoutException ste) {
				return false;
			} catch (IOException e) {
				return false;
			}

			return true;
		}

		public void send(String msg) {
			send(new Message(msg, username));
		}

		// TLV protocol
		public void send(Message msg) {
			try {
				send.writeObject(msg);
				send.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			// inital state, waiting for handshake
			send("connect " + username);
			while (!gotUser) {

				// messsages should only be strings at this point
				Message msg = receive();
				switch (msg.type) {
				case Message.TYPE_STRING:
					if (msg.value.equals("gotUser")) {
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
			Message msg = new Message();

			try {
				msg = (Message) receive.readObject();
			} catch (ClassCastException cce) {
				System.out.println("class cast exception");
			} catch (EOFException eofe) {
				// TODO: handle disconnect
				return new Message();
			} catch (StreamCorruptedException ste) {
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
				System.out.println("received message at client: " + msg.value);
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

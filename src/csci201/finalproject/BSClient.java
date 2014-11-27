package csci201.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
			send.println(msg);
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
			
			try {
				receive = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// let's get going!
			String msg;
			//Thread ka = new Thread(new KeepAlive(send, 100));
			
			while (true) {
				msg = receive();
				client.receive(msg);
			}

		}

		class KeepAlive implements Runnable {
			PrintWriter send;
			int interval;

			public KeepAlive(PrintWriter pw, int interval) {
				send = pw;
				this.interval = interval;
				start();
			}

			public void run() {
				while (true) {
					send.println("waiting");
					send.flush();
					try {
						sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		public String receive() {
			try {
				String msg = receive.readLine();
				// if (!msg.startsWith("users"))
				// System.out.println("received \"" + msg +"\"");
				return msg;
			} catch (IOException e) {
				return null;
			}
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

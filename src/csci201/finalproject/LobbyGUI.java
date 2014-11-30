package csci201.finalproject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LobbyGUI extends JPanel {
	JPanel WaitingLobby;
	JLabel WaitingLabel;
	JPanel WaitingButtonPanel;
	JButton InstructionsButton;
	JButton StartButton;

	BSClient.NetworkThread nt;
	ArrayList<String> usernames;
	String ipString = "";

	public LobbyGUI(BSClient.NetworkThread nt) {
		this.nt = nt;
	}

	public void setup() {
		WaitingLobby = new JPanel(new BorderLayout());

		usernames = new ArrayList<String>();

		BufferedImage LobbyImage;
		try {
			LobbyImage = ImageIO.read(new File("treasureBackground.jpg"));
			JLabel LobbyImageLabel = new JLabel(new ImageIcon(LobbyImage));
			WaitingLobby.add(LobbyImageLabel, BorderLayout.NORTH);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// get ip address
		try {
			if (nt.isHost)
				setIp(InetAddress.getLocalHost().getHostAddress());
			else
				setIp("Waiting for host");
		} catch (UnknownHostException e1) {
			setIp("[unknown host error (try again)]");
		}

		WaitingLabel = new JLabel(ipString);

		updateLabel();
		WaitingLobby.add(WaitingLabel, BorderLayout.CENTER);

		WaitingButtonPanel = new JPanel();
		InstructionsButton = new JButton("Instructions");
		InstructionsButton.addActionListener(new InstructionsListener());
		StartButton = new JButton("Start");
		StartButton.setEnabled(false);
		StartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nt.send("ready lobby");
			}
		});
		if (!nt.isHost)
			StartButton.setVisible(false);

		WaitingButtonPanel.add(InstructionsButton);
		WaitingButtonPanel.add(StartButton);
		WaitingLobby.add(WaitingButtonPanel, BorderLayout.SOUTH);

		add(WaitingLobby, BorderLayout.CENTER);
	}
	
	public void setIp(String ip) {
		ipString = "Give yer mates this secret code so they can join the crew! " + ip;
	}

	// function that takes a string split by spaces
	public ArrayList<String> getUsernames(String s) {
		String[] users = s.split(" ");
		usernames.clear();
		for (int i = 0; i < users.length; i++) {
			usernames.add(users[i]);
		}
		updateLabel();
		return usernames;
	}

	void updateLabel() {
		String text = "Connected players: ";
		for (int i = 0; i < usernames.size(); i++) {
			text += usernames.get(i)
					+ ((i == usernames.size() - 1) ? "" : ", ");
		}
		WaitingLabel.setText("<html>" + ipString + "<br>" + text + "</html>");
	}

	class InstructionsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane
					.showMessageDialog(
							null,
							"Kind of like BattleShip, but with more players, and way more awesome.",
							"Game Instructions", JOptionPane.PLAIN_MESSAGE);
		}
	}
}

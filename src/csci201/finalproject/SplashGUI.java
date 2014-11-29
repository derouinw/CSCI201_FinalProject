package csci201.finalproject;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import csci201.finalproject.BSClient.NetworkThread;
import csci201.finalproject.BSClient.RunServerThread;

public class SplashGUI extends JPanel {
	CardLayout splashPages;
	JPanel splashPagesContainer;

	JPanel SplashButtonPanel;
	JButton JoinButton;
	JButton CreateButton;

	JPanel choosePlayers;
	JLabel displayIPLabel;
	String ipString;
	JLabel numPlayersLabel;
	JComboBox numPlayersCombo;
	int numPlayers;
	JButton continuePlayers;

	JPanel chooseIP;
	JLabel chooseIPLabel;
	JTextField ipField;
	JButton continueIP;

	JPanel createName;
	JLabel handleLabel;
	JTextField name;
	JButton connect;

	JPanel main;
	JLabel connected;

	NetworkThread nt;

	String handle;
	String host;
	int port;
	boolean isHost = false;

	public SplashGUI(NetworkThread nt) {
		this.setPreferredSize(new Dimension(700, 600));
		this.nt = nt;

		// Read in Title Image
		BufferedImage TitleImage;
		try {
			TitleImage = ImageIO.read(new File("Title.jpg"));
			JLabel SplashTitleLabel = new JLabel(new ImageIcon(TitleImage));
			add(SplashTitleLabel);

		} catch (IOException e) {
			e.printStackTrace();
		}

		splashPages = new CardLayout();
		splashPagesContainer = new JPanel(splashPages);

		// SplashButton Panel Creation
		SplashButtonPanel = new JPanel();
		JoinButton = new JButton("Join Game");
		JoinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splashPages.show(splashPagesContainer, "chooseIP");
			}
		});
		CreateButton = new JButton("Create Game");

		CreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splashPages.show(splashPagesContainer, "choosePlayers");
			}
		});
		// Add components
		SplashButtonPanel.add(JoinButton);
		SplashButtonPanel.add(CreateButton);

		// ChoosePlayers Panel Creation
		choosePlayers = new JPanel();
		choosePlayers.setLayout(new BoxLayout(choosePlayers, BoxLayout.Y_AXIS));

		// get ip address
		try {
			ipString = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			ipString = "[unknown host error (try again)]";
		}

		displayIPLabel = new JLabel(
				"Give yer mates this secret code so they can join the crew! "
						+ ipString);
		JPanel numPanel = new JPanel();
		numPlayersLabel = new JLabel(
				"Select the count of pirates you wish to be in this battle. That includes yerself!");
		String[] numPlayersOptions = { "2", "3", "4" };
		numPlayersCombo = new JComboBox(numPlayersOptions);
		continuePlayers = new JButton("Continue");
		continuePlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				host = "127.0.0.1";
				numPlayers = Integer.valueOf((String) numPlayersCombo
						.getSelectedItem());
				splashPages.show(splashPagesContainer, "createName");
				RunServerThread rst = new RunServerThread(numPlayers); // this
																		// starts
																		// the
																		// server!
				isHost = true;
			}
		});
		choosePlayers.add(displayIPLabel);
		choosePlayers.add(numPlayersLabel);

		numPanel.add(numPlayersCombo);
		numPanel.add(continuePlayers);
		choosePlayers.add(numPanel);
		splashPagesContainer.add("choosePlayers", choosePlayers);

		// Enter IP address panel
		chooseIP = new JPanel();
		chooseIPLabel = new JLabel(
				"Enter the secret code so you can join the tumble!");
		ipField = new JTextField(10);
		continueIP = new JButton("Continue");
		continueIP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				host = ipField.getText();
				
				// validate ip address
				int lastIndex = 0, index = host.indexOf("."), count = 0;
				boolean valid = true;
				if (index != -1) {
					try {
						while (index != -1) {
							if (valid == false) break;
							count++;
							int num = Integer.valueOf(host.substring(lastIndex, index)
									.trim());
							if (num < 0 || num > 255) {
								valid = false;
								break;
							} else {
								lastIndex = index + 1;
								index = host.indexOf(".", lastIndex);
							}
						}
						
						// do one last time (after last dot)
						count++;
						int num = Integer.valueOf(host.substring(lastIndex)
								.trim());
						if (num < 0 || num > 255) {
							valid = false;
						}
					} catch (Exception ex) {
						valid = false;
					}
				} else {
					valid = false;
				}
				
				if (valid && count == 4) {
					splashPages.show(splashPagesContainer, "createName");
					host = host.trim();
				} else
					ipField.setText("Enter a valid ip!");
			}
		});
		chooseIP.add(chooseIPLabel);
		chooseIP.add(ipField);
		chooseIP.add(continueIP);
		splashPagesContainer.add("chooseIP", chooseIP);

		// Create user name Panel
		createName = new JPanel();
		handleLabel = new JLabel(
				"Enter the title by which you shall be known across the seven seas: ");
		name = new JTextField(10);
		connect = new JButton("Connect");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handle = name.getText();
				boolean valid = true;
				if (handle.indexOf(" ") != -1) {
					name.setText("No spaces allowed");
					valid = false;
				}
				
				if (valid && SplashGUI.this.nt.connect(host, handle, isHost))
					splashPages.show(splashPagesContainer, "main");
				else if (valid)
					name.setText("Connection failed");
			}
		});
		createName.add(handleLabel);
		createName.add(name);
		createName.add(connect);
		splashPagesContainer.add("createName", createName);

		main = new JPanel();
		connected = new JLabel("Not Connected");
		main.add(connected);
		splashPagesContainer.add("main", main);

		// Add various screen views
		splashPagesContainer.add("splash", SplashButtonPanel);
		splashPagesContainer.add("choosePlayers", choosePlayers);
		splashPagesContainer.add("chooseIP", chooseIP);
		splashPagesContainer.add("createName", createName);
		add(splashPagesContainer);
		splashPages.show(splashPagesContainer, "splash");
	}
}

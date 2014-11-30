package csci201.finalproject;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GameOverGUI extends JPanel {
	JPanel GameOver;
	CardLayout pages;
	JPanel GameStats;
	JPanel GameCredits;
	BufferedImage EndImage;
	JTable statsTable; 
	DefaultTableModel dtm;
	JScrollPane scrollPane;
	String[] colHeadings = {"User","Rank","Shots Hit", "Shots Taken", "Turns Played"};
	JButton EndButton;
	public GameOverGUI(){
		
		//card layout with game stats and credits
		pages = new CardLayout();
		GameOver = new JPanel(pages);
		
		GameOver.setVisible(true);
		add(GameOver);
		
		//add screens
		GameStats = new JPanel();
		GameStats.setLayout(new BorderLayout());
		GameOver.add("first", GameStats);
		GameCredits = new JPanel();
		GameCredits.setLayout(new BoxLayout(GameCredits,BoxLayout.Y_AXIS));
		GameOver.add("credits", GameCredits);
		
		dtm = new DefaultTableModel();
		statsTable = new JTable(dtm);
		
		statsTable.setSize(400,500);
		statsTable.setEnabled(false);
		
		scrollPane = new JScrollPane(statsTable);
		scrollPane.setPreferredSize(new Dimension(400,500));
		GameStats.add(scrollPane, BorderLayout.CENTER);
		
		for(int i=0; i<5; i++){
			dtm.addColumn(colHeadings[i]);
		}
		
		// make EndButton functional (takes you to credits)
		EndButton = new JButton("See who created this amazing game.");
		EndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pages.show(GameOver, "credits");
			}
		});
		GameStats.add(EndButton, BorderLayout.SOUTH);
		
		//add image to credit page
		try {
			EndImage = ImageIO.read(new File("Title.jpg"));
			JLabel ImageLabel = new JLabel(new ImageIcon(EndImage));
			GameCredits.add(ImageLabel, BorderLayout.NORTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//add names to credits
		JPanel NamesPanel = new JPanel();
		NamesPanel.setLayout(new BoxLayout(NamesPanel,BoxLayout.Y_AXIS));
		JLabel BillLabel = new JLabel("Bill Derouin");
		JLabel CaraLabel = new JLabel("Cara Kirshon");
		JLabel EshedLabel = new JLabel("Eshed Margalit");
		JLabel HaigLabel = new JLabel("Haig Nalbandian");
		JLabel MaxLabel = new JLabel("Max Weiner");
		
		NamesPanel.add(BillLabel);
		NamesPanel.add(CaraLabel);
		NamesPanel.add(EshedLabel);
		NamesPanel.add(HaigLabel);
		NamesPanel.add(MaxLabel);
		NamesPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		
		Box box = Box.createHorizontalBox();
	    
	    box.add(Box.createGlue());
	    box.add(NamesPanel);
	    box.add(Box.createGlue());
		
	    GameCredits.add(box);
		
		
		
		
	}
	void fillTable(String userName,String rank, String shotsHit,String shotsTaken, String turnsPlayed){
		Object[] currUser = new Object[]{userName,rank,shotsHit,shotsTaken,turnsPlayed};
		dtm.addRow(currUser); 
	}
}

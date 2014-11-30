package csci201.finalproject;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
		GameOver.add("credits", GameCredits);
		
		dtm = new DefaultTableModel();
		statsTable = new JTable(dtm);
		
		statsTable.setSize(500,600);
		statsTable.setEnabled(false);
		
		scrollPane = new JScrollPane(statsTable);
		GameStats.add(scrollPane, BorderLayout.CENTER);
		
		for(int i=0; i<5; i++){
			dtm.addColumn(colHeadings[i]);
		}
		
		EndButton = new JButton("See who created this amazing game.");
		EndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pages.show(GameOver, "credits");
			}
		});
		GameStats.add(EndButton, BorderLayout.SOUTH);
		
		try {
			EndImage = ImageIO.read(new File("Title.jpg"));
			JLabel ImageLabel = new JLabel(new ImageIcon(EndImage));
			GameCredits.add(ImageLabel, BorderLayout.NORTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}

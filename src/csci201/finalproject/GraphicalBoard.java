package csci201.finalproject;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GraphicalBoard extends JPanel {
	ArrayList<BoardSpace> boardSpaces;
	private String username;
	private boolean isOut;

	public GraphicalBoard(GameGUI.BoardSpaceListener bsl, String origin) {
		this.setLayout(new GridLayout(10, 10));
		isOut = false;
		boardSpaces = new ArrayList<BoardSpace>();
		for (int i = 0; i < 100; i++) {
			BoardSpace bs = new BoardSpace((i % 10), (i / 10));
			bs.addMouseListener(bsl);
			bs.setBackground(Color.LIGHT_GRAY);
			bs.setBorder(BorderFactory.createEtchedBorder());
			boardSpaces.add(bs);
			username = origin;
			this.add(bs);
		}
	}

	public void shipWreck() {
		for (BoardSpace bs : boardSpaces) {
			bs.setBackground(Color.black);
			bs.setOpaque(true);
		}
		isOut = true;
	}

	public void removeRedBorders() {
		for (BoardSpace bs : boardSpaces) {
			if (isOut) {
				bs.setBackground(Color.black);
			} else {
				if (bs.getBackground().equals(Color.blue)){
					bs.setBackground(Color.LIGHT_GRAY);
				}
			}
		}
	}
	
	public void addShotGraphic(int idx, boolean hit){
		if (hit){
			boardSpaces.get(idx).setText("H");
			boardSpaces.get(idx).setBackground(Color.red);
		}
		else{
			boardSpaces.get(idx).setText("M");
			boardSpaces.get(idx).setBackground(Color.green);
		}
	}

	public String getUsername() {
		return username;
	}
}

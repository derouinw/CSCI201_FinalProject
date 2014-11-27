package csci201.finalproject;


import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphicalBoard extends JPanel {
	ArrayList<BoardSpace> boardSpaces;
	private String username;
	private boolean isOut;
	
	public GraphicalBoard(GameGUI.BoardSpaceListener bsl, String origin) {
		this.setLayout(new GridLayout(10,10));
		isOut = false;
		boardSpaces = new ArrayList<BoardSpace>();
		for (int i=0;i<100;i++){
			BoardSpace bs = new BoardSpace((i/10),(i%10));
			bs.addMouseListener(bsl);
			bs.setBackground(Color.LIGHT_GRAY);
			bs.setBorder(BorderFactory.createEtchedBorder());
			boardSpaces.add(bs);
			username = origin;
			this.add(bs);
		}
	}
	
	public void shipWreck(){
		for (BoardSpace bs : boardSpaces){
			bs.setBackground(Color.black);
			bs.setOpaque(true);
		}
		isOut = true;
	}
	
	public void removeRedBorders(){
		for (BoardSpace bs : boardSpaces){
			if (isOut){
				bs.setBackground(Color.black);
			}
			else{
				bs.setBackground(Color.LIGHT_GRAY);
			}
		}
	}
	
	public String getUsername(){
		return username;
	}
}

package csci201.finalproject;


import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GraphicalBoard extends JPanel {
	ArrayList<BoardSpace> boardSpaces;
	
	public GraphicalBoard(GameGUI.BoardSpaceListener bsl) {
		this.setLayout(new GridLayout(10,10));
		boardSpaces = new ArrayList<BoardSpace>();
		for (int i=0;i<100;i++){
			BoardSpace bs = new BoardSpace((i/10),(i%10));
			bs.addMouseListener(bsl);
			bs.setBorder(BorderFactory.createEtchedBorder());
			boardSpaces.add(bs);
			this.add(bs);
		}
	}
}

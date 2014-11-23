package csci201.finalproject;

import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
	
	private HashMap<Coordinate,Ship> shipsToSpaces;
	private JLabel username;
	

	public Board() {
		shipsToSpaces = new HashMap<Coordinate,Ship>();
	}

	public Board(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public Board(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public Board(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
	
	public void addShip(Coordinate c, Ship ship){
		shipsToSpaces.put(c, ship);
	}
	
	public void receiveAttack(Coordinate c){
		for(Map.Entry<Coordinate, Ship> entry: shipsToSpaces.entrySet()){
			if(c.equals(entry.getKey())){
				entry.getValue().hit();
				shipsToSpaces.remove(entry.getValue());
			}
		}
	}

}

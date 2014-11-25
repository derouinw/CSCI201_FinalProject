package csci201.finalproject;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
	
	private HashMap<Coordinate,Ship> shipsToSpaces;
	ArrayList<BoardSpace> boardSpaces;
	private JLabel username;
	

	public Board() {
		setLayout(new GridLayout(10,10));
		shipsToSpaces = new HashMap<Coordinate,Ship>();
		boardSpaces = new ArrayList<BoardSpace>();
		for(int i=0; i<100; i++){
			BoardSpace oneSpace = new BoardSpace();
			boardSpaces.add(oneSpace);
			add(oneSpace);
		}
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
	
	public void receiveAttacksList(ArrayList<Shot> shots){
		for(int i = 0; i<shots.size(); i++){
			if(shots.get(i).getTargetPlayer().equals(username.getText()))
			{
				processAttack(shots.get(i));
			}
		}
	}
	
	public void processAttack(Shot s){
		for(Map.Entry<Coordinate, Ship> entry: shipsToSpaces.entrySet()){
			if(s.getShotDestination().equals(entry.getKey())){
				entry.getValue().hit();
				s.shotHitShip();
				shipsToSpaces.remove(entry.getValue());
			}
		}
	}

}

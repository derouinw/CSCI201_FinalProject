package csci201.finalproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Board extends JPanel implements Serializable {
	
	private HashMap<Coordinate,Ship> shipsToSpaces, firstCoordinates;
	private ArrayList<Shot> shotsFiredOnMyBoard;
	private ArrayList<BoardSpace> boardSpaces;

	public Board() {
		shipsToSpaces = new HashMap<Coordinate,Ship>();
		firstCoordinates = new HashMap<Coordinate,Ship>();
		shotsFiredOnMyBoard = new ArrayList<Shot>();
		boardSpaces = new ArrayList<BoardSpace>();

		this.setLayout(new GridLayout(10,10));
		boardSpaces = new ArrayList<BoardSpace>();
		for (int i=0;i<100;i++){
			BoardSpace bs = new BoardSpace((i/10),(i%10));
			bs.setBorder(BorderFactory.createEtchedBorder());
			boardSpaces.add(bs);
			this.add(bs);
		}
	}
	
	public void addShip(Coordinate c, Ship ship, boolean isFirstCoordinate){
		if (isFirstCoordinate){
			firstCoordinates.put(c, ship);
		}
		shipsToSpaces.put(c, ship);
	}
	
	/*public void receiveAttacksList(ArrayList<Shot> shots){
		for(int i = 0; i<shots.size(); i++){
			if(shots.get(i).getTargetPlayer().equals(username.getText()))
			{
				processAttack(shots.get(i));
			}
		}
	}*/
	
	public void processAttack(Shot s){
		for(Map.Entry<Coordinate, Ship> entry: shipsToSpaces.entrySet()){
			if(s.getShotDestination().equals(entry.getKey())){
				entry.getValue().hit();
				s.shotHitShip();
				shipsToSpaces.remove(entry.getValue());
			}
		}
		shotsFiredOnMyBoard.add(s);
	}
	
	public HashMap<Coordinate, Ship> getMap(){
		return shipsToSpaces;
	}
	
	public ArrayList<BoardSpace> getBoardspaces(){
		return boardSpaces;
	}
	
	public void paintComponent(Graphics g){
		//draw ships
		for (Map.Entry<Coordinate, Ship> entry : firstCoordinates.entrySet()){
			Coordinate c = entry.getKey();
			Image toDraw = entry.getValue().getImage();
			int x = c.getColumn()*36 + 10;
			int y = c.getRow()*30 + 24;
			g.drawImage(toDraw, x,y,null);
		}
		
		//draw shots
		for (Shot s: shotsFiredOnMyBoard){
			int x = (s.getShotDestination().getColumn() * 36) + 20;
			int y = (s.getShotDestination().getRow() * 30) + 30;
			if (s.wasAHit()){
				g.setColor(Color.red);
			}
			else{
				g.setColor(Color.green);
			}
			g.fillOval(x, y, 10, 10);
		}
	}

}

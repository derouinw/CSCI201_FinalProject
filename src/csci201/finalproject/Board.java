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

	private HashMap<Coordinate, Ship> shipsToSpaces, firstCoordinates;
	private ArrayList<ArrayList<Coordinate>> ships;
	private ArrayList<Shot> shotsFiredOnMyBoard;
	private ArrayList<BoardSpace> boardSpaces;

	public Board() {
		shipsToSpaces = new HashMap<Coordinate, Ship>();
		firstCoordinates = new HashMap<Coordinate, Ship>();
		shotsFiredOnMyBoard = new ArrayList<Shot>();
		boardSpaces = new ArrayList<BoardSpace>();

		this.setLayout(new GridLayout(10, 10));
		boardSpaces = new ArrayList<BoardSpace>();
		for (int i = 0; i < 100; i++) {
			BoardSpace bs = new BoardSpace((i / 10), (i % 10));
			bs.setBorder(BorderFactory.createEtchedBorder());
			boardSpaces.add(bs);
			this.add(bs);
		}

		ships = new ArrayList<ArrayList<Coordinate>>();
	}

	public void addShip(Coordinate c, Ship ship) {
		firstCoordinates.put(c, ship);
		
		ships.add(new ArrayList<Coordinate>());
		for (int i = 0; i < ship.healthPoints; i++) {
			Coordinate newC = new Coordinate(c);
			if (ship.vertical) {
				newC.setRow(c.getRow() + i);
			} else {
				newC.setColumn(c.getColumn() + i);
			}
			ships.get(ships.size() - 1).add(newC);
			shipsToSpaces.put(newC, ship);
		}
	}

	/*
	 * public void receiveAttacksList(ArrayList<Shot> shots){ for(int i = 0;
	 * i<shots.size(); i++){
	 * if(shots.get(i).getTargetPlayer().equals(username.getText())) {
	 * processAttack(shots.get(i)); } } }
	 */

	public boolean processAttack(Shot s) {
		boolean ret = false;
		for (Map.Entry<Coordinate, Ship> entry : shipsToSpaces.entrySet()) {
			if (s.getShotDestination().equals(entry.getKey())) {
				entry.getValue().hit();
				s.shotHitShip();
				shipsToSpaces.remove(entry.getValue());

				for (int i = 0; i < ships.size(); i++) {
					ArrayList<Coordinate> alc = ships.get(i);
					if (alc.contains(entry.getKey()))
						alc.remove(entry.getKey());
					if (alc.isEmpty())
						ships.remove(alc);
				}
				ret = true;
			}
		}
		shotsFiredOnMyBoard.add(s);
		return ret;
	}

	public HashMap<Coordinate, Ship> getMap() {
		return shipsToSpaces;
	}

	public int numShipsRemaining() {
		// return firstCoordinates.size();
		return ships.size();
	}

	public ArrayList<BoardSpace> getBoardspaces() {
		return boardSpaces;
	}

	public void paintComponent(Graphics g) {
		// draw ships
		for (Map.Entry<Coordinate, Ship> entry : firstCoordinates.entrySet()) {
			Coordinate c = entry.getKey();

			Image toDraw = entry.getValue().getSmallImage();
			int x = c.getColumn()*39 + 10;
			int y = c.getRow()*31 + 20;
			g.drawImage(toDraw, x,y,null);
		}

		
		//draw shots
		for (int i=0;i<shotsFiredOnMyBoard.size();i++){
			Shot s = shotsFiredOnMyBoard.get(i);
			int x = (s.getShotDestination().getColumn() * 39) + 20;
			int y = (s.getShotDestination().getRow() * 31) + 30;
			if (s.wasAHit()){
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			g.fillOval(x, y, 10, 10);
		}
	}

}

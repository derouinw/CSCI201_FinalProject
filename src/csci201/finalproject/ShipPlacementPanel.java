package csci201.finalproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

// This class handles a drag-and-drop/clicking ship placement functionality, 
// embedded within the fleet selection screen
public class ShipPlacementPanel extends JPanel implements MouseListener{
	final static int GRID_SIZE = 40, ROWS = 10, COLS = 10;
	final static String[] labels = { "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
	
	// actual board and ship data
	Board board;
	ArrayList<Ship> ships;
	private FleetGUI fGUI;
	private HashMap<Coordinate, Ship> sanityMap;

	// selected in gui component,
	// passed in from FleetGUI
	// 0 = dinghy
	// 1 = sloop
	// 2 = frigate
	// 3 = brigantine
	// 4 = Black Pearl/Galleon
	int shipType;

	// has the user hit ready yet?
	boolean enabled;

	//TODO DRAG & DROP?
	// for drag-and-drop
	//boolean mouseDown;

	// rotation of ship
	boolean vertical;

	// constructor
	public ShipPlacementPanel(FleetGUI fg) {
		// super constructor
		super();

		// initialize data structures
		board = new Board();
		fGUI = fg;
		ships = new ArrayList<Ship>();
		shipType = -1;
		sanityMap = new HashMap<Coordinate, Ship>();

		// initial boolean values
		enabled = true;
		vertical = true;
	}

	// called from FleetGUI, from a gui element
	// created to select ship type
	public void chooseShip(int ship) {
		shipType = ship;
//		System.out.println(shipType);
	}

	// toggled when user readies/unreadies
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	// bulk of logic
	// triggers when user clicks the panel
	public void mouseClicked(MouseEvent me) {
		// only does anything if enabled
		if (enabled) {
			// get mouse position
			int mX, mY, mRow, mCol;
			boolean inBounds = false;
			boolean valid = false;
			mX = me.getX() - 185;
			mY = me.getY() - 10;
			System.out.println(mX + " " + mY);
			if(mX >= 0 && mY >= 0){
				inBounds = true;
				//System.out.println("In Bounds!");
			}
			mCol = mX/40;
			mRow = mY/40;
			System.out.println("Row: " + mRow + " Col: " + mCol);
			// place ship at that location (if valid)
			// depends upon vertical
			Ship myShip = null;
			
			if(shipType == 0){
				myShip = new Dinghy();
				if(vertical){
					if(mRow <= 8){
						valid = true;
					}
				}
				else{
					if(mCol <= 8){
						valid = true;
					}
				}
			}
			else if(shipType == 1){
				myShip = new Sloop();
				if(vertical){
					if(mRow <= 7){
						valid = true;
					}
				}
				else{
					if(mCol <= 7){
						valid = true;
					}
				}
			}
			else if(shipType == 2){
				myShip = new Frigate();
				if(vertical){
					if(mRow <= 7){
						valid = true;
					}
				}
				else{
					if(mCol <= 7){
						valid = true;
					}
				}
			}
			else if(shipType == 3){
				myShip = new Brigantine();
				if(vertical){
					if(mRow <= 6){
						valid = true;
					}
				}
				else{
					if(mCol <= 6){
						valid = true;
					}
				}
			}
			else if(shipType == 4){
				myShip = new Galleon();
				if(vertical){
					if(mRow <= 5){
						valid = true;
					}
				}
				else{
					if(mCol <= 5){
						valid = true;
					}
				}
			}
			
			if(inBounds){
				if(mCol < 10 && mRow < 10){
					if(valid){
						boolean overlap = false;
						if(fGUI.haveShips(shipType)){
							//System.out.println("Have ships of type: " + shipType);
							if(vertical){
								Coordinate c = new Coordinate(mCol, mRow); //Are ship coordinates actual coordinates or grid spaces?
								Coordinate x = new Coordinate(mCol, mRow+1);
								Coordinate q = new Coordinate(mCol, mRow+2);
								Coordinate y = new Coordinate(mCol, mRow+3);
								Coordinate w = new Coordinate(mCol, mRow+4);
								
								if(sanityMap.containsKey(c) || sanityMap.containsKey(x)){
									overlap = true;
								}
								if(shipType > 0 && sanityMap.containsKey(q)){
									overlap = true;
								}
								if(shipType > 3 && sanityMap.containsKey(w)){
									overlap = true;
								}
								if(!overlap){
									fGUI.decrementShips(shipType);
									myShip.coord = c;
									myShip.vertical = true;
									ships.add(myShip);
									board.addShip(c, myShip);
									sanityMap.put(c, myShip);
									sanityMap.put(x, myShip);
									if(shipType > 0){
										sanityMap.put(q, myShip);
									}
									if(shipType > 2){
										sanityMap.put(y, myShip);
									}
									if(shipType > 3){
										sanityMap.put(w, myShip);
									}
									repaint();
									revalidate();
								}
								
							}
							else{
								Coordinate c = new Coordinate(mCol, mRow); 
								Coordinate x = new Coordinate(mCol+1, mRow);
								Coordinate q = new Coordinate(mCol+2, mRow);
								Coordinate y = new Coordinate(mCol+3, mRow);
								Coordinate w = new Coordinate(mCol+4, mRow);
								
								if(sanityMap.containsKey(c) || sanityMap.containsKey(x)){
									overlap = true;
								}
								if(shipType > 0 && sanityMap.containsKey(q)){
									overlap = true;
								}
								if(shipType > 3 && sanityMap.containsKey(w)){
									overlap = true;
								}
								if(!overlap){
									fGUI.decrementShips(shipType);
									myShip.coord = c;
									myShip.vertical = false;
									ships.add(myShip);
									board.addShip(c, myShip);
									sanityMap.put(x, myShip);
									if(shipType > 0){
										sanityMap.put(q, myShip);
									}
									if(shipType > 2){
										sanityMap.put(y, myShip);
									}
									if(shipType > 3){
										sanityMap.put(w, myShip);
									}
									repaint();
									revalidate();
								}
							}
						}
					}
				}
			}
		}
	}

	// drawing function, called from FleetGUI
	public void paintComponent(Graphics g) {
		// draw the background
		super.paintComponent(g);

		// draw the labels
		g.setColor(Color.BLACK);
		for (int i = 1; i <= ROWS; i++) {
			g.drawString(""+i, i*GRID_SIZE - 10, 10);
			g.drawString(labels[i], 0, i*GRID_SIZE);
		}

		// draw the ships
		for(int i = 0; i < ships.size(); i++){
			int x = ships.get(i).coord.getColumn();
			int y = ships.get(i).coord.getRow();
			g.drawImage(ships.get(i).getImage(), x*40 + 10, y*40 + 10, null);
		}
		// draw the grid
		g.setColor(Color.BLACK);
		for (int y = 10; y < ROWS * GRID_SIZE + 10; y += GRID_SIZE) {
			for (int x = 10; x < COLS * GRID_SIZE + 10; x += GRID_SIZE) {
				g.drawRect(x, y, GRID_SIZE, GRID_SIZE);
			}
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

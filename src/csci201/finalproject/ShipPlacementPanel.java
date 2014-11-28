package csci201.finalproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

// This class handles a drag-and-drop/clicking ship placement functionality, 
// embedded within the fleet selection screen
public class ShipPlacementPanel extends JPanel {
	final static int GRID_SIZE = 50, ROWS = 10, COLS = 10;
	final static String[] labels = { "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
	
	// actual board and ship data
	Board board;
	ArrayList<Ship> ships;

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
	public ShipPlacementPanel() {
		// super constructor
		super();

		// initialize data structures
		board = new Board();
		ships = new ArrayList<Ship>();
		shipType = -1;

		// initial boolean values
		enabled = true;
		vertical = true;
	}

	// called from FleetGUI, from a gui element
	// created to select ship type
	public void chooseShip(int ship) {
		shipType = ship;
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
			mX = me.getX() - 10;
			mY = me.getY() - 10;
			if(mX >= 0 && mY >= 0){
				inBounds = true;
			}
			mCol = mX/50;
			mRow = mY/50;
			// place ship at that location (if valid)
			// depends upon vertical
			Ship myShip = null;
			
			if(shipType == 0){
				myShip = myShip.new Dinghy();
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
				myShip = myShip.new Sloop();
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
				myShip = myShip.new Frigate();
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
				myShip = myShip.new Brigantine();
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
				myShip = myShip.new Galleon();
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
						FleetGUI.decrementShips(shipType);
						if(vertical){
							Coordinate c = new Coordinate(mCol, mRow); //Are ship coordinates actual coordinates or grid spaces?
							myShip.coord = c;
							myShip.vertical = true;
							ships.add(myShip);
							board.addShip(c, myShip);
							Coordinate x = new Coordinate(mCol, mRow+1);
							board.addShip(x, myShip);
							if(shipType > 0){
								Coordinate q = new Coordinate(mCol, mRow+2);
								board.addShip(q, myShip);
							}
							if(shipType > 2){
								Coordinate y = new Coordinate(mCol, mRow+3);
								board.addShip(y, myShip);
							}
							if(shipType > 3){
								Coordinate w = new Coordinate(mCol, mRow+4);
								board.addShip(w, myShip);
							}
							repaint();
							revalidate();
						}
						else{
							Coordinate c = new Coordinate(mCol, mRow);
							myShip.coord = c;
							myShip.vertical = false;
							ships.add(myShip);
							board.addShip(c, myShip);
							Coordinate x = new Coordinate(mCol+1, mRow);
							board.addShip(x, myShip);
							if(shipType > 0){
								Coordinate q = new Coordinate(mCol+2, mRow);
								board.addShip(q, myShip);
							}
							if(shipType > 2){
								Coordinate y = new Coordinate(mCol+3, mRow);
								board.addShip(y, myShip);
							}
							if(shipType > 3){
								Coordinate w = new Coordinate(mCol+4, mRow);
								board.addShip(w, myShip);
							}
							repaint();
						}
					}
				}
			}
		}
	}

	// drawing function, called from FleetGUI
	public void paintComponent(Graphics g) {
		// draw the background

		// draw the labels
		g.setColor(Color.BLACK);
		for (int i = 1; i <= ROWS; i++) {
			g.drawString(""+i, 30 + i*GRID_SIZE, 10);
			g.drawString(labels[i], 10, 10 + i*GRID_SIZE);
		}

		// draw the ships
		for(int i = 0; i < ships.size(); i++){
			int x = ships.get(i).coord.getColumn();
			int y = ships.get(i).coord.getRow();
			if(ships.get(i) instanceof Ship.Dinghy){
				BufferedImage dinghyImage = null;
				try {
					dinghyImage = ImageIO.read(new File("dinghy.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(dinghyImage, x*50, y*50, null);
			}
			if(ships.get(i) instanceof Ship.Sloop){
				BufferedImage sloopImage = null;
				try {
					sloopImage = ImageIO.read(new File("sloop.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(sloopImage, x*50, y*50, null);
			}
			if(ships.get(i) instanceof Ship.Frigate){
				BufferedImage frigateImage = null;
				try {
					frigateImage = ImageIO.read(new File("frigate.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(frigateImage, x*50, y*50, null);
			}
			if(ships.get(i) instanceof Ship.Brigantine){
				BufferedImage brigImage = null;
				try {
					brigImage = ImageIO.read(new File("brigantine.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(brigImage, x*50, y*50, null);
			}
			if(ships.get(i) instanceof Ship.Galleon){
				BufferedImage galleonImage = null;
				try {
					galleonImage = ImageIO.read(new File("galleon.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g.drawImage(galleonImage, x*50, y*50, null);
			}
		}
		// draw the grid
		g.setColor(Color.BLACK);
		for (int y = 10; y < ROWS * GRID_SIZE + 10; y += GRID_SIZE) {
			for (int x = 10; x < COLS * GRID_SIZE + 10; x += GRID_SIZE) {
				g.drawRect(x, y, GRID_SIZE, GRID_SIZE);
			}
		}
		
	}
}

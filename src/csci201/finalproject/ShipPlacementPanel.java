package csci201.finalproject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
		mouseDown = false;
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
			mX = me.getX() - 10;
			mY = me.getY() - 10;
			if(mX >= 0 && mY >= 0){
				inBounds = true;
			}
			mCol = mX/50;
			mRow = mY/50;
			// place ship at that location (if valid)
			// depends upon vertical
			if(inBounds){
				if(mCol < 10 && mRow < 10){
					if(vertical){
						Coordinate c = new Coordinate(mCol, mRow); //Are ship coordinates actual coordinates or grid spaces?
						board.addShip(c, shipType);
						repaint();
					}
					else{
						Coordinate c = new Coordinate(mCol, mRow);
						board.addShip(c, shipType);
						repaint();
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
		for(int i = 0; i < ships.getSize(); i++){
			if(shipType == 0)//NEED SHIP ACCESSOR FUNCTION!
			g.drawImage(dinghy, )
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

package csci201.finalproject;

import java.awt.Image;

public abstract class Ship {

	protected int healthPoints, startingHealthPoints;
	protected boolean vertical;
	protected Coordinate coord;
	protected Image image;
	
	public Ship() {
		// TODO Auto-generated constructor stub
	}
	
	public void hit(){
		if(healthPoints!=0){
			healthPoints--;
		}
	}
	
	public abstract Image getImage();
	public abstract Image getSmallImage();
	public int getHealthPoints(){
		return startingHealthPoints;
	}
}

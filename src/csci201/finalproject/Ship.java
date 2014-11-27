package csci201.finalproject;

public abstract class Ship {

	protected int healthPoints;
	protected boolean vertical;
	protected Coordinate coord;
	
	public Ship() {
		// TODO Auto-generated constructor stub
	}
	
	public void hit(){
		if(healthPoints!=0){
			healthPoints--;
		}
	}
}

package csci201.finalproject;

public abstract class Ship {

	private int healthPoints;
	
	public Ship() {
		// TODO Auto-generated constructor stub
	}
	
	public void hit(){
		if(healthPoints!=0){
			healthPoints--;
		}
	}

}

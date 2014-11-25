package csci201.finalproject;

public abstract class Ship {

	private int healthPoints;
	boolean vertical;
	Coordinate coord;
	
	public Ship() {
		// TODO Auto-generated constructor stub
	}
	
	public void hit(){
		if(healthPoints!=0){
			healthPoints--;
		}
	}
	
	public class Dinghy extends Ship {
		public Dinghy() {
			healthPoints = 2;
		}
	}
	public class Sloop extends Ship {
		public Sloop() {
			healthPoints = 3;
		}
	}
	public class Frigate extends Ship {
		public Frigate() {
			healthPoints = 3;
		}
	}
	public class Brigantine extends Ship {
		public Brigantine() {
			healthPoints = 4;
		}
	}
	public class Galleon extends Ship {
		public Galleon() {
			healthPoints = 5;
		}
	}

}

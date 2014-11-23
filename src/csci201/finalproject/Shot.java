package csci201.finalproject;

public class Shot {
	
	private String targetPlayer;
	private Coordinate shotDestination;
	private Boolean hitFlag;
	

	public Shot() {
		// TODO Auto-generated constructor stub
	}
	
	public Shot(String targetPlayer, Coordinate shotDestination){
		
		this.targetPlayer = targetPlayer;
		this.shotDestination = shotDestination;
		hitFlag = false;
		
	}
	
	public String getTargetPlayer(){
		return targetPlayer;
	}
	
	public Coordinate getShotDestination(){
		return shotDestination;
	}
	
	public void shotHitShip(){
		hitFlag = true;
	}

}

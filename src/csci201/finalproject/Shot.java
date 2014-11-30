package csci201.finalproject;

import java.io.Serializable;

public class Shot implements Serializable {

	private String targetPlayer, originPlayer;
	private Coordinate shotDestination;
	private boolean hitFlag;

	public Shot() {
		hitFlag = false;
	}

	public Shot(String targetPlayer, Coordinate shotDestination, String origin) {

		this.targetPlayer = targetPlayer;
		this.originPlayer = origin;
		this.shotDestination = shotDestination;
		hitFlag = false;

	}
	
	public Shot(Shot other) {
		this.targetPlayer = other.targetPlayer;
		this.originPlayer = other.originPlayer;
		this.shotDestination = other.shotDestination;
		this.hitFlag = other.hitFlag;
	}

	public String getTargetPlayer() {
		return targetPlayer;
	}

	public String getOriginPlayer() {
		return originPlayer;
	}

	public Coordinate getShotDestination() {
		return shotDestination;
	}

	public void shotHitShip() {
		hitFlag = true;
	}

	public boolean wasAHit() {
		return hitFlag;
	}

	public String toString() {
		return "Shot from " + originPlayer + " to " + targetPlayer + " at "
				+ shotDestination.toString() + " was a hit: " + hitFlag;
	}
}

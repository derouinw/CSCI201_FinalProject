package csci201.finalproject;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Sloop extends Ship implements Serializable {
	public Sloop() {
		super();
		healthPoints = 3;
		startingHealthPoints = 3;
	}

	public Image getImage() {
		if (vertical) {
			return (new ImageIcon("Sloop.png")).getImage();
		} else {
			return (new ImageIcon("sloopH.png")).getImage();
		}
	}
	
	public Image getSmallImage(){
		if (vertical) {
			return (new ImageIcon("Sloopsmall.png")).getImage();
		} else {
			return (new ImageIcon("sloopHsmall.png")).getImage();
		}
	}
	
	public String toString() { return "sloop"; }
}

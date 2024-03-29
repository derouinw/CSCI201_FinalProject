package csci201.finalproject;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;


public class Frigate extends Ship implements Serializable{
	public Frigate() {
		super();
		healthPoints = 3;
		startingHealthPoints = 3;
	}
	
	public Image getImage(){
		if(vertical){
			return (new ImageIcon("Frigate.png")).getImage();
		}
		else{
			return (new ImageIcon("frigateH.png")).getImage();
		}
	}
	
	public Image getSmallImage(){
		if (vertical) {
			return (new ImageIcon("Frigatesmall.png")).getImage();
		} else {
			return (new ImageIcon("frigateHsmall.png")).getImage();
		}
	}
	
	public String toString() { return "frigate"; }
}

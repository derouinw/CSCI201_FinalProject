package csci201.finalproject;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;


public class Dinghy extends Ship implements Serializable{
	public Dinghy(){
		super();
		healthPoints = 2;
	}
	
	public Image getImage(){
		if(vertical){
			return (new ImageIcon("Dinghy.png")).getImage();
		}
		else{
			return (new ImageIcon("dinghyH.png")).getImage();
		}
	}
	
	public Image getSmallImage(){
		if (vertical) {
			return (new ImageIcon("Dinghysmall.png")).getImage();
		} else {
			return (new ImageIcon("dinghyHsmall.png")).getImage();
		}
	}
	
	public String toString() { return "dinghy"; }
}


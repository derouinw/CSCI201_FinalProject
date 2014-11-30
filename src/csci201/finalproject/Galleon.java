package csci201.finalproject;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Galleon extends Ship implements Serializable{
	public Galleon() {
		super();
		healthPoints = 5;
	}
	
	public Image getImage(){
		if(vertical){
			return (new ImageIcon("Galleon.png")).getImage();
		}
		else{
			return (new ImageIcon("galleonH.png")).getImage();
		}
	}
}

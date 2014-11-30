package csci201.finalproject;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;


public class Brigantine extends Ship implements Serializable {
	public Brigantine() {
		super();
		healthPoints = 4;
	}
	
	public Image getImage(){
		if(vertical){
			return (new ImageIcon("Brigantine.png")).getImage();
		}
		else{
			return (new ImageIcon("brigantineH.png")).getImage();
		}
	}
}

package csci201.finalproject;

import java.awt.*;

import javax.swing.*;

public class Galleon extends Ship {
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

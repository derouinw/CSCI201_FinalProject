package csci201.finalproject;

import java.awt.*;

import javax.swing.*;

public class Galleon extends Ship {
	public Galleon() {
		super();
		healthPoints = 5;
	}
	
	public Image getImage(){
		return (new ImageIcon("Galleon.png")).getImage();
	}
}

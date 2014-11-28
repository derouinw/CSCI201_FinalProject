package csci201.finalproject;

import java.awt.*;

import javax.swing.*;

public class Brigantine extends Ship {
	public Brigantine() {
		super();
		healthPoints = 4;
	}
	
	public Image getImage(){
		return (new ImageIcon("Brigantine.png")).getImage();
	}
}
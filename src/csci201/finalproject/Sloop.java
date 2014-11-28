package csci201.finalproject;

import java.awt.*;

import javax.swing.*;

public class Sloop extends Ship {
	public Sloop() {
		super();
		healthPoints = 3;
	}
	
	public Image getImage(){
		return (new ImageIcon("Sloop.png")).getImage();
	}
}

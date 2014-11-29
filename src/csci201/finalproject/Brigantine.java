package csci201.finalproject;

import java.awt.*;

import javax.swing.*;

public class Brigantine extends Ship {
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
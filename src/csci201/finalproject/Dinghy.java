package csci201.finalproject;

import java.awt.*;

import javax.swing.*;

public class Dinghy extends Ship{
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
}

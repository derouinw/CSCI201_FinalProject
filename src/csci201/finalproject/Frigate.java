package csci201.finalproject;

import java.awt.*;
import javax.swing.*;

public class Frigate extends Ship {
	public Frigate() {
		super();
		healthPoints = 3;
	}
	
	public Image getImage(){
		return (new ImageIcon("Frigate.png")).getImage();
	}
}
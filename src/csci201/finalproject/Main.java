package csci201.finalproject;

import javax.swing.JFrame;

public class Main extends JFrame {

	public Main() {
		super("CSCI201 Final Project");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Main main = new Main();
	}

}

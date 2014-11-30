package csci201.finalproject;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main extends JFrame {
	GameOverGUI go;
	public Main() {
		super("Buccaneer Battles");
		setSize(800,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		go = new GameOverGUI();
		add(go, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		Main main = new Main();
	}

}

package csci201.finalproject;

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JLabel;

public class BoardSpace extends JLabel implements Serializable {
	private int row, col;

	public BoardSpace(int row, int col) {
		// TODO Auto-generated constructor stub
		this.row = row;
		this.col = col;
		this.setPreferredSize(new Dimension(30,30));
	}

	public BoardSpace(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public BoardSpace(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	public BoardSpace(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public BoardSpace(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	public BoardSpace(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}

}

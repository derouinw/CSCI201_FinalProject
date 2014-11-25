package csci201.finalproject;

import javax.swing.Icon;
import java.awt.*;
import javax.swing.*;
import javax.swing.JLabel;

public class BoardSpace extends JLabel {
	

	public BoardSpace() {
		// TODO Auto-generated constructor stub
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

}

package csci201.finalproject;

import java.io.Serializable;

public class Coordinate implements Serializable {
	
	private int row;
	private int column;

	public Coordinate() {
		
	}
	
	public Coordinate(int x, int y) {
		column = x;
		row = y;
	}
	
	public Coordinate(Coordinate c) {
		row = c.row;
		column = c.column;
	}

	public void setRow(int y){
		row = y;
	}
	
	public void setColumn(int x){
		column = x;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public String toString() {
		return "(" + column + ", " + row + ")";
	}
	
	//Overriding hash map lookup to allow map to find same coordinate pairs even if coordinate objects in different memory locs.
	
	public boolean equals(Object o){
		Coordinate other = (Coordinate) o;
		if((other.getRow()==this.row)&&(other.getColumn()==this.column)){
			return true;
		}
		
		return false;
	}
	
	public int hashCode(){
		return (int)((long)((Math.pow(row+17, 3))-(Math.pow(column+42, 2))));
	}

}

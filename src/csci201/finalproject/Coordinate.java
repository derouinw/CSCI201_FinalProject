package csci201.finalproject;

public class Coordinate {
	
	private int row;
	private int column;

	public Coordinate() {
		
	}
	
	public Coordinate(int x, int y) {
		row = x;
		column = y;
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
	
	public boolean equals(Coordinate other){
		
		if((other.getRow()==this.row)&&(other.getColumn()==this.column)){
			return true;
		}
		
		return false;
	}

}

package clue;

import java.awt.*;

public abstract class BoardCell {
	private int row;
	private int column;
	protected Color color;
    public BoardCell(){

    }
	public BoardCell(int r, int c) {
		row = r;
		column = c;
	}
	
	public boolean isWalkway() {
		return false;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public boolean isDoorway() {
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public abstract void draw(Graphics g, Board b);
    public void setColor(Color color){
        this.color = color;
    }
	
}

package clue;

import java.awt.Color;
import java.awt.Graphics;

public class WalkwayCell extends BoardCell{
	
	private static final int CELL_LENGTH = ClueGame.cellSize;
	private int x_position = getColumn() * CELL_LENGTH;
	private int y_position =  getRow() * CELL_LENGTH;
	
	public WalkwayCell (int r, int c) {
		super(r, c);

        setColor(Color.lightGray);

	}

	@Override
	public boolean isWalkway() {
		return true;
	}

	@Override
	public void draw(Graphics g, Board b) {
		
		g.setColor(color);
		g.fillRect(x_position, y_position, CELL_LENGTH, CELL_LENGTH);
		
		g.setColor(Color.BLACK);
		g.drawRect(x_position, y_position, CELL_LENGTH, CELL_LENGTH);
	}
	
}

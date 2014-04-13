package clue;

import java.awt.Color;
import java.awt.Graphics;

public class RoomCell extends BoardCell{
    //member variables
	public enum DoorDirection {UP, DOWN, LEFT, RIGHT, NONE;}
	private DoorDirection doorDirection;
	private char roomInitial;
	private boolean drawsName;
	private static final int DOOR_WIDTH = 3;
	private static final int CELL_LENGTH = ClueGame.cellSize;
	private int x_position = getColumn() * CELL_LENGTH;
	private int y_position =  getRow() * CELL_LENGTH;

    public RoomCell (){

    }


    //takes a
	public RoomCell(int r, int c, char i, char direction, boolean drawsName) {
		super(r, c);
		roomInitial = i;
        setColor(Color.BLACK);
		this.drawsName = drawsName;
		
		if (direction == 'U') {
			doorDirection = DoorDirection.UP;
		}
		else if (direction == 'D') {
			doorDirection = DoorDirection.DOWN;
		}
		else if (direction == 'L') {
			doorDirection = DoorDirection.LEFT;
		}
		else if (direction == 'R') {
			doorDirection = DoorDirection.RIGHT;
		}
		else {
			doorDirection = DoorDirection.NONE;
		}
	}
	
	@Override
	public boolean isDoorway() {
		if (doorDirection != DoorDirection.NONE) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isRoom() {
		return true;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public char getInitial() {
		return roomInitial;
	}

	@Override
	public void draw(Graphics g, Board b) {
		g.setColor(color);
		g.fillRect(x_position, y_position, CELL_LENGTH, CELL_LENGTH);
		
		g.setColor(Color.WHITE);

		if (drawsName) {
			g.drawString(b.getRoom(roomInitial), (x_position) + 10, (y_position) - 5) ;
		}
		
		if (isDoorway()) {
			switch (doorDirection) {
			case UP:
				g.fillRect(x_position, y_position, CELL_LENGTH, DOOR_WIDTH);
				break;
			case DOWN:
				g.fillRect(x_position, y_position + CELL_LENGTH - DOOR_WIDTH, CELL_LENGTH, DOOR_WIDTH);
				break;
			case LEFT:
				g.fillRect(x_position, y_position, DOOR_WIDTH, CELL_LENGTH);
				break;
			case RIGHT:
				g.fillRect(x_position + CELL_LENGTH - DOOR_WIDTH, y_position, DOOR_WIDTH, CELL_LENGTH);
				break;
			default:
				break;
			}
		}
		
	}
	
}

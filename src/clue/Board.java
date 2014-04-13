package clue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.TrayIcon.MessageType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Board: models the physical board game with features  such as cells that the players will participate on.
 */

public class Board extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<BoardCell> cells;
	private Map<Character,String> rooms;
	private int numRows;
	private int numColumns;
	private String layoutFile;
	private String legendFile;
	private Map<Integer, HashSet<Integer>> adjMtx;
	private boolean[] visited;
	private Set<BoardCell> targets;
	private final ClueGame owner;
    private Player currentPlayer;


	public Board(ClueGame owner) {
		this.owner = owner;
	}

	public Board(String layout, String legend, ClueGame owner) {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		adjMtx = new HashMap<Integer, HashSet<Integer>>();
		targets = new HashSet<BoardCell>();
		layoutFile = layout;
		legendFile = legend;
		this.owner = owner;
	}

	public Board(String layout, String legend) {
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		adjMtx = new HashMap<Integer, HashSet<Integer>>();
		targets = new HashSet<BoardCell>();
		layoutFile = layout;
		legendFile = legend;
		this.owner = new ClueGame();
	}
    public int roll (){
        Random rand = new Random();
        int randInt = rand.nextInt(6)+1;
        return randInt;

    }
    public void highlightTargets(){
        //redraw targets
        for(BoardCell rc : targets){

            rc.setColor(Color.CYAN);
                       //rc.draw(g, this);

        }

    }
    
    public void unHighlightTargets(){
    	//after moving, changes targets back to yellow
    	for(BoardCell b : targets){
    		if(b.isRoom()){
    			b.setColor(Color.black);
    		}
    		else{
    			b.setColor(Color.lightGray);

    		}
    	}
    }

    /**
     * Loads the configuration files
     */
	public void loadConfigFiles() {
		try {
			loadBoardConfig();
			loadRoomConfig();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

    /**
     * Loads only the csv file for rooms
     * @throws BadConfigFormatException
     * @throws FileNotFoundException
     */
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(layoutFile);
		Scanner inScan = new Scanner(reader);

		try {
			int i = 0;
			while (inScan.hasNext()) {
				String row = inScan.nextLine();
				String[] queue = row.split(",");
				if (queue.length != numColumns && (numColumns > 0)) {
					throw new BadConfigFormatException("Problem with the format of the board file.");

				}

				numColumns = queue.length;

				for (int j=0; j<queue.length; j++) {
					String roomKey = queue[j];
					if (!rooms.containsKey(roomKey.charAt(0))) {
						throw new BadConfigFormatException("Problem with the format of the board file: Invalid room key.");
					}

					if (roomKey.charAt(0) != 'W') {
						char tempDD = 'N';
						char tempRI = roomKey.charAt(0);
						boolean tempDrawsName = false;

						if (roomKey.length() > 1) {
							if (roomKey.charAt(1) == '$') {
								tempDrawsName = true;
							}
							else {
								tempDD = roomKey.charAt(1);
							}
						}

						RoomCell tempRC = new RoomCell(i, j, tempRI, tempDD, tempDrawsName);
						cells.add(tempRC);
					}
					else {
						WalkwayCell tempWC = new WalkwayCell(i, j);
						cells.add(tempWC);
					}
				}
				i++;
			}
			numRows = i;
		}
		finally {
			inScan.close();
		}

		//is it better to set this to the size of the board, or just the number of visitable areas?
		visited = new boolean[cells.size()];
		for(int i=0; i<visited.length; i++){
			visited[i] = false;
		}
	}

    /**
     * Loads the game's legend
     * @throws BadConfigFormatException
     * @throws FileNotFoundException
     */
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(legendFile);
		Scanner inScan = new Scanner(reader);

		try {
			while (inScan.hasNext()) {
				String u = inScan.nextLine();
				String[] queue = u.split(",");
				if (queue.length != 2) {
					throw new BadConfigFormatException("Problem with the format of the room legend file.");
				}

				//clean up whitespace from the value
				queue[1] = queue[1].replaceFirst(" ", "");

				rooms.put(queue[0].charAt(0), queue[1]);
			}

		}
		finally {
			inScan.close();
		}

	}

	//returns legend
	public Map<Character, String> getRooms(){
		return rooms;
	}

	public int calcIndex (int row, int column) {
		return ((numColumns*row) + column);
	}
    public ArrayList<BoardCell> getCells (){
        return cells;
    }
	public BoardCell getCell(int location) {
		return cells.get(location);
	}

	public BoardCell getCell(int r, int c) {
		int location = calcIndex(r, c);
		return cells.get(location);
	}

	public RoomCell getRoomCell(int r, int c) throws RuntimeException {
		int location = calcIndex(r, c);
		if (cells.get(location).isRoom()) {
			return (RoomCell) cells.get(location);
		}

		else { // We chose to handle a non-RoomCell situation by throwing a RuntimeException
			throw new RuntimeException("The given location does not contain a RoomCell.");
		}
	}

	public RoomCell getRoomCell(int location) throws RuntimeException {
		if (cells.get(location).isRoom()) {
			return (RoomCell) cells.get(location);
		}

		else { // We chose to handle a non-RoomCell situation by throwing a RuntimeException
			throw new RuntimeException("The given location does not contain a RoomCell.");
		}
	}


	public String getRoom(char c) {
		c = Character.toUpperCase(c);
		return rooms.get(c);
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

    /**
     * Fills in the adjacency Map for a given BoardCell
     */
	public void calcAdjacencies() {
		BoardCell current;
		HashSet<Integer> adjList;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				adjList = new HashSet<Integer>();
				current = getCell(calcIndex(i,j));

				if((i - 1) >= 0 && checkAdjacency(calcIndex(i-1,j), calcIndex(i,j))){
					adjList.add(calcIndex(i-1, j));

				}

				if((i + 1) < numRows && checkAdjacency(calcIndex(i+1,j), calcIndex(i,j))){
					adjList.add(calcIndex(i+1, j));
				}

				if((j-1) >= 0 && checkAdjacency(calcIndex(i,j-1), calcIndex(i,j))){
					adjList.add(calcIndex(i, j-1));
				}


				if((j+1) < numColumns && checkAdjacency(calcIndex(i,j+1), calcIndex(i,j))){
					adjList.add(calcIndex(i, j+1));
				}
                //System.out.println("AdjList : " + calcIndex(current.getRow(), current.getColumn()));
                adjMtx.put(calcIndex(current.getRow(), current.getColumn()), adjList);



			}
		}
	}

    /**
     * Determines if a given cell can be entered
     * @param index
     * @param origin
     * @return the validity of a given cell index to be traveled upon
     */
	public boolean checkAdjacency(int index, int origin){

		if (cells.get(index).isWalkway()) {
			if (cells.get(origin).isWalkway()) {
				return true;
			}
			else if (cells.get(origin).isDoorway()) {
				return checkDoorDirection(getCell(index), getCell(origin));
			}
			else { return false; }

		}
		else if (cells.get(index).isDoorway()) {
			if (cells.get(origin).isWalkway()) {
				return checkDoorDirection(getCell(origin), getCell(index));

			}
			else {
				return false;
			}
		}
		else if (cells.get(index).isRoom()) {
			return false;
		}
		else {
			return false;
		}

	}

    /**
     * returns the adjacency list for a particular cell
     * @param cell
     * @return
     */
	public HashSet<Integer> getAdjList(int cell){
		return adjMtx.get(cell);

	}

    /**
     * Ensures that all cells except the origin location are considered
     * not visited prior to calculating the targets
     * @param row
     * @param column
     * @param move
     */
	public void startTargets(int row, int column, int move) {
		// Setup

		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		if (adjMtx.isEmpty()) {

			calcAdjacencies();
		}
		targets.clear();
		visited[calcIndex(row, column)] = true;

		calcTargets(calcIndex(row,column), move);

	}


   	public void startTargets(int index, int move) {
		// Setup
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		if (adjMtx.isEmpty()) {
			calcAdjacencies();
		}
		targets.clear();
		visited[index] = true;
		calcTargets(index, move);
        for(BoardCell bc : targets){

        }
	}

    /**
     * Calculates potential targets for a given location and number of steps
     * @param index
     * @param move
     *
     */
    public void calcTargets(int index, int move){
        targets = new HashSet<BoardCell>();
        createTargets(index, move);


    }
	public void createTargets(int index, int move) {
		ArrayList<Integer> adjacentCells = new ArrayList<Integer>();
        //System.out.println("index : " + index);
       // System.out.println("adjacentCells: " + adjMtx);
       // System.out.println("specific : " + getAdjList(index));
		for (Integer cell : getAdjList(index)) {
			if (!visited[cell]) {
				adjacentCells.add(cell);
			}
		}
		for (Integer cell : adjacentCells) {
			visited[cell] = true;
			if (move == 1 && !targets.contains(cell)||cells.get(cell).isDoorway() ) {
				targets.add(cells.get(cell));
            //    System.out.println(calcIndex(cells.get(cell).getRow(), cells.get(cell).getColumn()));
            }


			else {
				createTargets(cell, (move - 1));
			}
			visited[cell] = false;
		}
	}

    /**
     *This function checks if from a given cell, if the given door can be entered from the direction
     * @param current the board cell that is the originated the call
     * @param door  the door board cell in question
     *@return  true if the door can be used, otherwise false
     */
	public boolean checkDoorDirection(BoardCell current, BoardCell door){
		if(!door.isDoorway()){ return false; }
		else{
			int diff = calcIndex(current.getRow(), current.getColumn())-calcIndex(door.getRow(), door.getColumn());
			//at this point we are guaranteed that door is actually a doorway, thus we are safe to cast as RoomCell
			switch(((RoomCell) door).getDoorDirection()){
			case UP: if( diff == (-1*numColumns) ){ return true; }else{ break; }
			case DOWN: if( diff == numColumns ){ return true; } else{ break;}
			case LEFT: if( diff == -1 ){ return true; } else{ break; }
			case RIGHT: if( diff == 1 ){ return true; } else{ break; }
			default: break;
			}
			return false;
		}
	}

   	public Set<BoardCell> getTargets(){
		return targets;
	}

    /**
     * Draws the computer and human players (separately)
     * @param myGraphics one Graphics object to draw upon
     */
	public void drawPlayers(Graphics myGraphics) {
		int row, column;
		for (Player p : owner.getComputers()) {
			row = getCell(p.getLocation()).getRow();
			column = getCell(p.getLocation()).getColumn();
			p.draw(myGraphics, row, column);
		}

		row = getCell(owner.getHuman().getLocation()).getRow();
		column = getCell(owner.getHuman().getLocation()).getColumn();
		owner.getHuman().draw(myGraphics, row, column);

	}


    /**
     *Draws the actual Grid
     * @param myGraphics
     */
	@Override
	public void paintComponent(Graphics myGraphics) {

		super.paintComponent(myGraphics);

		for (BoardCell c : cells) {
			c.draw(myGraphics, this);
		}

		myGraphics.setColor(Color.BLACK);
		myGraphics.drawLine(numColumns * ClueGame.cellSize, 0, numColumns * ClueGame.cellSize, numRows * ClueGame.cellSize);
		myGraphics.drawLine(0, numRows * ClueGame.cellSize, numColumns * ClueGame.cellSize, numRows * ClueGame.cellSize);

		drawPlayers(myGraphics);
//        owner.takeTurn( myGraphics);

	}

}

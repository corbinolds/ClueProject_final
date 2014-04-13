//package clueTests;
//
//import static org.junit.Assert.*;
//
//import java.io.FileNotFoundException;
//import java.util.Map;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import clue.BadConfigFormatException;
//import clue.Board;
//import clue.BoardCell;
//import clue.RoomCell;
//
//public class ConfigBoardTests {
//	private static Board testBoard;
//	public static final int NUM_ROOMS = 11;
//	public static final int NUM_ROWS = 22;
//	public static final int NUM_COLUMNS = 23;
//
//
//	@BeforeClass
//	public static void setUpBoard(){
//		testBoard = new Board("ClueLayout_rader.csv", "ClueLegend_test.txt");
//		testBoard.loadConfigFiles();
//	}
//
//	@Test
//	public void testRooms() {
//		Map<Character, String> rooms = testBoard.getRooms();
//
//		// Check correct number of rooms
//		assertEquals(NUM_ROOMS, rooms.size());
//
//		// Test contents of legend
//		assertEquals("Conservatory", rooms.get('C'));
//		assertEquals("Ballroom", rooms.get('B'));
//		assertEquals("Billiard room", rooms.get('R'));
//		assertEquals("Dining room", rooms.get('D'));
//		assertEquals("Walkway", rooms.get('W'));
//	}
//
//	//test the board getRoom method
//	@Test
//	public void testGetRooms(){
//		assertEquals("Conservatory", testBoard.getRoom('C'));
//		assertEquals("Ballroom", testBoard.getRoom('B'));
//		assertEquals("Billiard room", testBoard.getRoom('R'));
//		assertEquals("Dining room", testBoard.getRoom('D'));
//		assertEquals("Walkway", testBoard.getRoom('W'));
//
//		//test ability to handle lower case letters
//		assertEquals("Conservatory", testBoard.getRoom('c'));
//		assertEquals("Ballroom", testBoard.getRoom('b'));
//		assertEquals("Billiard room", testBoard.getRoom('r'));
//		assertEquals("Dining room", testBoard.getRoom('d'));
//		assertEquals("Walkway", testBoard.getRoom('w'));
//	}
//
//	@Test
//	public void testDimensions() {
//		assertEquals(NUM_ROWS, testBoard.getNumRows());
//		assertEquals(NUM_COLUMNS, testBoard.getNumColumns());
//	}
//
//	@Test
//	public void testCalcIndex() {
//		//test corners of the board
//		assertEquals(0, testBoard.calcIndex(0, 0));
//		assertEquals(NUM_COLUMNS-1, testBoard.calcIndex(0, NUM_COLUMNS-1));
//		assertEquals(483, testBoard.calcIndex(NUM_ROWS-1, 0));
//		assertEquals(505, testBoard.calcIndex(NUM_ROWS-1, NUM_COLUMNS-1));
//
//		//test a few others
//		assertEquals(26, testBoard.calcIndex(1, 3));
//		assertEquals(64, testBoard.calcIndex(2, 18));
//		assertEquals(264, testBoard.calcIndex(11, 11));
//		assertEquals(326,testBoard.calcIndex(14, 4));
//		assertEquals(180, testBoard.calcIndex(7, 19));
//		assertEquals(499, testBoard.calcIndex(21, 16));
//	}
//
//	//test the contents of a few random cells
//	@Test
//	public void testInitials() {
//		assertEquals('C', testBoard.getRoomCell(0, 0).getInitial());
//		assertEquals('R', testBoard.getRoomCell(4, 8).getInitial());
//		assertEquals('B', testBoard.getRoomCell(9, 0).getInitial());
//		assertEquals('O', testBoard.getRoomCell(21, 22).getInitial());
//		assertEquals('K', testBoard.getRoomCell(21, 0).getInitial());
//	}
//
//	@Test
//	public void testDoorDirections() {
//		//test a door pointing right
//		RoomCell room = testBoard.getRoomCell(4, 3);
//		assertTrue(room.isDoorway());
//		assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
//
//		//test a door pointing down
//		room = testBoard.getRoomCell(4, 8);
//		assertTrue(room.isDoorway());
//		assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
//
//		//test a door pointing left
//		room = testBoard.getRoomCell(15, 18);
//		assertTrue(room.isDoorway());
//		assertEquals(RoomCell.DoorDirection.LEFT, room.getDoorDirection());
//
//		//test a door pointing up
//		room = testBoard.getRoomCell(14, 11);
//		assertTrue(room.isDoorway());
//		assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
//
//		//test if cell can recognize it isn't a door
//		room = testBoard.getRoomCell(14, 14);
//		assertFalse(room.isDoorway());
//
//		//test that walkways do not act as doors
//		BoardCell cell = testBoard.getCell(testBoard.calcIndex(0, 6));
//		assertFalse(cell.isDoorway());
//	}
//
//	//test that all doors in test config are detected
//	@Test
//	public void testDoorways() {
//		int numDoors = 0;
//		int totalCells = testBoard.getNumColumns() * testBoard.getNumRows();
//		assertEquals(506, totalCells);
//		for (int i=0; i<totalCells; i++)
//		{
//			BoardCell cell = testBoard.getCell(i);
//			if (cell.isDoorway())
//				numDoors++;
//		}
//		assertEquals(16, numDoors);
//	}
//
//
//	@Test (expected = BadConfigFormatException.class)
//	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
//		//create new board to test bad config files
//		Board tBoard = new Board("ClueLayoutBadColumns.csv", "ClueLegend.txt");
//		tBoard.loadRoomConfig();
//		tBoard.loadBoardConfig();
//	}
//
//	@Test (expected = BadConfigFormatException.class)
//	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
//		//create new board to test bad config files
//		Board tBoard = new Board("ClueLayoutBadRoom.csv", "ClueLegend.txt");
//		tBoard.loadRoomConfig();
//		tBoard.loadBoardConfig();
//	}
//
//	@Test (expected = BadConfigFormatException.class)
//	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
//		//create new board to test bad legend files
//		Board tBoard = new Board("ClueLayout.csv", "ClueLegendBadFormat.txt");
//		tBoard.loadRoomConfig();
//		tBoard.loadBoardConfig();
//
//	}
//
//
//}

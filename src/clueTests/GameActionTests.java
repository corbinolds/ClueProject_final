//package clueTests;
//
//import static org.junit.Assert.*;
//
//import java.util.ArrayList;
//import java.util.Set;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import clue.BoardCell;
//import clue.Card;
//import clue.ClueGame;
//import clue.ComputerPlayer;
//import clue.Solution;
//
//public class GameActionTests {
//
//	private static ClueGame game;
//	private static Card white, mustard, knife, rope, study, library;
//	@BeforeClass
//	public static void setupClass() {
//		game = new ClueGame("CluePlayers.txt", "ClueLayout.csv", "ClueLegend.txt", "ClueWeapons.txt");
//		game.loadConfigFiles();
//
//		white = new Card(Card.cardType.PERSON, "Mrs. White");
//		mustard = new Card(Card.cardType.PERSON, "Colonel Mustard");
//		knife = new Card(Card.cardType.WEAPON, "Knife");
//		rope = new Card(Card.cardType.WEAPON, "Rope");
//		study = new Card(Card.cardType.ROOM, "Study");
//		library = new Card(Card.cardType.ROOM, "Library");
//
//	}
//
//
//	// Room has NOT been visited, but is adjacent. Ensure the room is picked.
//
//	@Test
//	public void testRoomPreference() {
//
//		// Place a player next to a door
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(4, 4));
//
//		// Create target list 1 away
//		game.getBoard().startTargets(testPlayer.getLocation(), 1);
//
//		Set<BoardCell> testTargets = game.getBoard().getTargets();
//
//		for (int i = 0; i < 100; i++) {
//			BoardCell pickedTarget = testPlayer.pickTarget(testTargets);
//			assertEquals(4, pickedTarget.getRow());
//			assertEquals(3, pickedTarget.getColumn());
//		}
//	}
//
//	@Test
//	public void testTargetRandomSelection() {
//
//		// Place a player at 16, 7
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(16, 7));
//
//		// Create target list
//		game.getBoard().startTargets(testPlayer.getLocation(), 2);
//
//		Set<BoardCell> testTargets = game.getBoard().getTargets();
//
//		int loc_15_6Tot = 0;
//		int loc_15_8Tot = 0;
//		int loc_14_7Tot = 0;
//		int loc_17_6Tot = 0;
//		int loc_17_8Tot = 0;
//		int loc_18_7Tot = 0;
//		int loc_16_9Tot = 0;
//
//		// Run the test 200 times
//		for (int i=0; i<200; i++) {
//			BoardCell pickedTarget = testPlayer.pickTarget(testTargets);
//			if (pickedTarget == game.getBoard().getCell(15, 6))
//				loc_15_6Tot++;
//			else if (pickedTarget == game.getBoard().getCell(15, 8))
//				loc_15_8Tot++;
//			else if (pickedTarget == game.getBoard().getCell(14, 7))
//				loc_14_7Tot++;
//			else if (pickedTarget == game.getBoard().getCell(17, 6))
//				loc_17_6Tot++;
//			else if (pickedTarget == game.getBoard().getCell(17, 8))
//				loc_17_8Tot++;
//			else if (pickedTarget == game.getBoard().getCell(18, 7))
//				loc_18_7Tot++;
//			else if (pickedTarget == game.getBoard().getCell(16, 9))
//				loc_16_9Tot++;
//			else
//				fail("Invalid target selected");
//		}
//		// Ensure we have 200 total selections (fail should also ensure)
//		assertEquals(200, loc_15_6Tot +
//				loc_15_8Tot +
//				loc_14_7Tot +
//				loc_17_6Tot +
//				loc_17_8Tot +
//				loc_18_7Tot +
//				loc_16_9Tot);
//		// Ensure each target was selected more than once
//		assertTrue(loc_15_6Tot > 10);
//		assertTrue(loc_15_8Tot > 10);
//		assertTrue(loc_14_7Tot > 10);
//		assertTrue(loc_17_6Tot > 10);
//		assertTrue(loc_17_8Tot > 10);
//		assertTrue(loc_18_7Tot > 10);
//		assertTrue(loc_16_9Tot > 10);
//	}
//
//	@Test
//	public void testTargetRandomSelectionWithVisitedRoom() {
//
//		// Place a player at 16, 7
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(4, 4));
//		testPlayer.setLastRoomVisited('R');
//
//		// Create target list
//		game.getBoard().startTargets(testPlayer.getLocation(), 1);
//
//		Set<BoardCell> testTargets = game.getBoard().getTargets();
//
//		int loc_4_3Tot = 0;
//		int loc_5_4Tot = 0;
//		int loc_4_5Tot = 0;
//
//		// Run the test 200 times
//		for (int i=0; i<200; i++) {
//			BoardCell pickedTarget = testPlayer.pickTarget(testTargets);
//			if (pickedTarget == game.getBoard().getCell(4, 3))
//				loc_4_3Tot++;
//			else if (pickedTarget == game.getBoard().getCell(5, 4))
//				loc_5_4Tot++;
//			else if (pickedTarget == game.getBoard().getCell(4, 5))
//				loc_4_5Tot++;
//			else
//				fail("Invalid target selected");
//		}
//		// Ensure we have 200 total selections (fail should also ensure)
//		assertEquals(200, loc_4_3Tot +
//				loc_5_4Tot +
//				loc_4_5Tot);
//		// Ensure each target was selected more than once
//		assertTrue(loc_4_3Tot > 10);
//		assertTrue(loc_5_4Tot > 10);
//		assertTrue(loc_4_5Tot > 10);
//	}
//
//	@Test
//	public void testDisproveOnePlayerOneMatch() {
//
//		// Setup
//		Card c = null;
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(0, 0));
//		testPlayer.getCards().add(rope);
//		testPlayer.getCards().add(knife);
//		testPlayer.getCards().add(white);
//		testPlayer.getCards().add(mustard);
//		testPlayer.getCards().add(study);
//		testPlayer.getCards().add(library);
//
//		// Test a match with a weapon
//		c = testPlayer.disproveSuggestion("g", "g", "Rope");
//		assertEquals("Rope", c.getName());
//
//		// Match with person
//		c = testPlayer.disproveSuggestion("Mrs. White", "g", "g");
//		assertEquals("Mrs. White", c.getName());
//
//		// Match with room
//		c = testPlayer.disproveSuggestion("g", "Study", "g");
//		assertEquals("Study", c.getName());
//
//		// Returns null
//		c = testPlayer.disproveSuggestion("g", "g", "g");
//		assertEquals(null, c);
//	}
//
//	@Test
//	public void testDisproveOnePlayerMultipleMatches() {
//
//		// Setup
//		Card c = null;
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(0, 0));
//		testPlayer.getCards().add(rope);
//		testPlayer.getCards().add(knife);
//		testPlayer.getCards().add(white);
//		testPlayer.getCards().add(mustard);
//		testPlayer.getCards().add(study);
//		testPlayer.getCards().add(library);
//
//		int whiteTot = 0;
//		int studyTot = 0;
//		int ropeTot = 0;
//
//		// loop to ensure a random card is selected
//		for (int i=0; i<200; i++) {
//			c = testPlayer.disproveSuggestion("Mrs. White", "Study", "Rope");
//			if (c.getName().equals("Mrs. White"))
//				whiteTot++;
//			else if (c.getName().equals("Study"))
//				studyTot++;
//			else if (c.getName().equals("Rope"))
//				ropeTot++;
//			else
//				fail("Invalid target selected");
//		}
//
//
//		assertTrue(whiteTot > 10);
//		assertTrue(studyTot > 10);
//		assertTrue(ropeTot > 10);
//	}
//
//	@Test
//	public void testAllPlayersQueried(){
//
//		Card c = null;
//		game.getHuman().getCards().add(rope);
//		game.getComputers().get(0).getCards().add(knife);
//		game.getComputers().get(1).getCards().add(white);
//		game.getComputers().get(2).getCards().add(mustard);
//		game.getComputers().get(3).getCards().add(study);
//		game.getComputers().get(4).getCards().add(library);
//
//		// Ensure null
//		c = game.handleSuggestion("g", "g", "g", game.getHuman());
//		assertEquals(null, c);
//
//		// Test for human
//		c = game.handleSuggestion("g", "g", "Rope", game.getComputers().get(0));
//		assertEquals("Rope", c.getName());
//
//		// Person suggesting is the only one that can disprove it
//		c = game.handleSuggestion("g", "g", "Knife", game.getComputers().get(0));
//		assertEquals(null, c);
//
//		// Two possible answers, make sure first is returned
//		c = game.handleSuggestion("Mrs. White", "g", "Knife", game.getComputers().get(3));
//		assertEquals("Knife", c.getName());
//
//		// Make sure all are queried
//		c = game.handleSuggestion("g", "Library", "g", game.getHuman());
//		assertEquals("Library", c.getName());
//
//
//
//	}
//
//	@Test
//	public void testCreateSuggestionOnlyOnePossible() {
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(17, 20));
//		ArrayList<Card> tempCards = new ArrayList<Card>(game.getPlayerCards());
//
//		for (int i = 1; i < tempCards.size(); i++) {
//			testPlayer.updateSeen(tempCards.get(i));
//		}
//
//		String suggestionPerson = tempCards.get(0).getName();
//
//		tempCards.clear();
//		tempCards = new ArrayList<Card>(game.getWeaponCards());
//		for (int i = 1; i < tempCards.size(); i++) {
//			testPlayer.updateSeen(tempCards.get(i));
//		}
//
//		String suggestionWeapon = tempCards.get(0).getName();
//
//		Solution s = testPlayer.createSuggestion(game.getPlayerCards(), game.getWeaponCards(), game.getBoard());
//
//		assertEquals(suggestionPerson, s.getPerson());
//		assertEquals(suggestionWeapon, s.getWeapon());
//		assertEquals("Dining Room", s.getRoom());
//
//	}
//
//	@Test
//	public void testCreateSuggestionLotsPossible() {
//		ComputerPlayer testPlayer = new ComputerPlayer("test", "white", game.getBoard().calcIndex(17, 20));
//		ArrayList<Card> tempCards = new ArrayList<Card>(game.getPlayerCards());
//
//		for (int i = 2; i < tempCards.size(); i++) {
//			testPlayer.updateSeen(tempCards.get(i));
//		}
//
//		String suggestionPerson1 = tempCards.get(0).getName();
//		String suggestionPerson2 = tempCards.get(1).getName();
//
//		tempCards.clear();
//		tempCards = new ArrayList<Card>(game.getWeaponCards());
//		for (int i = 2; i < tempCards.size(); i++) {
//			testPlayer.updateSeen(tempCards.get(i));
//		}
//
//		String suggestionWeapon1 = tempCards.get(0).getName();
//		String suggestionWeapon2 = tempCards.get(1).getName();
//
//		int p1Tot = 0;
//		int p2Tot = 0;
//		int w1Tot = 0;
//		int w2Tot = 0;
//		Solution s = null;
//
//		for (int i = 0; i < 200; i++) {
//			s = testPlayer.createSuggestion(game.getPlayerCards(), game.getWeaponCards(), game.getBoard());
//			if (s.getPerson().equals(suggestionPerson1))
//				p1Tot++;
//			else if (s.getPerson().equals(suggestionPerson2))
//				p2Tot++;
//			else
//				fail("Invalid suggestion");
//			if (s.getWeapon().equals(suggestionWeapon1))
//				w1Tot++;
//			else if (s.getWeapon().equals(suggestionWeapon2))
//				w2Tot++;
//			else
//				fail("Invalid suggestion");
//		}
//
//		assertTrue(p1Tot > 10);
//		assertTrue(p2Tot > 10);
//		assertTrue(w1Tot > 10);
//		assertTrue(w2Tot > 10);
//	}
//}

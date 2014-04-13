//package clueTests;
//
//import static org.junit.Assert.*;
//
//import java.awt.Color;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import clue.Card;
//import clue.Card.cardType;
//import clue.ClueGame;
//import clue.ComputerPlayer;
//import clue.Solution;
//
//public class GameSetupTests {
//
//	private static ClueGame game;
//
//	@Before
//	public void setupClass() {
//		game = new ClueGame("CluePlayers.txt", "ClueLayout.csv", "ClueLegend.txt", "ClueWeapons.txt");
//	}
//
//	@Test
//	public void playerLoadingTest() {
//
//		// Test human
//		assertEquals(game.getHuman().getName(), "Mrs. White");
//		assertEquals(game.getHuman().getColor(), Color.white);
//		assertEquals(game.getHuman().getLocation(), game.getBoard().calcIndex(11, 13));
//
//		// Check computer array
//		assertEquals(game.getComputers().size(), 5);
//
//		assertEquals(game.getComputers().get(0).getName(), "Miss Scarlett");
//		assertEquals(game.getComputers().get(0).getColor(), Color.red);
//		assertEquals(game.getComputers().get(0).getLocation(), game.getBoard().calcIndex(6, 6));
//
//		assertEquals(game.getComputers().get(4).getName(), "Professor Plum");
//		assertEquals(game.getComputers().get(4).getColor(), Color.magenta);
//		assertEquals(game.getComputers().get(4).getLocation(), game.getBoard().calcIndex(20, 5));
//	}
//
//	@Test
//	public void loadCardsTest() {
//
//		// Check that the deck contains the correct number of cards
//		assertEquals(21, game.getCards().size());
//
//		// Check for the correct number of cards of each type
//		int weaponCount = 0;
//		int roomCount = 0;
//		int personCount = 0;
//
//		for (Card c : game.getCards()) {
//			if (c.getType() == cardType.PERSON) {
//				personCount++;
//			}
//			if (c.getType() == cardType.ROOM) {
//				roomCount++;
//			}
//			if (c.getType() == cardType.WEAPON) {
//				weaponCount++;
//			}
//		}
//
//		assertEquals(6, personCount);
//		assertEquals(6, weaponCount);
//		assertEquals(9, roomCount);
//
//		// Check a room, weapon, and person
//
//		assertTrue(game.getCards().contains(new Card(cardType.WEAPON, "Candlestick")));
//		assertTrue(game.getCards().contains(new Card(cardType.ROOM, "Study")));
//		assertTrue(game.getCards().contains(new Card(cardType.PERSON, "Colonel Mustard")));
//
//
//	}
//
//
//	@Test
//	public void dealCardsTest() {
//
//		game.deal();
//
//		// Check that all cards are dealt
//		assertEquals(0, game.getCards().size());
//
//		// All players have the similar number of cards. Compares the size of the human hand with each computers' hand; not
//		// exhaustive, but sufficient
//		for (ComputerPlayer c: game.getComputers()) {
//			assertTrue(Math.abs(game.getHuman().getCards().size() - c.getCards().size()) < 2);
//		}
//
//		// Compare every computers' hand with the humans hand. Again, not exhaustive, but sufficient
//		for (ComputerPlayer c: game.getComputers()) {
//			for (Card card : c.getCards()) {
//				assertFalse(game.getHuman().getCards().contains(card));
//			}
//		}
//
//	}
//
//	@Test
//	public void accusationTest() {
//		game.setSolution(new Solution("Colonel Mustard", "Candlestick", "Study"));
//
//		// Should pass
//		assertTrue(game.checkAccusation(new Solution("Colonel Mustard", "Candlestick", "Study")));
//
//		// Wrong person
//		assertFalse(game.checkAccusation(new Solution("Mrs. White", "Candlestick", "Study")));
//
//		// Wrong weapon
//		assertFalse(game.checkAccusation(new Solution("Colonel Mustard", "Revolver", "Study")));
//
//		// Wrong room
//		assertFalse(game.checkAccusation(new Solution("Colonel Mustard", "Candlestick", "Dining Room")));
//	}
//
//}

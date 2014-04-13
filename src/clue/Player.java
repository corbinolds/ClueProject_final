package clue;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.Field;

import clue.Card.cardType;

public class Player {
	private String name = "";
	private ArrayList<Card> cards;
	private Color color;
	protected int location;
	private Random rand = new Random();
	public Player (){

    }
	public Player(String name, String color, int location) {
		this.name = name;
		
		try {
		    Field field = Class.forName("java.awt.Color").getField(color);
		    this.color = (Color)field.get(null);
		} catch (Exception e) {
		    this.color = null; // Not defined
		}
		
		this.location = location;
		cards = new ArrayList<Card>();
	}
	
	public Card disproveSuggestion(String person, String room, String weapon) {
		Card personName = new Card(cardType.PERSON, person);
		Card roomName = new Card(cardType.ROOM, room);
		Card weaponName = new Card(cardType.WEAPON, weapon);
		
		ArrayList<Card> matches = new ArrayList<Card>();
		
		for (Card c : cards) {
			if (c.equals(personName) || c.equals(roomName) || c.equals(weaponName)) {
				matches.add(c);
			}
		}
		
		if (!matches.isEmpty()) {
			int i = 0;
			int index = rand.nextInt(matches.size());
			for (Card c : matches) {
				if (i == index) {
					return c;
				}
				i++;
			}
		}
		
		return null;
		
	}

	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getLocation() {
		return location;
	}

    public void setLocation(int location){
        this.location = location;

    }

	public ArrayList<Card> getCards() {
        System.out.println(cards);
		return cards;

	}

    public void addCard (Card myCard){

        cards.add(myCard);
    }

	public void draw(Graphics g, int row, int col) {
		g.setColor(color);
		g.fillOval(col * ClueGame.cellSize, row * ClueGame.cellSize, ClueGame.cellSize, ClueGame.cellSize);
		
	}


}

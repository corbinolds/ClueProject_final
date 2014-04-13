package clue;

public class Card {

	public enum cardType {PERSON, WEAPON, ROOM};
	private String name;
	private cardType type;

	public Card(cardType type, String name) {
		this.name = name;
		this.type = type;
    }
    public Card (){

    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public cardType getType() {
		return type;
	}

	public void setType(cardType type) {
		this.type = type;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}

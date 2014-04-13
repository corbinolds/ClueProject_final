package clue;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class NotesPanel extends JDialog {

	private static final long serialVersionUID = 1L;
	private final ClueGame owner;

    /**
     * Constructor that does all the constructor work
     * @param owner
     */
	public NotesPanel(ClueGame owner) {
		this.owner = owner;
		
		setSize(500, 700);
		setTitle("Notes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set default close behavior
		
		// Create main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); //top, left, bottom, right  border
		mainPanel.setLayout(new GridLayout(3, 2, 10, 10));//rows, cols, horizontal gap, vertical gap
		
		// Load data into collections
		Collection<String> peopleNames = new HashSet<String>();
		HashSet<String> roomNames =  new HashSet<String>(this.owner.getBoard().getRooms().values());
		Collection<String> weaponNames = new HashSet<String>();
		
		for (Card c : this.owner.getPlayerCards()) {
			peopleNames.add(c.getName());
		}
		
		for (Card c : this.owner.getWeaponCards()) {
			weaponNames.add(c.getName());
		}
		
		// Clean room collection
		roomNames.remove("Walkway");
		roomNames.remove("Closet");
		
		JPanel peopleSeenPanel = createSeenPanel(peopleNames, "People Seen");
		JPanel roomsSeenPanel = createSeenPanel(roomNames, "Rooms Seen");
		JPanel weaponsSeenPanel = createSeenPanel(weaponNames, "Weapons Seen");
		
		JPanel peopleGuessPanel = createGuessPanel(peopleNames, "Person Best Guess");
		JPanel roomsGuessPanel = createGuessPanel(roomNames, "Room Best Guess");
		JPanel weaponsGuessPanel = createGuessPanel(weaponNames, "Weapon Best Guess");

		mainPanel.add(peopleSeenPanel);
		mainPanel.add(peopleGuessPanel);

		mainPanel.add(roomsSeenPanel);
		mainPanel.add(roomsGuessPanel);
		
		mainPanel.add(weaponsSeenPanel);
		mainPanel.add(weaponsGuessPanel);
		
		
		add(mainPanel);
		
	}
	
	public JPanel createGuessPanel(Collection<String> items, String name) {
		JPanel panel = new JPanel(new GridLayout(0,1));
		
		JComboBox<String> combo = new JComboBox<String>();
		for (String s : items) {
			combo.addItem(s);
		}
		
		panel.add(combo);
		panel.setBorder(new TitledBorder(new EtchedBorder(), name));
		return panel;
	}
	
	public JPanel createSeenPanel(Collection<String> items, String name) {
		JPanel panel = new JPanel(new GridLayout(0, 2));


        for (String s : items) {
			panel.add(new JCheckBox(s));
		}

		panel.setBorder(new TitledBorder(new EtchedBorder(), name));
		
		return panel;
	}

}

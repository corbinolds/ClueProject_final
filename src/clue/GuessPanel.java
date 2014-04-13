package clue;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GuessPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private final ClueGame owner;
    private JComboBox<String> personCombo;
    private JComboBox<String> weaponCombo;
    private JComboBox<String> roomCombo;
    /**
     * Constructor that does all the constructor work
     * @param owner
     */
	public GuessPanel(ClueGame owner) {
		this.owner = owner;
        personCombo = new JComboBox<String>();
        weaponCombo = new JComboBox<String>();
        roomCombo = new JComboBox<String>();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Make an Accusation");
		setSize(300, 400);
		createLayout();
		
	}
		
	public void createLayout(){	
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); //top, left, bottom, right  border
		mainPanel.setLayout(new GridLayout(4, 2, 0, 0));//rows, cols, horizontal gap, vertical gap
		
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
		
		JPanel peopleGuessPanel = createGuessPanel(peopleNames, personCombo);
		JPanel roomsGuessPanel = createGuessPanel(roomNames, roomCombo);
		JPanel weaponsGuessPanel = createGuessPanel(weaponNames, weaponCombo);
		JLabel nameLabel = createLabelPanel("Person");
		JLabel weaponLabel = createLabelPanel("Weapon");
		JLabel roomLabel = createLabelPanel("Room");
		
		JButton submitButton = new JButton("Submit");
        submitButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	//make guess
                    String personChosen=(String)personCombo.getSelectedItem();
                    String roomChosen=(String)roomCombo.getSelectedItem();
                    String weaponChosen=(String)weaponCombo.getSelectedItem();
                    Solution maybeSolution = new Solution(personChosen, weaponChosen, roomChosen);
                    JButton j = new JButton("d");
                    if( owner.getSolution().equals(maybeSolution))
                    {



                        owner.endGame(owner.getHuman(), maybeSolution);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(j, "Your accusation was incorrect.", "Sorry!", JOptionPane.INFORMATION_MESSAGE);
                        owner.getHuman().setHasMoved(true);
                        dispose();



                    }


         
                }
            });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	dispose();
                }
            });
		
		mainPanel.add(nameLabel);
		mainPanel.add(peopleGuessPanel);

		mainPanel.add(roomLabel);
		mainPanel.add(roomsGuessPanel);
		
		mainPanel.add(weaponLabel);
		mainPanel.add(weaponsGuessPanel);
		
		mainPanel.add(submitButton);
		mainPanel.add(cancelButton);
	
		
		add(mainPanel);
	}
	
	public JPanel createGuessPanel(Collection<String> items, JComboBox combo) {
		JPanel panel = new JPanel(new GridLayout(0,1));

		for (String s : items) {
			combo.addItem(s);
		}
		
		panel.add(combo);

		return panel;
	}
	
	public JLabel createLabelPanel(String name){
		JLabel nameLabel = new JLabel(name);
		return nameLabel;
	}
}
	
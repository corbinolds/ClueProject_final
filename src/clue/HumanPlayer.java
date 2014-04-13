package clue;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HumanPlayer extends Player {
    Board myBoard;
    private boolean hasMoved;
    private ClueGame myGame;
    
	public HumanPlayer(String name, String color, int location, Board b, ClueGame myGame) {

		super(name, color, location);
        myBoard = b;
        hasMoved = false;
        this.myGame = myGame;

	}
    public void makeTurn (int roll, Player current){
        //if can make accusation, make one
        if(canMakeAccusation()==true){
            makeAccusation();

        }
        else{
        makeMove(roll, current);
        }
    }

    public void makeMove(int roll, Player current){

        if(roll != -1){
        myBoard.startTargets(location, roll);
        setHasMoved(false);
        myBoard.highlightTargets();
        }

    }

    //only made when the player has sufficient enough information
    //to guess the solution
    public boolean canMakeAccusation(){
        return false;

    }
    public void makeAccusation (){
        //a dialog is prompted
        //the three components of a card are gathered
        //check if the accusation is correct
        Solution myAccusation = getAccusationUser();

    }
    public Solution getAccusationUser (){
        JDialog myDialog = new JDialog();
        JPanel person = makePanel("Person", "");
        JPanel weapon = makePanel("Weapon", "");
        JPanel location = makePanel("Room", "");
        myDialog.add(person);
        myDialog.add(weapon);
        myDialog.add(location);
        return  null;
    }


    public JPanel makePanel(String borderName, String textBoxName){
        JPanel newPanel = new JPanel(new GridLayout(0, 2, 5, 0));
        JLabel rollLabel = new JLabel(borderName + ":");
       JTextField field = new JTextField("");



        field.setEditable(true);

        newPanel.add(rollLabel);
        newPanel.add(field);


        return newPanel;
    }
    public void setHasMoved(boolean b){
    	hasMoved = b;
    }
    
    public boolean getHasMoved(){
		return hasMoved;
	}
    

}
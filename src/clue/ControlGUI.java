package clue;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class ControlGUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTextField field;
	private JButton nextPlayerButton;
	private JButton accusationButton;
    private JTextField guess;
    private JTextField guessResult;
    private JTextField roll;
    private JTextField whoseTurn;
    private JPanel controlPanel;
    private Font myFont;
    private int rollNum;
   	private Board myBoard;
    private ClueGame myClueGame;
	public ControlGUI(ClueGame cg, int num) {
		setSize(600, 500);
        myFont = new Font("I don't always act like a descriptive name, but when I do, I am descriptive...",
        Font.ROMAN_BASELINE, 8);
		createLayout();
        myClueGame = cg;
        rollNum = num;

	}
	public void createLayout() {

		//controlPanel = new JPanel();

		 controlPanel = new JPanel();
	///	controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		controlPanel.setLayout(new GridLayout(2, 3));
		
		// Whose turn
		JPanel turnGroup = new JPanel();
		turnGroup.setLayout(new GridLayout(2,1));


		JLabel turnLabel = new JLabel("Whose Turn?");
		whoseTurn = new JTextField();


		
		// Buttons

       // nextPlayerButton.addMouseListener(buttonListener);

        nextPlayerButton = new JButton("Next Player");
        nextPlayerButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                if(myClueGame.getCurrentPlayer() instanceof HumanPlayer){

                	if(myClueGame.getHuman().getHasMoved() == false){
                		JButton j = new JButton("d");
                		JOptionPane.showMessageDialog(j, "You need to move before you can go to next player!", "Sorry!", JOptionPane.ERROR_MESSAGE);
                	}
                	else if(myClueGame.getHuman().getHasMoved() == true){
                		myClueGame.nextPlayer();
                		myClueGame.takeTurn();



                	}
                }
                else{
                	myClueGame.nextPlayer();
            		myClueGame.takeTurn();
                    }


                }
            });



		accusationButton = new JButton("Make an Accusation");

		accusationButton.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				//if accusation is clicked and is turn
				if((myClueGame.getCurrentPlayer() instanceof HumanPlayer) && 
						(myClueGame.getHuman().getHasMoved() == false)){
					//guess panel
					GuessPanel guessPanel = new GuessPanel(myClueGame);
					guessPanel.setVisible(true);
						
				}
				//if accusation is clicked and isnt turn
				else{
					JButton j = new JButton("d");
            		JOptionPane.showMessageDialog(j, "It isn't your turn!", "Sorry!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});


		
		// Adds
		controlPanel.add(turnGroup);
		controlPanel.add(nextPlayerButton);
		controlPanel.add(accusationButton);
        JPanel guessP = makePanel("Guess", "Guess");
        JPanel rollP = makePanel("Die", "Roll");
        JPanel guessResultP  = makePanel("Guess Result", "Response");
        JPanel whoseTurnP = makePanel("Whose Turn", "");
        JPanel panelSpacer = new JPanel();

        JTextPane spacer= new JTextPane();
        // JTextPane.setEditable
        JScrollPane scrollPane = new JScrollPane(spacer);
       panelSpacer.add(scrollPane);

		controlPanel.add(rollP);
		controlPanel.add(guessP);
		controlPanel.add(guessResultP);
        controlPanel.add(whoseTurnP);
       // controlPanel.add(panelSpacer);
		
		add(controlPanel);
	}
    public void updateGuessResult (String guessText, String responseText){
        Point guessLocation = guess.getLocation();
        guess.setText(guessText);
        guess.setFont(myFont);
        Dimension d = guess.getPreferredSize();
       d.setSize(d.getWidth()+100, d.getHeight());
      guess.setSize(d);

       // controlPanel.remove(guess);
        //guess.setLocation(guessLocation);
      //  controlPanel.add(guess);
        controlPanel.setSize(controlPanel.getPreferredSize());
     //   controlPanel.putComp
      //  controlPanel.add(guess);
       //System.out.println(d.getWidth());
        guessResult.setText(responseText);
        guessResult.setFont(myFont);
      d = guessResult.getPreferredSize();

      //  System.out.println(d.getWidth());
   //     guessResult.setPreferredSize(d);
   }
   public void updateRoll (int chocolateRoll){
       roll.setText(Integer.toString(chocolateRoll));
       roll.setFont(myFont);
       ///Dimension d = roll.getPreferredSize();
       //roll.setPreferredSize(d);

   }
    public  void updateWhoseTurn (String turn){
        whoseTurn.setText(turn);
        whoseTurn.setFont(myFont); //repetition, repetition
        ///Dimension d = whoseTurn.getPreferredSize();
        //whoseTurn.setPreferredSize(d);
    }
	//makes a new panel with a border, text box, and a label for the textbox
	public JPanel makePanel(String borderName, String textBoxName){

		JPanel newPanel = new JPanel(new GridLayout(0, 2, 5, 0));
        JLabel rollLabel;
        if(!textBoxName.equals(""))
             rollLabel = new JLabel(textBoxName + ":");
        else{
            rollLabel = new JLabel();
        }

        if(textBoxName.equals("Roll")){
          //  System.out.print(myClueGame.myRoll);
            roll = new JTextField();
            roll.setEditable(false);

            newPanel.add(roll);
         //   newPanel.add(rollLabel);
        }
        else if (textBoxName.equals("Guess")){
            guess= new JTextField();
            guess.setEditable(false);

          //  newPanel.add(rollLabel);
            newPanel.add(guess);
        }
        else if(textBoxName.equals("")){
            whoseTurn = new JTextField();
            whoseTurn.setEditable(false);
            //JScrollPane scrollPane = new JScrollPane();
            //newPanel.add(rollLabel);
            newPanel.add(whoseTurn);

        }
        else{
            guessResult = new JTextField();
            guessResult.setEditable(false);
            //newPanel.add(rollLabel);
            newPanel.add(guessResult);
        }



		newPanel.setBorder(new TitledBorder(new EtchedBorder(), borderName));
		
		return newPanel;
	}
    public void setRollNum(int num){
        rollNum = num;
    }

	public static void main(String[] args) {

	}

}

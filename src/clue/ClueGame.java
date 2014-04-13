package clue;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import clue.Card.cardType;

public class ClueGame extends JFrame {

    private static final long serialVersionUID = 1L;
    private ArrayList<Card> cards;
    private ArrayList<Card> weaponCards;
    private ArrayList<Card> playerCards;
    private ArrayList<ComputerPlayer> computers;
    private HumanPlayer human;
    private Solution solution;
    private Board board;
    private String playerFile;
    private String weaponFile;
    public static final int cellSize = 30;

    private Player currentPlayer;
    public int myRoll;//public, I know
    private JMenuBar menuBar;
    private ControlGUI myControl;

    public ClueGame(String playerFile, String layoutFile, String legendFile, String weaponFile) {
        //for loading in the board information
        this.playerFile = playerFile;
        this.weaponFile = weaponFile;

        //actually loading all the information into the Board
        board = new Board(layoutFile, legendFile, this);
        board.loadConfigFiles();

        //instantiating variables for the game
        computers = new ArrayList<ComputerPlayer>();
        cards = new ArrayList<Card>();
        weaponCards = new ArrayList<Card>();
        playerCards = new ArrayList<Card>();

        //loading player and card information
        loadConfigFiles();
        deal(); //now the deck ('cards') should contain zero
        currentPlayer = computers.get(4);
        myRoll = -1;

        //sets up the gui
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Clue");
        setSize((board.getNumColumns()+8) * cellSize, (board.getNumRows()+8) * cellSize);

        //menu for closing and looking at notes
        menuBar = new JMenuBar();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(createFileMenu());

        //adds human cards
        JPanel humanCards = createMyCards();
        add(humanCards, BorderLayout.EAST);

        add(board, BorderLayout.CENTER);
        myControl = new ControlGUI(this, -1);
        add(myControl, BorderLayout.SOUTH);



    }

    //default constructor
    public ClueGame() {

    }

    private JPanel createMyCards(){
        ArrayList<ArrayList<String>> organizedCards = organizeHumanCards();
        JPanel masterPanel = new JPanel(new GridLayout(0,1));
        masterPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));

        //add the 3 [] [] [] panels
        for(int i =0; i<3; i++){
            JPanel panelPeople = new JPanel(new GridLayout(1,1));
            JPanel panelWeapons = new JPanel(new GridLayout(1,1));
            JPanel panelRooms = new JPanel(new GridLayout(1,1));
            if(i==0){
                panelPeople.setBorder(new TitledBorder(new EtchedBorder(), "Persons"));

                for (String s : organizedCards.get(i)) {

                    JTextField temp = new JTextField(s);
                    temp.setEditable(false);
                    panelPeople.add(temp);
                }


            }
            if(i==1){
                panelWeapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
                for (String s : organizedCards.get(i)) {
                    JTextField temp = new JTextField(s);
                    temp.setEditable(false);
                    panelWeapons.add(temp);
                }



            }
            if(i==2){

                panelRooms.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
                for (String s : organizedCards.get(i)) {
                    JTextField temp = new JTextField(s);
                    temp.setEditable(false);
                    panelRooms.add(temp);
                }
            }
            masterPanel.add(panelPeople, BorderLayout.EAST);
            masterPanel.add(panelWeapons, BorderLayout.EAST);
            masterPanel.add(panelRooms, BorderLayout.EAST);
        }
        return masterPanel;
    }

    //Organizes human cards so that displaying them on the GUI is simpler
    public ArrayList<ArrayList<String>> organizeHumanCards(){
        ArrayList<Card> myCards = human.getCards();

        ArrayList<ArrayList<String>> organizedCards = new ArrayList<ArrayList<String>>();
        ArrayList<String> personsNames = new ArrayList<String>();
        ArrayList<String> weaponsNames = new ArrayList<String>();
        ArrayList<String> roomsNames = new ArrayList<String>();
        for(Card c : myCards){

            if(c.getType().equals(cardType.PERSON)){
                personsNames.add(c.getName());
            }
            if(c.getType().equals(cardType.WEAPON)){
                weaponsNames.add(c.getName());
            }
            if(c.getType().equals(cardType.ROOM)){
                roomsNames.add(c.getName());
            }
        }

        organizedCards.add(personsNames);
        organizedCards.add(weaponsNames);
        organizedCards.add(roomsNames);
        return organizedCards;
    }
    //makes menu in gui
    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(createFileNotesItem());
        menu.add(createFileExitItem());
        return menu;
    }

    //makes exit option in the menu
    private JMenuItem createFileExitItem() {
        JMenuItem item = new JMenuItem("Exit");
        class MenuItemExitListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }
        item.addActionListener(new MenuItemExitListener());
        return item;
    }

    //make notes option in menu
    private JMenuItem createFileNotesItem() {
        final ClueGame g = this;
        JMenuItem item = new JMenuItem("Notes");
        class MenuItemNotesListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                NotesPanel notes = new NotesPanel(g);
                notes.setVisible(true);
            }
        }
        item.addActionListener(new MenuItemNotesListener());
        return item;
    }

    //selects random cards for a solution, then deals out cards to all players, leaving 0 in the deck
    public void deal() {
        Random rand = new Random();
        createSolution();
        Card c;
        while (!cards.isEmpty()) {
            int randIndex = Math.abs(rand.nextInt(cards.size()));
            c = cards.get(randIndex);
            human.addCard(c);
            cards.remove(c);
            for (ComputerPlayer p : computers) {
                if (!cards.isEmpty()) {
                    c = cards.get(rand.nextInt(cards.size()));
                    p.addCard(c);
                    cards.remove(c);
                }
            }
        }
    }


    public void createSolution() {
        // puts cards into separate arraylists so that a card from each list can
        // be chosen at random for the solution
        ArrayList<Card> weapons = new ArrayList<Card>();
        ArrayList<Card> rooms = new ArrayList<Card>();
        ArrayList<Card> people = new ArrayList<Card>();
        System.out.println ("size: " + cards.size());

        //orgainizes cards, again
        for (Card c : cards) {
            if (c.getType() == cardType.PERSON) {
                people.add(c);
            }
            if (c.getType() == cardType.WEAPON) {
                weapons.add(c);
            }
            if (c.getType() == cardType.ROOM) {
                rooms.add(c);
            }
        }
        Random rand = new Random();
        int randPeople = Math.abs(rand.nextInt(people.size()));
        System.out.println("weapons size " + weapons.size());
        int randWeapons = Math.abs(rand.nextInt(weapons.size()));
        int randRoom =Math.abs(rand.nextInt(rooms.size()));

        solution = new Solution(
                people.get(randPeople).getName(),
                weapons.get(randWeapons).getName(),
                rooms.get(randRoom).getName()
        );
        System.out.println(solution.getPerson() +  " " + solution.getRoom() + "  " + solution.getWeapon());
        //removes the solution cards from the deck of cards so that they cant be dealt
        cards.remove(new Card(cardType.PERSON, solution.getPerson()));
        cards.remove(new Card(cardType.WEAPON, solution.getWeapon()));
        cards.remove(new Card(cardType.ROOM, solution.getRoom()));

    }

    public Card getCard(String name){
        //iterate through the entire deck of cards to find name
        //there likely is room for efficiency
        for(Card c: cards){
            if(c.getName().equals(name))
                return c;
        }
        Card aCard = new Card();
        return  aCard;

    }


    //loads player, room, and weapon cards and creates the players
    public void loadConfigFiles() {
        try {
            loadPlayers();
            loadWeapons();
            loadRooms();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    public void checksValidTarget (Point p){

        System.out.println("x then y " + p.x + " " + p.y);

        //checks validity
        Boolean valid = false;

        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();


        for(BoardCell bc : board.getTargets()){


            rectangles.add(new Rectangle(((bc.getColumn()) *cellSize) + 10, ((bc.getRow() + 1)*cellSize) + 25,cellSize, cellSize));
            System.out.println("Row, then col: " + bc.getColumn() *cellSize+ " "+ bc.getRow()*cellSize);

            // rectangles.add(rect);

        }
        for(Rectangle rect : rectangles){
            if  (rect.contains(p.x,p.y)){
                valid = true;
                human.setLocation((((rect.y/cellSize) - 1) * 23) + (rect.x/cellSize));
                human.setHasMoved(true);
                board.unHighlightTargets();

                board.repaint(); //key point here, the board is being repainted
                return;
            }
         }



    if (valid!=true){
        JButton temp = new JButton("temp");
        JOptionPane.showMessageDialog(temp, "Must chose a valid location", "Location Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
}
    public void  takeTurn (){

        if(currentPlayer.getName().equals("Dino Grey"))  {

            HumanPlayer humanPlayer = (HumanPlayer)currentPlayer;
            myRoll = board.roll();
            updateRollDisplay(myRoll);
            humanPlayer.makeTurn(myRoll, currentPlayer);

            board.repaint();
        }
        else{
            ComputerPlayer computerPlayer = (ComputerPlayer) currentPlayer;
            myRoll = board.roll();
            computerPlayer.makeTurn(myRoll, currentPlayer);
            updateRollDisplay(myRoll);
            board.repaint();


        }
    }

    public void nextPlayer (){


        if(currentPlayer instanceof HumanPlayer){
            currentPlayer = computers.get(0);

        }
        else if(currentPlayer.equals(computers.get(4))){
            currentPlayer = human;
        }
        else if(currentPlayer.equals(computers.get(0))){
            currentPlayer = computers.get(1);

        }
        else if (currentPlayer.equals(computers.get(1))){
            currentPlayer = computers.get(2);
        }
        else if(currentPlayer.equals(computers.get(2))){
            currentPlayer = computers.get(3);
        }
        else{
            currentPlayer = computers.get(4);
        }


        updateWhoseTurn();


    }
    public Card processSuggestion (Solution mySuggestion){
        //move suggested player to room
        Player p = getPlayer(mySuggestion.person);
        RoomCell rc = getRoomCell(mySuggestion.room);
        p.setLocation(board.calcIndex(rc.getRow(), rc.getColumn()));

        //iterate through all players, including self, to see if one or more cards of the solution are present





        ArrayList<Card> presentCards = new ArrayList <Card> ();
        for(ComputerPlayer cp : getComputers()){
            if( cp.disproveSuggestion(mySuggestion.person, mySuggestion.room, mySuggestion.weapon) != null)
                presentCards.add(cp.disproveSuggestion(mySuggestion.person, mySuggestion.room, mySuggestion.weapon));
        }
        if (presentCards.size()<0)
            return null;
        Random rand = new Random();
        int randomCard = rand.nextInt(presentCards.size());

        if(randomCard<0){
            randomCard *= -1;
        }
        Card disprover = presentCards.get(randomCard);
        updateSuggestionDisplay(mySuggestion, disprover);
        return disprover ;



    }
    //given a certain String, return the corresponding player
    public Player getPlayer (String name){
        Player p = new Player();
        for(ComputerPlayer cp : computers){
            if (cp.getName().equals(name))
                p=cp;
        }
        if(human.getName().equals(name))
            p = human;
        return p;
    }
    //returns a room given the title for it
    public RoomCell getRoomCell(String roomName){
        //iterate through the legend map to get initials
        Character myCharacter = new Character(' ');
        RoomCell myRoomCell = new RoomCell();
        Set<Character> initials = board.getRooms().keySet();
        for(Character c : initials){
            if(board.getRooms().get(c).equals(roomName))
                myCharacter = c;

        }


        RoomCell rc = new RoomCell();
        for(BoardCell bc : board.getCells()){
            if(bc instanceof RoomCell) {
                RoomCell tempCell = (RoomCell)bc;
                if(((Character)tempCell.getInitial())==myCharacter)
                    myRoomCell = tempCell;
            }
        }
        return  myRoomCell;

    }
    public void updateSuggestionDisplay(Solution mySuggestion, Card disprover){
        String suggestionText = new String (mySuggestion.person + ", " + mySuggestion.room + ", " + mySuggestion.weapon );
        String disproverText = new String (disprover.getName());
        myControl.updateGuessResult(suggestionText, disproverText);



    }
    public void updateRollDisplay(int chocolateRoll){ //naming variables is difficult, don't judge
        myControl.updateRoll(chocolateRoll);

    }

    public void updateWhoseTurn (){
        myControl.updateWhoseTurn(currentPlayer.getName());
    }
    public void updateSuggestionDisplay(Solution mySuggestion){

    }


    // determines who disproves a suggestion when it is made, and what happens
    // when no one can disprove a suggestion
    public Card handleSuggestion(String person, String room, String weapon, Player suggestingPerson) {
        Card c = null;

        if (!suggestingPerson.equals(human)) {
            c = human.disproveSuggestion(person, room, weapon);
        }

        if (suggestingPerson != human) {
            c = human.disproveSuggestion(person, room, weapon);
        }
        if (c != null) {

            return c;
        }

        for (ComputerPlayer p : computers) {
            if (suggestingPerson != p) {
                c = p.disproveSuggestion(person, room, weapon);
            }

        }

        return c;
    }

    //if someone accuses the correct person, weapon, and room, returns true
    public static boolean checkAccusation(Solution solution) {
        return solution.equals(solution);
    }

    public void endGame(Player winner, Solution actual){
        String personChosen = actual.getPerson();
        String roomChosen = actual.getRoom();
        String weaponChosen = actual.getWeapon();
        String winnerName = winner.getName();
        this.getContentPane().removeAll();
        JButton j = new JButton("d");
        JOptionPane.showMessageDialog(j, "It was " + personChosen+ " in the " + roomChosen + " with the " + weaponChosen, "You got it!", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }


    //creates the players and stores their starting locations, colors, and names
    public void loadPlayers() throws BadConfigFormatException, FileNotFoundException {

        FileReader reader = new FileReader(playerFile);
        Scanner input = new Scanner(reader);
        int row;
        int column;

        while (input.hasNext()) {
            String s = input.nextLine();
            String[] split = s.split(", ");
            if (split.length != 4) {
                input.close();
                throw new BadConfigFormatException("Incorrect format in player configuration: " + split);
            }
            try {
                row = Integer.parseInt(split[2]);
                column = Integer.parseInt(split[3]);
            } catch (NumberFormatException e) {
                input.close();
                throw new BadConfigFormatException("Incorrect format in player configuration: " + split);
            }
            if (split[0].equals("Dino Grey")) {
                human = new HumanPlayer(split[0], split[1], board.calcIndex(row, column), board, this);
                cards.add(new Card(cardType.PERSON, split[0]));
                playerCards.add(new Card(cardType.PERSON, split[0]));
            } else {
                computers.add(new ComputerPlayer(split[0], split[1], board.calcIndex(row, column), board, this));
                cards.add(new Card(cardType.PERSON, split[0]));
                playerCards.add(new Card(cardType.PERSON, split[0]));
            }
        }

        input.close();

    }



    //loads in the weapon names
    public void loadWeapons() throws BadConfigFormatException, FileNotFoundException {

        FileReader reader = new FileReader(weaponFile);
        Scanner input = new Scanner(reader);

        while (input.hasNext()) {
            String weaponName = input.nextLine();


            System.out.println ("WeaponName: "+ weaponName);
            cards.add(new Card(cardType.WEAPON, weaponName));
            weaponCards.add(new Card(cardType.WEAPON, weaponName));
        }

        input.close();

    }

    //loads in the room names
    public void loadRooms() {
        ArrayList<String> rooms = new ArrayList<String>(board.getRooms().values());

        for (String roomName : rooms) {
            if (!roomName.equals("Closet") && !roomName.equals("Walkway")) {
                cards.add(new Card(cardType.ROOM, roomName));
            }
        }
    }

    //makes the game
    public static void main(String[] args) {
        ClueGame game = new ClueGame("CluePlayers.txt", "ClueLayout.csv", "ClueLegend.txt", "ClueWeapons.txt");

        MouseTrigger mouseTrigger = new MouseTrigger(game);
        game.addMouseListener(mouseTrigger);
        game.setVisible(true);

        //splash screen
        JButton j = new JButton("d");
        JOptionPane.showMessageDialog(j, "You are Dino Grey, click the Next Player button to start", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);

    }

    // Used for testing

    public ArrayList<ComputerPlayer> getComputers() {
        return computers;
    }

    public HumanPlayer getHuman() {
        return human;
    }

    public Solution getSolution (){
        return solution;
    }

    public Board getBoard() {
        return board;
    }

    public  ArrayList<Card> getCards() {
        return cards;
    }
    public Player getCurrentPlayer (){
        return currentPlayer;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public ArrayList<Card> getWeaponCards() {
        return weaponCards;
    }

    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

}

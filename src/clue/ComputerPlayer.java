package clue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	private char lastRoomVisited;


	private Set<Card> seenCards = new HashSet<Card>();
    private Board myBoard;
    private ClueGame myGame;
    private boolean hasUnprovenSuggestion;

    private Solution unProvenSuggestion;


	
	public ComputerPlayer(String name, String color, int location, Board myBoard, ClueGame myGame) {
		super(name, color, location);
        this.myBoard = myBoard;
        hasUnprovenSuggestion = false;

        this.myGame = myGame;
        unProvenSuggestion = new Solution("", "","");//attempt to prevent null pointers?


	}

	public BoardCell pickTarget(Set<BoardCell> targets) {
		
		for (BoardCell c : targets) {
			if (c.isDoorway()) {
				RoomCell r = (RoomCell)c;
				if (r.getInitial() != lastRoomVisited) {
					lastRoomVisited = r.getInitial();

					return c;
				}
			}
		}
		
		int i = 0;

        Random rand = new Random();
		int index = rand.nextInt(targets.size()+1)-1;
        if(index<0){
            index *=-1;
        }

		for (BoardCell c : targets) {
			if (i == index) {
				return c;
			}
			i++;
		}
        return new WalkwayCell(0, 0);
		}
    public void makeTurn (int roll, Player current){

        if(canMakeAccusation()==true){
            isAccusationRight();
        }
        else{ makeMove(roll, current);}
    }
    public boolean canMakeAccusation (){
        if(hasUnprovenSuggestion ==true)
            return true;
        System.out.println("Name : "+ getName());
        System.out.println("within canMakeAccusation ");
        return false;
    }
    public void isAccusationRight (){
        if(myGame.checkAccusation(unProvenSuggestion)==true){
            //end game
            myGame.endGame(this, unProvenSuggestion);
        }
     }

    public void makeMove(int roll, Player current)
    {

        myBoard.startTargets(getLocation(), roll);
        BoardCell bc = pickTarget(myBoard.getTargets());
        current.setLocation( myBoard.calcIndex(bc.getRow(), bc.getColumn()));
        processTarget(bc);

    }


    public void processTarget(BoardCell targetCell)
    {
        if(targetCell instanceof RoomCell){
            RoomCell rc = (RoomCell)targetCell;
            lastRoomVisited = rc.getInitial();
            System.out.println("lastRoomVisited: " + lastRoomVisited);

            Solution mySuggestion = createSuggestion(getCards(), myGame.getWeaponCards(), myBoard);
         //   System.out.println("within processTarget: ");

           if( myGame.processSuggestion (mySuggestion) == null){
               hasUnprovenSuggestion = true;
           }
        }

    }

	
	public Solution createSuggestion(ArrayList<Card> playerCards, ArrayList<Card> weaponCards, Board board) {

		ArrayList<Card> tempPlayers = new ArrayList<Card>(playerCards);
		ArrayList<Card> tempWeapons = new ArrayList<Card>(weaponCards);
		
		for (Card c : seenCards) {
			tempPlayers.remove(c);
			tempWeapons.remove(c);
		}

		Random rand = new Random();
       // System.out.println("size: " + tempPlayers.size());
        int randIntPlayers = rand.nextInt(tempPlayers.size());
        if(randIntPlayers<0)
            randIntPlayers *= -1;
        int randIntWeapon = rand.nextInt(tempWeapons.size());
        if(randIntWeapon<0)
            randIntWeapon *=-1;
		RoomCell r = board.getRoomCell(location);
        String playerName =tempPlayers.get(Math.abs(rand.nextInt(tempPlayers.size()))).getName();
        String weaponName = tempWeapons.get(Math.abs(rand.nextInt(tempWeapons.size()))).getName();
        String roomInitial = board.getRoom(r.getInitial());
        System.out.println("playerName: " + playerName);
        System.out.println("weaponName: " + weaponName);
        System.out.println("roomInitial: " +  roomInitial);
		Solution s = new Solution(playerName,weaponName,roomInitial);
        updateSeen(myGame.getCard(playerName));
        updateSeen(myGame.getCard(weaponName));
        updateSeen(myGame.getCard(roomInitial));
        return s;
	}


	public void updateSeen(Card seen) {
		seenCards.add(seen);
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(char lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}

}

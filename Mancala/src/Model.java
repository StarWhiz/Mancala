/**
 * Tai Dao - 11/21/2017 4:00 PM - 5:45 PM
 */
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Model {
	private boolean playerATurn;
	private boolean freeTurn;
	
	private ArrayList<ModelPit> pList;
	private ArrayList<ModelPit> savedPList;
	private ModelPit aPitGoal;
	private ModelPit bPitGoal;
	
	private ChangeEvent event;
	private ArrayList<ChangeListener> listeners;
	
	/**
	 * Initializes each pit with a set number of stones. 
	 * 
	 * Inits goal pits.
	 * Inits listeners.
	 * Makes player A the first player by setting playerAturn to true.
	 * 
	 * @param startingStones
	 */
	Model(int startingStones) {
		pList = new ArrayList<ModelPit> ();
		savedPList = new ArrayList<ModelPit> ();
		for (int i = 0; i <= 11; i ++) {
			pList.add(new ModelPit(startingStones)); //Each Pit will be initialized with 3 or 4 stones and
			//added to an arrayList of Pits
		}
		aPitGoal = new ModelPit(0);
		bPitGoal = new ModelPit(0);
		listeners = new ArrayList<ChangeListener> ();
		playerATurn = true;
		freeTurn = false;
		event = new ChangeEvent(this); // this refers to this Model object...
	}
	
	/**
	 * This saveState should be called at the start of the player's turn. Always save state b4 making a move.
	 */
	public void saveState() {
		savedPList = pList;
	}
	
	/**
	 * If the undo button is pressed this is called.
	 */
	public void undo() {
		pList = savedPList;
		updateViews();
	}

	
	/**
	 * When called updates all views based on the data structure ArrayList<ModelPit> pList
	 */
	public void updateViews() {
		for (ChangeListener cL : listeners) { //notify all listeners that the state has changed.
			cL.stateChanged(event); //model object is passed to the stateChanged functions.
		}
	}
	
	/**
	 * pitPos is retrieved from a pit listener. when a pit is pressed it calls this function with it's position as the parameter
	 * 
	 * This function checks if the selected pit is valid for the current player's turn.
	 * 
	 * @param pitPos
	 */
	public void checkCorrectPitSelected (int pitPos) {
		
		//First check if the pit is empty to begin with....
		if (checkIfSelectedPitIsEmpty(pitPos)) {
			System.out.println("That pit is empty! Select a pit with stones on your side!");
		}
		else { //Then check if the pit belongs to the player... 
			if (playerAsTurn() && pitPos <= 5) {
				saveState(); // saves current state before moving
				System.out.println("Correct Pit Selected");
				moveStonesPlayerA(pitPos);

			}
			else if (!playerAsTurn() && pitPos > 5) {
				System.out.println("Correct Pit Selected");
			}
			else {
				System.out.println("Wrong Pit Selected. Please choose your own Pit.");
			}
			
		}
	}
	
	/**
	 * This function checks if the selected pit doesn't have any stones. If it doesn't have any stones
	 * it returns true.
	 * 
	 * returns true // if no stones
	 */
	public boolean checkIfSelectedPitIsEmpty (int pitPos) {
		int stonesInSelectedPit = pList.get(pitPos).returnStones();
		if (stonesInSelectedPit == 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * This function returns true if it is player A's turn. It returns false if it isn't player A's turn.
	 * 
	 * @return playerATurn
	 */
	public boolean playerAsTurn() {
		return playerATurn;
	}
	
	/**
	 * This function changes the current player's turn to the other player's turn.
	 *
	 */
	public void changePlayerTurns() {
		playerATurn = !playerATurn;
	}
	
	/**
	 * Gets number of stones in current pit and empties it.
	 * 
	 * Move stones according to player A's POV.
	 */
	public void moveStonesPlayerA(int pitPos) {
		int currentPos = pitPos;
		int stonesInHand = pList.get(currentPos).emptyPit(); //Pick up stones in hand... So pit is empty...
		
		while (stonesInHand != 1) { //WHILE WE DON'T Have 1 stone left in our hand....
			currentPos++; //move to next pit

			if (currentPos != 6) { //if current position is not the player B's position...
				pList.get(currentPos).addStone(1); //add 1 stone to that pit
				stonesInHand--; //now we have 1 less stone in our hand
			}
			else {
				aPitGoal.addStone(1); //if current pos is in player B's pit then first... add stone to player A's goal
				stonesInHand--;
				
				if (stonesInHand == 0 ) { //if the number of stones left in hand is 0 and we just added to a goal.. free turn for A
					//Do Free Turn here.
					break; //Exit while loop
				}
				else { // else continue on adding stones to pit until stones in hand is empty
					pList.get(currentPos).addStone(1);
					stonesInHand--;
				}
			}
			//Check if we should capture or not...	
			
			//Check for win condition
			if (winConditionMet()) {
				//Function for  GAME OVER!!! GG
			}
			
			//Check if player still has free turn
			if (freeTurn) {
				//don't change PlayerTurns
			} else {
				changePlayerTurns(); //change PlayerTurns
			}
		}
		//WHILE WE DO Have 1 stone left in our hand....
		if (pList.get(currentPos).isEmpty()){ //if the current selected pit is empty place the last pebble here and capture!
			// TODO perform capture method here....
		}
		// else do nothing and don't capture
	}
	
	/**
	 * Gets number of stones in current pit and empties it.
	 * 
	 * Move stones according to player B's POV.
	 */
	
	public void moveStonesPlayerB(int pitPos) {
		int currentPos = pitPos;
		int stonesInHand = pList.get(pitPos).emptyPit();
		
		while (stonesInHand != 0) {
			currentPos++; //move to next pit
			if (currentPos != 12) { //if current pit position is not out of index
				pList.get(currentPos).addStone(1); //add 1 stone to that pit
				stonesInHand--; //now we have 1 less stone in our hand
			}
			else { // case: pit position is out of index
				bPitGoal.addStone(1); //if current pos is in player B's pit then first... add stone to player B's goal
				stonesInHand--;
				if (stonesInHand == 0 ) { //if the number of stones left in hand is 0 and we just added to a goal.. free turn for B
					freeTurn = true;
					break; //Exit while loop
				}
				else { // else continue on adding stones to pit until stones in hand is empty
					currentPos = 0; // resets current position because we advanced past playerB's goal...
					pList.get(currentPos).addStone(1);
					stonesInHand--;
				}
			}
			//Check if we should capture or not...
			
			
			
			//Check for win condition
			if (winConditionMet()) {
				//Function for  GAME OVER!!! GG
			}
			
			//Check if player still has free turn
			if (freeTurn) {
				//don't change PlayerTurns
			} else {
				changePlayerTurns();
			}
		}
	}
	
	/**
	 * Checks if a win condition has been met yet
	 * 
	 * @return ???
	 */
	public boolean winConditionMet () {
		return false;
	}

}

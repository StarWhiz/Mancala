/**
 * Class that has model of pit
 * @author Tai Doa
 */
public class ModelPit {

	private int stones;
	
	/**
	 * Load the pit up with 3 or 4 initial starting stones....
	 * 
	 * @param s ; initial starting stones
	 */
	public ModelPit(int s) {
		stones = s;
	}
	
	/**
	 * Method to add a stones to a pit
	 * 
	 * @param s ; stones to add
	 */
	public void addStone(int s) {
		stones = stones + s;
	}
	
	/**
	 * Method to set # of stones in pit
	 * 
	 * @param s ; stones to add
	 */
	public void setStones(int s) {
		stones = s;
	}
	
	/**
	 * This function... returns...
	 * 
	 * @return number of stones currently in pit
	 */
	public int returnStones() {
		return stones;
	}
	
	/**
	 * This function returns the number of stone currently in the pit, then it empties the pit.
	 * 
	 * @return temp; number of stones currently in the pit
	 */
	public int emptyPit() {
		
		int temp = stones;
		stones = 0;
		return temp;	
	}

	public boolean isEmpty() {
		if (stones == 0) {
			return true;
		}
		else {
			return false;
		}
	}
}

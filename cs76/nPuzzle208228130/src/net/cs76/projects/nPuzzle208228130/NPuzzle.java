package net.cs76.projects.nPuzzle208228130;

public class NPuzzle {
	// map of blocks to actual positions on grid
	// key: grid position
	// value: game order
	// when they match up, game is over
	private int[] positions; 
	
	// grid position of the blank space
	private int blank;
	
	// num moves
	private int moves;
	
	public NPuzzle(int size) {
		positions = new int[size*size];
		startNew();
	}
	
	/**
	 * Sets up our game state for a new game
	 */
	public void startNew() {
		int l = positions.length,
			i;
		
		// set the positions.
		for (i=0;i<l;i++) {
			// l never changes, so subtracting i as we loop will give us a decrement
			positions[i] = l-i-2;
		}
		
		blank = l-1;
		
		// special case, we need to swap 1 and 2
		if (l%2 == 0) {
			positions[l-3] = 1;
			positions[l-2] = 2;
		}
		
		// set the number of moves to 0
		moves = 0;
	}
	
	/**
	 * Confirms that the 2 positions can be swapped
	 */
	public boolean canSwap(int t1, int t2) {
		int diff = Math.abs(t1 - t2),
			size = Difficulty.size();
		// if the 2 positions are horizontally adjacent, the diff will be 1
		// if they're vertically adjaacent, the diff will be the number of columns, or size
		if (diff == 1 || diff == size)
			return true;
		
		return false;
	}
	
	/**
	 * Swaps the position of 2 tiles
	 * @param t1
	 * @param t2
	 */
	public void swap(int t1, int t2) {
		if (!canSwap(t1, t2)) return;
		int temp = positions[t1];
		positions[t1] = positions[t2];
		positions[t2] = temp;
		
		// increment move counter
		moves++;
	}
	
	/**
	 * Special use case for swap:
	 * Swapping with the blank square
	 * @param target
	 */
	public void swap(int target) {
		if (!canSwap(target, blank)) return;
		swap(target, blank);
		blank = target;
	}
	
	/** 
	 * Test that the game has been completed 
	 */
	public boolean hasWon() {
		int i, l = positions.length;
		boolean vict = true;
		
		// loop through all the positions
		// if the key and value are the same, the game is over
		// the last position should be blank and is ignored
		
		// if the last position is not blank, the game isn't over
		if (blank != l-1) return false;
		
		for (i=0;i<l-1;i++) {
			if (i != positions[i]) {
				vict = false;
				break;
			}
		}
		
		return vict;
	}
	
	/**
	 * Gets the block position of the given grid position
	 */
	public int getGamePosition(int pos) {
		return positions[pos];
	}
	
	/**
	 * Special case for getting the position of the blank
	 */
	public int getBlankPosition() {
		return blank;
	}
	
	public int getNumMoves() {
		return moves;
	}
}

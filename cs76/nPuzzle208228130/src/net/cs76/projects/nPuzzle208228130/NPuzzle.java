/**
 * Matt Petrovic
 * mpetrovic@iq.harvard.edu
 * 208228130
 * 
 * Stores all of the game state for our game. 
 * Each instance represents one game state
 * This is stored in preferences as a json string
 * Uses dumb, reverse-order implementation of NPuzzle initialization
 */
package net.cs76.projects.nPuzzle208228130;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

public class NPuzzle implements Serializable {
	// Eclipse wanted this
	private static final long serialVersionUID = -6514357686117633888L;

	// map of blocks to actual positions on grid
	// key: grid position
	// value: bitmap position
	// when they match up, game is over
	private int[] positions; 
	
	// grid position of the blank space
	private int blank;
	
	// num moves
	private int moves;
	
	// the image used
	private int image;
	
	/**
	 * Create a new NPuzzle game
	 * @param size - The size of the game board
	 * @param image - The image used in the board
	 */
	public NPuzzle(int size, int image) {
		this.image = image;
		positions = new int[size*size];
		startNew();
	}
	
	// for use with static methods
	private NPuzzle() {}
	
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
			
			// special case. 1 and 2 need to be swapped when the size is even
			if (l%2 == 0) {
				if (positions[i] == 2) {
					positions[i] = 1;
				}
				else if (positions[i] == 1) {
					positions[i] = 2;
				}
			}
		}
		
		blank = l-1;
		
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
				// if even one position is off, the game isn't over
				vict = false;
				// and we can just stop checking now
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
	
	// Getter functions for loaded data
	public int getSize() {
		return (int)Math.sqrt((double)positions.length);
	}
	
	public int getImage() {
		return image;
	}
	
	/**
	 * Saves the game state
	 * Game state is saved as a JSON object
	 * @param ctx
	 */
	public void save(Context ctx) {
		JSONObject json = new JSONObject();
		JSONArray pos = new JSONArray();
		
		try {
			// make our positions array into a JSONArray
			int i, l = positions.length;
			for (i=0; i<l; i++) {
				pos.put(i, positions[i]);
			}
			
			// put all the data
			json.put("positions", pos)
				.put("blank", blank)
				.put("moves", moves)
				.put("image", image);
			
			// and save it
			SharedPreferences prefs = ctx.getSharedPreferences("NPuzzle", Context.MODE_PRIVATE);
			prefs.edit().putString("gameData", json.toString()).commit();
		}
		catch (JSONException e) {
			// I control this data completely, so this should never happen
		}
	}
	
	/**
	 * Loads the game state
	 */
	public static NPuzzle load(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("NPuzzle", Context.MODE_PRIVATE);
		NPuzzle np = null;
		try {
			JSONObject json = new JSONObject(prefs.getString("gameData", ""));
			
			JSONArray pos = json.optJSONArray("positions");
			
			np = new NPuzzle();
			
			// convert the JSONArray into an actual array
			int i, l = pos.length();
			
			np.positions = new int[l];
			for (i=0; i<l; i++) {
				np.positions[i] = pos.getInt(i);
			}
			
			// get the remaining values
			np.blank = json.getInt("blank");
			np.moves = json.getInt("moves");
			np.image = json.getInt("image");
		}
		catch (JSONException e) {
			// probably the string was empty, so nothing needs to be done
			np = null;
		}
		return np;
		
	}
	
	/**
	 * Clears the existing game state in storage
	 */
	public void clear(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("NPuzzle", Context.MODE_PRIVATE);
		prefs.edit().remove("gameData").commit();
	}
}

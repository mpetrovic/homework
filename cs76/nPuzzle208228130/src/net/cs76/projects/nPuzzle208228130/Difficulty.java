/**
 * Matt Petrovic
 * mpetrovic@iq.harvard.edu
 * 208228130
 * 
 * Static class that stores and handles the difficulty of the game
 */
package net.cs76.projects.nPuzzle208228130;

import android.content.Context;
import android.content.SharedPreferences;

public class Difficulty {
	// constants
	final static int EASY = R.id.diff_easy;
	final static int NORMAL = R.id.diff_normal;
	final static int HARD = R.id.diff_hard;
	
	// current difficulty
	private static int diff = NORMAL;
	
	/**
	 * Sets the difficulty of the game
	 * @param The new difficulty level. Must be one of the marked difficulties
	 * If the difficulty given is not in the allowed range, nothing is changed
	 */
	public static boolean d(int new_diff) {
		if (new_diff != EASY && new_diff != NORMAL && new_diff != HARD)
			return false;
		
		diff = new_diff;
		return true;
	}
	
	/**
	 * Gets the difficulty of the game
	 */
	public static int d() {
		return diff;
	}
	
	/**
	 * Gets the size of the game board.
	 * All game boards are square by default, so this is a single int
	 */
	public static int size() {
		switch (diff) {
		case EASY:
			return 3;
		case HARD:
			return 5;
		case NORMAL:
		default:
			return 4;
		}
	}
	
	/**
	 * Saves the chosen difficulty to preferences
	 * @param ctx
	 */
	public static void putToStore(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("nPuzzle", Context.MODE_PRIVATE);
		prefs.edit().putInt("difficulty", diff).commit();
	}
	
	/**
	 * Loads the saved difficulty
	 * @param ctx
	 * @return
	 */
	public static int getFromStore(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences("nPuzzle", Context.MODE_PRIVATE);
		d(prefs.getInt("difficulty", 4));
		return d();
	}
}

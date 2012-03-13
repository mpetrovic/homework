package net.cs76.projects.student208228130;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import android.content.Context;

public class GameData implements Serializable {
	
	public static Context ctx;
	
	public Tower[] towers;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4575292647122624476L;

	/**
	 * Loads an in-progress game from a given source
	 * @param source - Accepted values: 'continue'
	 * @return	The game data from that source
	 */
	public static GameData load(String source) {
		GameData g = new GameData();
		
		return g;
	}

	/**
	 * Creates a new game from the given level
	 * @param id	The id of the level to pull data from
	 * @return		The new game
	 */
	public static GameData create(int level) {
		return loadDataFromRaw(level);
	}
	
	/**
	 * Private constructor
	 * All creation should pass through static methods
	 */
	private GameData() {
		
	}
	
	private static GameData loadDataFromRaw(int raw) {
		InputStreamReader isr = new InputStreamReader(ctx.getResources().openRawResource(raw));
		BufferedReader bfr = new BufferedReader(isr);
		
		GameData gd = new GameData();
		
		return gd;
	}
}

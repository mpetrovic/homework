package net.cs76.projects.student208228130;

import java.io.Serializable;

public class GameData implements Serializable {

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
		// stub
		GameData g = new GameData();
		
		return g;
	}

	/**
	 * Creates a new game from the given level
	 * @param id	The id of the level to pull data from
	 * @return		The new game
	 */
	public static GameData create(long level) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Private constructor
	 * All creation should pass through static methods
	 */
	private GameData() {
		
	}
}

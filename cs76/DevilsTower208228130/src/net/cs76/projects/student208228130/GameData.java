package net.cs76.projects.student208228130;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class GameData extends World implements Serializable {
	
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
	 * This constructor will handle things common to every game:
	 * Gravity, the ground, setting up the physics engine
	 */
	private GameData() {
		super(new Vec2(0.0f, -9.8f), true);
		
		// create the ground
		BodyDef bd = new BodyDef();
		FixtureDef fd = new FixtureDef();
		fd.density = 1.0f;
		fd.friction = 1.0f;
		fd.restitution = 0.5f;
		
		createBody(bd).createFixture(fd);
		
	}
	
	private static GameData loadDataFromRaw(int raw) {
		InputStreamReader isr = new InputStreamReader(ctx.getResources().openRawResource(raw));
		BufferedReader bfr = new BufferedReader(isr);
		GameData gd = new GameData();
		
		String data = "";
		try {
			String line = bfr.readLine();
			
			while (line != null) {
				data.concat(line);
				line = bfr.readLine();
			}
		}
		catch (IOException ioe) {
			return gd;
		}
		
		if (data.length() > 0) {
			try {
				JSONObject obj = new JSONObject(data);
				JSONArray towers = obj.getJSONArray("towers");
				gd.towers = new Tower[towers.length()];
				for (int i=0, l=towers.length(); i<l; i++) {
					JSONObject towerDef = towers.getJSONObject(i);
					HashMap<String, Integer> starting = new HashMap<String, Integer>();
					JSONObject positions = towerDef.getJSONObject("starting");
					Iterator<String> iter = positions.keys();
					while (iter.hasNext()) {
						String key = iter.next();
						starting.put(key, positions.getInt(key));
					}
					Tower t = new Tower(
						gd,
						towerDef.getInt("speed"),
						towerDef.getInt("can_build"),
						starting
					);
					gd.towers[i] = t;
				}
			}
			catch (JSONException jse) {
				return gd;
			}
		}
		
		return gd;
	}
}

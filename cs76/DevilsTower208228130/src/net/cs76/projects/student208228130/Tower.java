package net.cs76.projects.student208228130;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class Tower implements Serializable {

	private static final long serialVersionUID = -6260272047909430180L;

	// all segments are given a key representing their position in the tower
	// ex. 1-1 represents the leftmost segment on the bottom floor
	// 3-2 represents the third floor, 2nd segment from the left
	// so on and so far
	private Map<String, Segment> segments;
	
	// how quickly new segments are constructed
	// in seconds
	private int baseConstructRate;
	private int constructRate;
	
	// segments currently under construction
	private int canConstruct;			// number of segments that can be constructed at once
	private String[] inConstruction;	// Map keys of segments under construction 
	
	// when this hits 0, the devils give up and the tower is no longer a threat
	private int morale;
	
	// the physics engine world
	private World world;
	
	public Tower(World world, int baseRate, int canConstruct, Map<String, Integer> starting) {
		this.world = world;
		
		baseConstructRate = constructRate = baseRate;
		
		this.canConstruct = canConstruct;
		
		// prep for setting up the initial segments
		segments = new HashMap<String, Segment>();
		
		String[] keys = new String[1];
		keys = starting.values().toArray(keys);
		BodyDef bd = new BodyDef();
		
		// from our starting Map, we set up the existing segment instances
		for (int i=0,l=keys.length; i<l; i++) {
			segments.put(keys[i], new Segment((int)starting.get(keys[i]), bd, world));
		}
	}
	
	private void beginConstruction() {
		if (canConstruct > inConstruction.length) {
			// AI time
		}
	}
	
	public void step() {
		beginConstruction();
	}
}

/**
 * Matt Petrovic
 * mpetrovic@iq.harvard.edu
 * 208228130
 * 
 * First Activity seen by the user
 * Displays a list of the images they are able to use
 * as game board.
 */
package net.cs76.projects.nPuzzle208228130;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ImageSelection extends Activity implements OnItemClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // continues loading the view, since this will be active when they return
        ListView list = (ListView)findViewById(R.id.img_list);
        list.setAdapter(new ImageAdapter(this));
        list.setOnItemClickListener(this);
    }
    
    /**
     * Loads the difficulty of the game when we come back,
     * and checks for a game in progress to continue
     */
    public void onResume() {
    	super.onResume();
        
    	Difficulty.getFromStore(this);
        
        // a game has been started but not completed
        // load it up and pass it to the GamePlay activity
        NPuzzle np = NPuzzle.load(this);
        if (np != null) {
        	Intent i = new Intent(this, GamePlay.class);
        	i.putExtra("loadedGameState", np);
        	startActivity(i);
        }
    }

    /**
     * Implements onItemClick()
     * 
     * Start a game at the given difficulty with this image
     */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// set up Intent
		Intent i = new Intent(this, GamePlay.class);
		
		// set game level
		i.putExtra("gameLevel", id);
		
		// start it
		startActivity(i);
	}
	
	/**
	 * Creates the menu that sets the difficulty of the game
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater infl = getMenuInflater();
		infl.inflate(R.menu.difficulty, menu);
		return true;
	}
	
	/**
	 * Changes the difficulty of the game based on the user's selection
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Difficulty.d(item.getItemId())) {
			Difficulty.putToStore(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
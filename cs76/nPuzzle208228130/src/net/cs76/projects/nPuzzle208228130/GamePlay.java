/**
 * Matt Petrovic
 * mpetrovic@iq.harvard.edu
 * 208228130
 * 
 * The actual game Activity
 * The game begins with a 3 second countdown with the full image
 * Then the image is split apart by the Adapter.
 * 
 * This activity handles the 3 second cooldown
 */
package net.cs76.projects.nPuzzle208228130;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class GamePlay extends Activity implements OnItemClickListener {
	
	// the background image
	private int image;
	
	// game state manager
	NPuzzle np;
	
	/** Called when Activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // check to see if we got an NPuzzle game from the previous activity
        np = (NPuzzle)getIntent().getExtras().getSerializable("loadedGameState");
        if (np == null) {
        	// nope, this is a new game
            image = (int)getIntent().getExtras().getLong("gameLevel");
        	np = new NPuzzle(Difficulty.size(), image);
        }
        else {
        	// we're returning from a save.
        	image = np.getImage();
        }
        startOver();
	}
	
	/**
	 * Check that we have some game data when we come back
	 */
	public void onResume() {
		if (np == null) {
			np = NPuzzle.load(this);
		}
		super.onResume();
	}
	
	/**
	 * If we've won the game, clear any save data
	 * If we haven't, save it so we can get at it later
	 */
	public void onPause() {
		if (np.hasWon()) {
			np.clear(this);
		}
		else {
			np.save(this);
		}
		super.onPause();
	}

	/** 
	 * When an item is clicked, it should swap with the blank
	 */
	public void onItemClick(AdapterView<?> adv, View img, int pos, long id) {
		// swap the positions
		np.swap(pos);
		
		if (np.hasWon()) {
			// the player has won the game,
			// move to the YouWin activity
			Intent i = new Intent(this, YouWin.class);
			
			i.putExtra("image", image);
			i.putExtra("moves", np.getNumMoves());
			startActivity(i);
			finish();
		}
		
		// Alert the adapter that the game state has changed
		GridView grid = (GridView)adv;
		BaseAdapter ba = (BaseAdapter) grid.getAdapter();
		ba.notifyDataSetChanged();
		grid.postInvalidate();
	}
	
	/**
	 * Previews the completed image with a 3s countdown, 
	 * then starts the game proper
	 */
	public void startOver() {
        setContentView(R.layout.game_preview);
        ImageView img = (ImageView)findViewById(R.id.img);
        img.setImageResource(image);
        
        // so we can get at this inside the CountDownTimer
        final GamePlay self = this;
        
        // start a countdown timer
        new CountDownTimer(4000, 1000) {
        	public void onTick(long rem) {
        		// change the text
        		TextView txt = (TextView)findViewById(R.id.countdown);
        		txt.setText(""+Math.round(rem/1000));
        	}
        	public void onFinish() {
        		// countdown over, switch to the grid
        		self.setContentView(R.layout.game);
                GridView grid = (GridView)findViewById(R.id.gameGrid);
                grid.setOnItemClickListener(self);
                
                // and setup the game
                self.setupGame();
        	}
        }.start();
		
	}
	
	/**
	 * Convenience function for common use case
	 */
	private void setupGame() {
		setupGame(true);
	}
	
	/**
	 * Sets up the game board.
	 * 
	 * @param restart - If true, resets the game entirely. Otherwise, maintain save state
	 */
	private void setupGame(boolean restart) {
        GridView grid = (GridView)findViewById(R.id.gameGrid);
        GameAdapter ga;
        int size = Difficulty.size();
        // starting from scratch
		if (restart) {
			// set the grid to the right number of columns
			size = np.getSize();
	        grid.setNumColumns(size);
	        // and the adapter for it
	        ga = new GameAdapter(this, np, image, size);
			grid.setAdapter(ga);
		}
		else {
			// we aren't starting from scratch
			// we probably came from an orientation change
			// tell the adapter to rebuild the cache
			ga = (GameAdapter)grid.getAdapter();
			ga.setup();
		}
		
		// force the grid to be the right size
		int width = ga.getWidth(),
			height = ga.getHeight();
		LayoutParams params = new LayoutParams(width, height);
		grid.setLayoutParams(params);
	}
	
	/**
	 * Creates the menu that sets the difficulty of the game
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater infl = getMenuInflater();
		infl.inflate(R.menu.ingame, menu);
		return true;
	}
	
	/**
	 * Changes the difficulty of the game based on the user's selection
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// user opted to change the difficulty, or reset the game entirely
		if (Difficulty.d(id) || id == R.id.reset) {
			Difficulty.putToStore(this);
			np = new NPuzzle(Difficulty.size(), image);
			startOver();
			return true;
		}
		else if (id == R.id.back) {
			// user decided to go back
			// the image_selection activity never goes away
			finish();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * The orientation of the device changed.
	 * The image needs to be resized and 
	 * the bitmap cache rebuilt
	 */
	public void onConfigurationChanged(Configuration c) {
		super.onConfigurationChanged(c);
		setupGame(false);
	}
}

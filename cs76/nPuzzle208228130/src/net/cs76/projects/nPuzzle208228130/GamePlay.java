package net.cs76.projects.nPuzzle208228130;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class GamePlay extends Activity implements OnItemClickListener {
	
	// the background image
	private int image;
	
	// game state manager
	NPuzzle np;
	
	/** Called when Activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        image = (int)getIntent().getExtras().getLong("gameLevel");
        GridView grid = (GridView)findViewById(R.id.gameGrid);
        grid.setOnItemClickListener(this);
        
        setupGame();
        
	}

	/** 
	 * When an item is clicked, it should swap with the blank
	 */
	public void onItemClick(AdapterView<?> adv, View img, int pos, long id) {
		np.swap(pos);
		if (np.hasWon()) {
			// move to the YouWin activity
			Intent i = new Intent(this, YouWin.class);
			
			i.putExtra("image", image);
			i.putExtra("moves", np.getNumMoves());
		}
		else {
			GridView grid = (GridView)adv;
			BaseAdapter ba = (BaseAdapter) grid.getAdapter();
			ba.notifyDataSetChanged();
			grid.postInvalidate();
		}
	}
	
	private void setupGame() {
		setupGame(true);
	}
	
	private void setupGame(boolean restart) {
        GridView grid = (GridView)findViewById(R.id.gameGrid);
        int size = Difficulty.size();
		if (restart) {
	        np = new NPuzzle(size);
	        grid.setNumColumns(size);
			grid.setAdapter(new GameAdapter(this, np, image, size));
		}
		else {
			GameAdapter ga = (GameAdapter)grid.getAdapter();
			ga.setup();
		}
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
			setupGame(true);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * The orientation of the device changed.
	 * The image needs to be resized and 
	 */
	public void onConfigurationChanged(Configuration c) {
		super.onConfigurationChanged(c);
		setupGame(false);
	}
}

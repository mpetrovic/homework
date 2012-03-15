/**
 * Matt Petrovic
 * mpetrovic@iq.harvard.edu
 * 208228130
 * 
 * Final Activity seen, when the user wins a game.
 * Displays the gameboard as a single unified image,
 * with the number of moves they took
 */
package net.cs76.projects.nPuzzle208228130;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YouWin extends Activity implements OnClickListener {
	
	/** Called when Activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youwin);
		
		// sets the giant image to the game they just played
		ImageView img = (ImageView)findViewById(R.id.img);
		int image = getIntent().getExtras().getInt("image");
		img.setImageResource(image);
		
		// Shows the number of move the user took to win
		TextView mv = (TextView)findViewById(R.id.moves);
		int moves = (int)getIntent().getExtras().getInt("moves");
		mv.setText(""+moves);
		
		// Sets the onclick listener so we leave this activity and return to ImageSelection
		RelativeLayout lin = (RelativeLayout)findViewById(R.id.you_win_view);
		lin.setOnClickListener(this);
	}

	/**
	 *  Return to image selection when we click somewhere
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, ImageSelection.class);
		startActivity(i);
		finish();
	}
}

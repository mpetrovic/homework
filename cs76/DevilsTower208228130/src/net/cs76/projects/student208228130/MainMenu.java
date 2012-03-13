package net.cs76.projects.student208228130;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // assign the context so the game engine can access it
        GameData.ctx = this;
    }

	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
			case R.id.mainMenu_Continue:
				i.setClass(this, GamePlayActivity.class);
				i.putExtra("gameData", GameData.load("continue"));
				break;
			case R.id.mainMenu_NewGame:
				i.setClass(this, LevelSelectActivity.class);
				break;
			case R.id.mainMenu_HighScores:
				i.setClass(this, HighScoresActivity.class);
				break;
			case R.id.mainMenu_Settings:
				i.setClass(this, GameSettingsActivity.class);
				break;
			case R.id.mainMenu_ExitGame:
				finish();
				return;
		}
		
		startActivity(i);
	}
}
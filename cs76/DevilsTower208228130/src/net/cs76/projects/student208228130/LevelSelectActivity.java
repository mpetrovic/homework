package net.cs76.projects.student208228130;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LevelSelectActivity extends Activity implements OnItemClickListener {
	
	public void onCreate(Bundle state) {
		super.onCreate(state);
		
		setContentView(R.layout.level_select);
		ListView list = (ListView)findViewById(R.id.levelSelect_list);
		list.setAdapter(new LevelSelectAdapter(this));
		list.setOnItemClickListener(this);
	}

	/**
	 * Gets the level the used wants to play
	 * and loads it into the Game
	 */
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		Intent i = new Intent(this, GamePlayActivity.class);
		GameData g = GameData.create((int)id);
		i.putExtra("gameData", g);
		startActivity(i);
		finish();
	}

}

package net.cs76.projects.student208228130;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LevelSelectAdapter extends BaseAdapter {
	
	// the levels
	Field[] levels;
	
	Context ctx;
	
	public LevelSelectAdapter(Context ctx) {
		levels = R.raw.class.getFields();
		this.ctx = ctx;
	}

	public int getCount() {
		return levels.length;
	}

	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return levels[pos];
	}

	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int pos, View convert, ViewGroup parent) {
		TextView tv;
		if (convert == null) {
			tv = (TextView) convert;
		}
		else {
			tv = new TextView(ctx);
		}
		String str = levels[pos].getName();
		str = str.replace("level_", "");
		int level = Integer.parseInt(str);
		tv.setText("Level "+level);
		return tv;
	}

}

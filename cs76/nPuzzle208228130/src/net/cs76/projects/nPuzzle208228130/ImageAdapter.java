package net.cs76.projects.nPuzzle208228130;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	
	// the images used to build a game
	private Integer[] images;
	
	// the context to add views to
	private Context ctx;
	
	// image cache
	private Bitmap[] cache;
	
	// filenames
	private String[] fnames;
	
	// Constructor
	public ImageAdapter(Context c) {
		ctx = c;
		
		// get our list of images
		Field[] imgs = R.drawable.class.getFields();
		
		int count = 0, 
			index = 0, 
			// define iterators once rather than each time the loop is run
			i, l = imgs.length;
		Bitmap bmp;
		Resources resCache = ctx.getResources();
		
		// Get a count of the number of images provided
		for (i=0; i<l; i++) {
			if (imgs[i].getName().startsWith("puzzle_")) count++; 
		}
		
		images = new Integer[count];
		cache = new Bitmap[count];
		fnames = new String[count];
		
		try {
			// run through all our puzzle_ images, store the main and a cached thumbnail
			for (i=0; i<l; i++) {
				if (imgs[i].getName().startsWith("puzzle_")) {
					images[index] = imgs[i].getInt(null);
					// separate lines for readability. No one likes a single line of code thats 200 chars long
					bmp = BitmapFactory.decodeResource(resCache, images[index]);
					cache[index] = Bitmap.createScaledBitmap(bmp, 90, 90, true);
					fnames[index] = imgs[i].getName();
					index++;
				}
			}
		}
		// these should never happen
		catch (IllegalAccessException e) {}
		catch (IllegalArgumentException e) {}
		
	}
	

	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return images[position];
	}
	
	public Object getThumb(int position) {
		return cache[position];
	}

	/**
	 * Construct a view from the main_item template which uses the scaled down image and its filename
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// Inflates the main_item ViewGroup
		LayoutInflater infl = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup row = (ViewGroup)infl.inflate(R.layout.main_item, null);
		
		ImageView img = (ImageView)row.findViewById(R.id.mainMenuRowImage);
		img.setImageBitmap(cache[position]);
		
		TextView txt = (TextView)row.findViewById(R.id.mainMenuRowText);
		txt.setText(fnames[position]);
		return row;
	}

}

package net.cs76.projects.nPuzzle208228130;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GameAdapter extends BaseAdapter {
	
	// the image we're playing with
	private int image;
	private Bitmap bmp;
	
	private NPuzzle np;
	
	// the size of the game grid
	private int size;
	
	// the pieces of the image
	private Bitmap[] blocks;
	
	// the context
	private Context ctx;

	public GameAdapter(Context c, NPuzzle np, int image, int size) {
		super();
		ctx = c;
		this.image = image;
		this.size = size;
		
		blocks = new Bitmap[size*size];
		this.np = np;
		setup();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return size * size;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return blocks[np.getGamePosition(position)];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (blocks[0] == null) {
			setup();
		}
		ImageView iv;
		if (convertView != null) {
			iv = (ImageView)convertView;
		}
		else {
			iv = new ImageView(ctx);
		}
		if (np.getBlankPosition() == position) {
			iv.setImageBitmap(null);
			iv.setMinimumHeight(blocks[0].getHeight());
			iv.setMinimumWidth(blocks[0].getWidth());
		}
		else {
			iv.setImageBitmap(blocks[np.getGamePosition(position)]);
		}
		
		return iv;
	}
	
	public void setup() {
		// get the size of the screen
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		
		// and the bitmap we're making into a game
		Resources res = ctx.getResources();
		bmp = BitmapFactory.decodeResource(res, image);
		int max_width = disp.getWidth(),
			max_height = disp.getHeight(),
			width = bmp.getWidth(),
			height = bmp.getHeight();
		float scaleH = (float)max_height/height,
			  scaleW = (float)max_width/width,
			  scale;
		
		// get the amount we have to scale the image by to fit the viewport
		if (scaleH < scaleW) scale = scaleH;
		else scale = scaleW;
		
		// create the new image and change our variables to match
		bmp = Bitmap.createScaledBitmap(bmp, (int)Math.round(width*scale), (int)Math.round(height*scale), true);
		width = (int)Math.round(width*scale);
		height = (int)Math.round(height*scale);
		
		// get tile dimensions
		int tileWidth = (int)Math.round(width/size),
			tileHeight = (int)Math.round(height/size),
			i, j;
		
		for (i=0;i<size;i++) {
			for (j=0;j<size;j++) {
				int key = i*size+j;
				blocks[key] = Bitmap.createBitmap(bmp, j*tileWidth, i*tileHeight, tileWidth, tileHeight, null, false);
			}
		}
	}
}

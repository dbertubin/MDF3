/*
 * project	ClimaCast
 * 
 * package 	com.petrockz.climacast
 * 
 * @author 	${author}
 * 
 * date 	Oct 10, 2013
 */
package com.petrockz.climacast;



import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

// TODO: Auto-generated Javadoc
/**
 * The Class Favorites.
 */
public class Favorites extends Activity implements FavoritesFragment.FavoritesListener{

	Context _context;

	public static final String FILE_NAME = "favsArray";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Favorites" , "Setting CV");
		setContentView(R.layout.favorites_fragment);
		_context = this;
		
		ActionBar actionBar = getActionBar(); 
		actionBar.setDisplayHomeAsUpEnabled(true);
		Log.i("CONTEXT =", "YAY");
	}

	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FavoritesFragment.FavoritesListener#onFavoriteSelected(java.lang.String)
	 */
	@Override
	public void onFavoriteSelected(String zip) {
		Intent myIntent = new Intent(_context, MainActivity.class);
		myIntent.putExtra("item", zip);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		//				setResult(Activity.RESULT_OK, myIntent);
		startActivity(myIntent);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
	        switch (item.getItemId()) {
	        case android.R.id.home: 
	            onBackPressed();
	            return true;
	        }

	    return super.onOptionsItemSelected(item);
	}
	
}




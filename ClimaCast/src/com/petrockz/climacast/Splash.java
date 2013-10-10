/*
 * project	ClimaCast
 * 
 * package 	com.petrockz.climacast
 * 
 * @author 	Derek Bertubin
 * 
 * date 	Oct 10, 2013
 */
package com.petrockz.climacast;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class Splash.
 */
public class Splash extends Activity {

	
	MediaPlayer _player;
	
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		AssetFileDescriptor afd;
		try {
			// Get the music file from the asset folder
			afd = getAssets().openFd("thunder.mp3");
			// Creation of new media _player;
			_player = new MediaPlayer();
			// Set the _player music source.
			_player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			// Set the looping and play the music.
		 
			_player.prepare();
			_player.start();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("AFD ERROR", e.toString());
		}
		
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. 
			 * 
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start app main activity
				Intent i = new Intent(Splash.this, MainActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		_player.release();
		_player = null;
		super.onStop();
		
	}
	
}


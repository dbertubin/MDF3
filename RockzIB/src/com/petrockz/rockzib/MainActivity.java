package com.petrockz.rockzib;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	
	Button _backButton;
	Button _fwButton;
	Button _stopButton;
	Button _goButton;
	
	EditText _urlEditText;
	WebView _webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		Uri data = intent.getData();
		
		
//		startActivity(Intent.createChooser(intent, getTitle()));
		
		_urlEditText = (EditText) findViewById(R.id.editText1);
		_backButton = (Button) findViewById(R.id.backButton);
		_fwButton = (Button) findViewById(R.id.fwButton);
		_stopButton = (Button) findViewById(R.id.stopButton);
		_goButton = (Button) findViewById(R.id.goButton);
		_goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Go", "was hit");
			}
		});
		
		
		_urlEditText = (EditText) findViewById(R.id.editText1);
		
		
		// WebView Set Up 
		_webView = (WebView) findViewById(R.id.webView);
		
		// Enable JS 
		_webView.getSettings().setJavaScriptEnabled(true);
		
		// Loads default url 
		_webView.loadUrl("http://www.google.com");
		
		// Allows for internal handling of urls 
		_webView.setWebViewClient(new RockzIBWebViewClient());
		
		
	}
	
	// Allows for internal handling of urls 
	private class RockzIBWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView webview, String url) {
			webview.loadUrl(url);
			return true;
		}
		
		
	}

	// Checking for Native Back Button navigation 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && _webView.canGoBack()) {
	    	_webView.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

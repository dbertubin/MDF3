package com.petrockz.rockzib;


import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	Button _backButton;
	Button _fwButton;
	Button _stopButton;
	Button _goButton;

	EditText _urlEditText;
	WebView _webView;

	String _url;
	String _passedUrl = new String();
	
	Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_context = this; 
		
		Intent intent  = getIntent();
		Uri data = intent.getData();
		
		if (data != null) {
			_passedUrl = data.toString();
			Log.i("Data", data.toString());
		}

		if (_passedUrl.startsWith("http")) {
			_webView.loadUrl(_passedUrl);
		} else {
			Toast.makeText(MainActivity.this,"This didnt work", Toast.LENGTH_SHORT).show();
		}
		
	
		
		_urlEditText = (EditText) findViewById(R.id.editText1);

		
		// WebView Set Up 
				_webView = (WebView) findViewById(R.id.webView);

				// Enable JS 
				_webView.getSettings().setJavaScriptEnabled(true);

				// Loads default url 
				_webView.loadUrl("http://www.google.com");

				// Allows for internal handling of urls 
				_webView.setWebViewClient(new RockzIBWebViewClient());

		
		

		// BACK BUTTON 
		_backButton = (Button) findViewById(R.id.backButton);

		_backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_webView.canGoBack()) {
					_webView.goBack();
				}
			}
		});


		// FORWARD BUTTON 
		_fwButton = (Button) findViewById(R.id.fwButton);
		_fwButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (_webView.canGoForward()) {
					_fwButton.setVisibility(View.VISIBLE);
					_webView.goForward();
				}
			}
		});
		
	
		// GO BUTTON 
		_goButton = (Button) findViewById(R.id.goButton);
		_goButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Go", "was hit");				

				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(_urlEditText.getWindowToken(), 0);
				
				
				// grab string from edit text 
				_url = _urlEditText.getText().toString();

				// check to see if string contains url prefix and add if not 
				if (!_url.startsWith("http"))
					_url = "http://" + _url;

				// load url if good 
				_webView.loadUrl(_url);
					
			}
		});

		
		
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

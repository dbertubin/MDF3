package com.petrockz.rockzib;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

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
		
		
		_backButton = (Button) findViewById(R.id.backButton);
		_fwButton = (Button) findViewById(R.id.fwButton);
		_stopButton = (Button) findViewById(R.id.stopButton);
		_goButton = (Button) findViewById(R.id.goButton);
		_urlEditText = (EditText) findViewById(R.id.editText1);
		_webView = (WebView) findViewById(R.id.webView);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.petrockz.climacast;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class About extends Activity{
	
	
	public class JavaScriptInterface {
	    Context _context;
	    

	    public JavaScriptInterface (Context context)
	    {
	    	_context = context;
	       
	    }

	    @JavascriptInterface
	    public void sendEmail(String string){
	    
	    	Log.i("String is", string);
	 	
	    	Log.i("INTENT", "is getting hit");
	        Intent sendIntent = new Intent(Intent.ACTION_SEND);
	        sendIntent.setType("text/html");
	        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, string + "\n -Sent From ClimaCast");
	        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Email From Climacast");
	        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "dbertubin@fullsail.edu" });
	        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	        _context.startActivity(sendIntent);
	    } 
	    
	}
	
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	
		
		/* 
		 * Set up Action Bar with back 
		 ******************************/
		ActionBar actionBar = getActionBar(); 
		actionBar.setDisplayHomeAsUpEnabled(true);
	
		/* 
		 * Instantiate WebView 
		 *********************/
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("file:///android_asset/index.html");
		webView.setWebViewClient(new ThisWebViewClient());
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Native");
		/*
		 * Enable JavaScript
		 ********************/
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		

		
		
		
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
	
	private class ThisWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView webview, String url) {
			webview.loadUrl(url);
			return true;
		}
	}
	
	
	
	
}

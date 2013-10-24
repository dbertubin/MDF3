package com.petrockz.climacast;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class About extends Activity{
	
	
	public class JavaScriptInterface {
	    private About AppView;
	    

	    public JavaScriptInterface (About about)
	    {
	    	AppView = about;
	       
	    }

	    public void sendEmail(){
	        Intent sendIntent = new Intent(Intent.ACTION_SEND);
	        sendIntent.setType("text/html");
	        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,"test text");
	        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Email From Climacast");
	        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	        startActivity(Intent.createChooser(sendIntent, "Send email..."));
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
		
		/*
		 * Enable JavaScript
		 ********************/
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		
		JavaScriptInterface jsi = new JavaScriptInterface(this);
		webView.addJavascriptInterface(jsi, "Android");
		
		
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

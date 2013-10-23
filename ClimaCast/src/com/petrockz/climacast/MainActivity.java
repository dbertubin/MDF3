/*
 * project	ClimaCast
 * 
 * package 	com.petrockz.climacast
 * 
 * @author 	Derek Bertubin
 * 
 * date 	Aug 5, 2013
 * 
 * For this assignment I used the Following to hopefully meet the requirements of the assignment
 * 
 * Location - The Location Button uses GPS and provides a usable Zip code to feed the editText 
 * 
 * Media Playback - Occurs in the Splash Activity on Application Launch 
 * 
 * Network Connections - Checks to see what type of network connections are being used and notifies user if there is a recommenced option 
 * 
 */
package com.petrockz.climacast;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.petrockz.chucknorris.lib.NetworkConnection;
import com.petrockz.climacast.ClimaCastWidgetProvider.WidgetListener;
import com.petrockz.climacast.FavoritesFragment.FavoritesListener;
import com.petrockz.climacast.FormFragment.FormListener;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.widget.ImageView;
import android.text.InputType;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;



import android.widget.Toast;


// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity implements FormListener, WidgetListener,FavoritesListener, GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	private static final int REQUEST_CODE = 0;
	static Context _context;

	/*
	 * 
	 *  UI ELEMENTS 
	 * 
	 * 
	 */
	Button _getWeatherButton;
	Button _saveFavButton;
	Button _viewFavButton;
	Button _showMapButton;
	Button _getGPS; 

	EditText _inputText;
	GridLayout _resultsGrid;
	GridLayout _5dayGrid;
	Boolean _connected;
	Boolean _isMobileConn;
	Boolean _isWifiConn;

	
	/*
	 * 
	 *  DATA VARIABLES 
	 * 
	 */
	String _baseURL;
	String _finalURLString;
	String _inputHolder;
	String _temp ;
	String _humidity ;
	String _windSpeed ;
	String _windDirection ;
	String _weatherDescValue;
	String _zip;
	String _numDays;
	String _formattedDate;
	String _formattedDateAdd1;
	String _formattedDateAdd2;
	String _formattedDateAdd3;
	String _formattedDateAdd4;
	String _widgetTemp;
	String _widgetZip;
	
	Spinner _selector;
	Spinner _navSelector;
	
	ArrayList<String> _options = new ArrayList<String>();
	ArrayList<String> _navOptions = new ArrayList<String>();
	ArrayList<String> _dateArray = new ArrayList<String>();
	ArrayList<String> _hiArray = new ArrayList<String>();
	ArrayList<String> _lowArray = new ArrayList<String>();
	ArrayList<String> _conArray = new ArrayList<String>();
	ArrayList<String> detailsHolder = new ArrayList<String>();
	public static ArrayList<String> _favorites = new ArrayList<String>();
	
	ImageView image;
	JSONObject _dataObj;
	int _optionSelected;


	// LOCATION VARS
	LocationClient mLocationClient;
	Location mCurrentLocation;
	private ConnectionResult connectionResult;
	
	
	

	/**
	 * Inits the layout elements.
	 */
	private void initLayoutElements() {
		_context = this;

		_inputText = (EditText)findViewById(R.id.editText);
		_inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
		_finalURLString = null;

		_resultsGrid = (GridLayout) findViewById(R.id.currentData);
	}


	/* 
	 *
	 * ONCREATE	 
	 *
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_fragment);
		_temp = null;
		updateOptionsArray();
		// Layout Elements are contained in here
		initLayoutElements();
		spinnerSelector();
		updateNavOptionsArray();
		
		_favorites = getFavs();

		// Called on launch of main to see if connected to Wifi or Mobile 
		getNetworkInfo();
		

		/*
		 * Create a new location client, using the enclosing class to
		 * handle callbacks.
		 */
		mLocationClient = new LocationClient(this, this, this);


		// Save display data 

		if (_temp != null) {
			savedInstanceState.putString("data_tempF", _temp);
			savedInstanceState.putString("data_humidity", _humidity);
			savedInstanceState.putString("data_windSpeed", _windSpeed);
			savedInstanceState.putString("data_windDirection", _windDirection);
			savedInstanceState.putString("data_location", _zip);
			savedInstanceState.putString("weatherDesc", _weatherDescValue);
			onSaveInstanceState(savedInstanceState);
		}


	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	//This grabs the data.  
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState.getString("data_tempF", _temp) !=null) {
			((TextView) findViewById(R.id.data_tempF)).setText(savedInstanceState.getString("data_tempF"));
			((TextView) findViewById(R.id.data_humidity)).setText(savedInstanceState.getString("data_humidity"));
			((TextView) findViewById(R.id.data_windSpeed)).setText(savedInstanceState.getString("data_windSpeed"));
			((TextView) findViewById(R.id.data_windDirection)).setText(savedInstanceState.getString("data_windDirection"));
			((TextView) findViewById(R.id.data_location)).setText(savedInstanceState.getString("data_location"));
			((ImageView) findViewById(R.id.weatherDesc)).setImageResource(ImageConverter.getConditionImage("weatherDesc"));
		}
	}





	/**
	 * Spinner selector.
	 */
	private void spinnerSelector() {
		_selector = (Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, _options);
		listAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		_selector.setAdapter(listAdapter);
		_selector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override	
			public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){
				Log.i("OPTIONSELECTED", parent.getItemAtPosition(pos).toString());
				_optionSelected = pos;

			}


			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}




	/**
	 * Update options array.
	 */
	private void updateOptionsArray() {	
		_options.add("Get Current Weather");
		_options.add("Get 5 Day Forecast");
		_options.add("Get Weather for " + getDate().get(1));
		_options.add("Get Weather for " + getDate().get(2));
		_options.add("Get Weather for " + getDate().get(3));
		_options.add("Get Weather for " + getDate().get(4));

	}

	private void updateNavOptionsArray() {
		_navOptions.add("Favorites");
		_navOptions.add("Help");
	}


	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	@SuppressLint("SimpleDateFormat")
	private ArrayList<String> getDate(){
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM. dd");
		String _formattedDate = df.format(c.getTime());

		c.add(Calendar.DATE, 1);  // number of days to add
		String _formattedDateAdd1 = df.format(c.getTime());

		c.add(Calendar.DATE, 1);  // number of days to add
		String _formattedDateAdd2 = df.format(c.getTime());

		c.add(Calendar.DATE, 1);  // number of days to add
		String _formattedDateAdd3 = df.format(c.getTime());

		c.add(Calendar.DATE, 1);  // number of days to add
		String _formattedDateAdd4 = df.format(c.getTime());

		ArrayList<String> list = new ArrayList<String>();
		list.add(_formattedDate);
		list.add(_formattedDateAdd1);
		list.add(_formattedDateAdd2);
		list.add(_formattedDateAdd3);
		list.add(_formattedDateAdd4);

		return list;

	}

	/**
	 * Gets the favs.
	 *
	 * @return the favs
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getFavs (){

		Object stored = ReadWrite.readStringObject(_context, "favsArray", false);
		ArrayList<String> favs;
		if (stored == null) {
			Log.i("HISTORY", "NO HISTORY FILE FOUND");
			favs = new ArrayList<String>();
		} else {
			favs = (ArrayList<String>) stored;
		}
		return favs;
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();

	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		_favorites = getFavs();
		String getString = getIntent().getStringExtra("item");
		_inputText.setText(getString);
		//		_player.start();

	}



	/// GET WEATHER 
	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FormFragment.FormListener#getWeather(java.lang.String)
	 */
	@SuppressLint("HandlerLeak")
	@Override
	public void getWeather(String zip) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		netCon();
		Log.i("ONLICK", "hit");
		if(_connected){
			_inputHolder = _inputText.getText().toString();
			if (_inputText.getText().toString().length() == 5) {

				
				
				// DISMISSES KEYBOARD on CLICK 
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(_inputText.getWindowToken(), 0);


				Handler weatherHandler = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);

						Log.i("HANDLER", "is being hit");
						if (msg.arg1 == RESULT_OK && msg.obj != null) {
							String messageString = msg.obj.toString();
							Log.i("URL_RESPONSE", messageString);

							try {
								// Pull JSON data from API
								JSONObject json = new JSONObject(messageString);
								JSONObject data = json.getJSONObject("data");
								Boolean error = data.has("error");
								if (error) {

									Toast toast = Toast.makeText(_context,"Sorry we were not able to find the zip you entered", Toast.LENGTH_SHORT);
									toast.show();
								} else {

									displayFromWrite();

								}
							} catch (JSONException e) {
								Log.e("JSON ERROR", e.toString());
							} 
							if (_optionSelected == 1) {
								Intent intent = new Intent(_context,Forecast.class);
								intent.putExtra("URI", 0);

								startActivityForResult(intent, 0);
							}

							if (_optionSelected == 2) {
								Intent intent = new Intent(_context,Forecast.class);
								intent.putExtra("URI", 1);

								startActivityForResult(intent, 0);
							}

							if (_optionSelected == 3) {
								Intent intent = new Intent(_context,Forecast.class);
								intent.putExtra("URI", 2);

								startActivityForResult(intent, 0);
							}

							if (_optionSelected == 4) {
								Intent intent = new Intent(_context,Forecast.class);
								intent.putExtra("URI", 3);

								startActivityForResult(intent, 0);
							}

							if (_optionSelected == 5) {
								Intent intent = new Intent(_context,Forecast.class);
								intent.putExtra("URI", 4);

								startActivityForResult(intent, 0);
							}

						}

					}

				};


				try {
					_finalURLString = getURLString(_inputText.getText().toString());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Messenger weatherMessenger = new Messenger(weatherHandler);
				Intent startWeatherIntent = new Intent(_context, WeatherService.class);
				startWeatherIntent.putExtra(WeatherService.MESSENGER_KEY, weatherMessenger);
				startWeatherIntent.putExtra(WeatherService.FINALURL_KEY, _finalURLString);

				// Start the service remember that the handleMessage method will not be called until the Service is done. 
				startService(startWeatherIntent);


			} else if (_inputText.getText().toString().length() !=5) {

				Toast toast = Toast.makeText(getApplicationContext(), R.string.enter_a_valid_zip_code_, Toast.LENGTH_LONG);
				toast.show();
				


			}


		};
		
	}

	/**
	 * Gets the network info.
	 *
	 * @return the network info
	 */
	private boolean getNetworkInfo () {
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		boolean _isWifiConn = networkInfo.isConnected();
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean _isMobileConn = networkInfo.isConnected();


		if (!_isMobileConn) {
			// AlertDialog if not MOBLILE is not connected
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Hello!");
			alert.setMessage("We noticed that you do not have mobile data enabled on this device. To use this applicaiton independant of Wi-Fi, please enable your data connection");
			alert.setCancelable(false);
			alert.setPositiveButton("Ok, Thank you", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.cancel();
				}
			});
			alert.show();
		}

		if (!_isWifiConn) {
			// AlertDialog if not MOBLILE is not connected
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Hello!");
			alert.setMessage("We noticed that you do not have Wi-fi enabled or are not connected to a Wi-fi on this device. To save data usage, please enable your Wi-Fi connection");
			alert.setCancelable(false);
			alert.setPositiveButton("Ok, Thank you", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.cancel();
				}
			});
			alert.show();
		}

		return _isMobileConn && _isWifiConn;



	}

	// DETECT NETWORK CONNECTION

	/**
	 * Net con.
	 */
	private void netCon(){

		_connected = NetworkConnection.getConnectionStatus(_context);
		if (_connected) {
			Log.i("NETWORK CONNECTION",
					NetworkConnection.getConnectionType(_context));

		} else {

			// AlertDialog if not connected
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Oops!");
			alert.setMessage("Please check your network connection and try again.");
			alert.setCancelable(false);
			alert.setPositiveButton("Hiyah!",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(
						DialogInterface dialogInterface, int i) {
					dialogInterface.cancel();
				}
			});
			alert.show();


		}		 
	}


	/**
	 * Gets the uRL string.
	 *
	 * @param zip the zip
	 * @return the uRL string
	 * @throws MalformedURLException the malformed url exception
	 */
	private  String getURLString (String zip) throws MalformedURLException {

		String finalURLString = "";
		// This will change to current plus 5 day to fit week 2 assignment and CP query
		// http://api.worldweatheronline.com/free/v1/weather.ashx?q=32707&format=json&num_of_days=5&key=p5rbnjhy84gpvc7arr3qb38c
		String _baseURL = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=";
		String apiKey = "p5rbnjhy84gpvc7arr3qb38c";

		String qs = "";

		try {
			qs = URLEncoder.encode(zip, "UTF-8");

			finalURLString = _baseURL + qs + "&format=json&num_of_days=5"+ "&key=" +apiKey;
		} catch (Exception e) {
			Log.e("BAD URL", "ENCODING PROBLEM");
			finalURLString = null;
		}

		return finalURLString;
	}


	/**
	 * Display from write.
	 *
	 * @throws JSONException the jSON exception
	 */
	private void displayFromWrite() throws JSONException{
		String fileContents = ReadWrite.readStringFile(_context, "weatherData", false);

		JSONObject mainObj = new JSONObject(fileContents);
		_dataObj = mainObj.getJSONObject("data");
		JSONArray conditionsObj = _dataObj.getJSONArray("current_condition");
		JSONObject weatherObj = conditionsObj.getJSONObject(0);
		_temp = weatherObj.getString("temp_F");
		_humidity = weatherObj.getString("humidity");
		_windSpeed = weatherObj.getString("windspeedMiles");
		_windDirection = weatherObj.getString("winddir16Point");
		JSONArray weatherDesc = weatherObj.getJSONArray("weatherDesc");
		_weatherDescValue = weatherDesc.getJSONObject(0).getString("value");
		_zip = _dataObj.getJSONArray("request").getJSONObject(0).getString("query");
		
		TempHolder._temp = _temp;


		displayData();
	}

	/**
	 * Display data.
	 */
	private void displayData(){

		TextView temp =	(TextView) findViewById(R.id.data_tempF); 
		temp.setText(_temp + " F°");

		TextView humid = (TextView) findViewById(R.id.data_humidity);
		humid.setText(_humidity + "%");

		TextView windSpeed= (TextView) findViewById(R.id.data_windSpeed);
		windSpeed.setText(_windSpeed + " MPH");
		((TextView) findViewById(R.id.data_windDirection)).setText(_windDirection);

		((TextView) findViewById(R.id.data_location)).setText(_zip);
		((TextView) findViewById(R.id.data_location)).setText(_zip);
		ImageView imageView = (ImageView) findViewById(R.id.weatherDesc);
		imageView.setImageResource(ImageConverter.getConditionImage(_weatherDescValue));
	}

	/// SAVE FAVORITES
	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FormFragment.FormListener#saveFavorite(java.lang.String)
	 */
	@Override
	public void saveFavorite(String zip) {
		// TODO Auto-generated method stub
		if (_inputText.getText().toString().length() == 5) {

			if (_favorites.contains(_inputText.getText().toString())) {
				Toast.makeText(_context, _inputText.getText().toString() + " already exists in Favorites", Toast.LENGTH_SHORT).show();
			} else{
				_favorites.add(_inputText.getText().toString());	
				ReadWrite.storeObjectFile(_context, Favorites.FILE_NAME, _favorites, false);
				Toast.makeText(_context, "Success! " + _inputText.getText().toString() + " was saved to Favorites!", Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(_context, R.string.enter_a_valid_zip_code_, Toast.LENGTH_SHORT).show();
		}


	}

	/// VIEW FAVORITES 
	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FormFragment.FormListener#viewFavorites()
	 */
	@Override
	public void viewFavorites() {
		// TODO Auto-generated method stub
		if (_favorites.size() != 0) {

			Intent viewFav = new Intent(_context, Favorites.class);
			startActivityForResult(viewFav, REQUEST_CODE);
		}
	}


	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FormFragment.FormListener#showMap(java.lang.String)
	 */
	@Override
	public void showMap(String zip) {
		// TODO Auto-generated method stub
		if (_inputText.getText().toString().length() == 5) {
			// Map point based on Zip
			Uri location = Uri.parse("geo:0,0?q=" + _inputText.getText().toString());
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
			startActivity(mapIntent);

		} else {
			Toast.makeText(_context, R.string.enter_a_valid_zip_code_, Toast.LENGTH_SHORT).show();
		}

	}


	// GPS Method 
	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FormFragment.FormListener#getZipFromGPS()
	 */
	@Override
	public  void getZipFromGPS() {
		//		Toast.makeText(_context, "This Button Works", Toast.LENGTH_SHORT).show();
		netCon();
		if (_connected) {
			// Grab Location from location client 
			mCurrentLocation = mLocationClient.getLastLocation();
			Log.i("TAG", mCurrentLocation.toString());
			// Convert lat/long into an actual address 
			Geocoder geocoder = new Geocoder(_context, Locale.getDefault());
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(
						mCurrentLocation.getLatitude(),
						mCurrentLocation.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e("LocationSampleActivity",
						"IO Exception in getFromLocation()");
				e1.printStackTrace();
			}
			Log.i("ADDRESS IS", addresses.toString());
			// Assign the returned Value from getPostalCode in the address to _zip
			_zip = addresses.get(0).getPostalCode();
			// Set the string into the _inputText 
			_inputText.setText(_zip);
						
		} 
	
	}

	/* (non-Javadoc)
	 * @see com.petrockz.climacast.FavoritesFragment.FavoritesListener#onFavoriteSelected(java.lang.String)
	 */
	@Override
	public void onFavoriteSelected(String zip) {
		_inputText.setText(zip);


	}


	// GPS CODE 
	private final static int
	CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Define a DialogFragment that displays the error dialog
	/**
	 * The Class ErrorDialogFragment.
	 */
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;
		// Default constructor. Sets the dialog field to null
		/**
		 * Instantiates a new error dialog fragment.
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}
		// Set the dialog to display
		/**
		 * Sets the dialog.
		 *
		 * @param dialog the new dialog
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
		// Return a Dialog to the DialogFragment.
		/* (non-Javadoc)
		 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Handle results returned to the FragmentActivity
	 * by Google Play services
	 */
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(
			int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST :
			/*
			 * If the result code is Activity.RESULT_OK, try
			 * to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK :
				/*
				 * Try the request again
				 */

				break;
			}

		}
	}

	/**
	 * Services connected.
	 *
	 * @return true, if successful
	 */
	@SuppressWarnings("unused")
	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode =
				GooglePlayServicesUtil.
				isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates",
					"Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			int errorCode = connectionResult.getErrorCode();
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					errorCode,
					this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment =
						new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
			}
		}
		return _connected;
	}

	// Define Location Services Callbacks

	/**
	 * Gets the support fragment manager.
	 *
	 * @return the support fragment manager
	 */
	private  FragmentManager getSupportFragmentManager() {
		return null;


	}


	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		//		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
	}


	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * Called by Location Services if the attempt to
	 * Location Services fails.
	 */
	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects.
		 * If the error has a resolution, try sending an Intent to
		 * start a Google Play services activity that can resolve
		 * error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} 
	}


	@Override
	public void showActivityFromSelection(int position) {
		switch (position) {
		case 1:
			Intent viewFav = new Intent(_context, Favorites.class);
			startActivityForResult(viewFav, REQUEST_CODE);
			break;

		default:
			break;
		}

	}



	protected void launchActivity(int itemPosition, Context _Context) {
	     switch (itemPosition) {
	        case 1:
	        	Intent viewFav = new Intent(_Context, Favorites.class);
				startActivityForResult(viewFav, REQUEST_CODE);
				Log.d("THIS WAS ", "HIT");
	           break;
	      
	         
	     }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_favorites:
	        	Intent viewFav = new Intent(_context, Favorites.class);
				startActivityForResult(viewFav, REQUEST_CODE);
	            return true;
	        case R.id.action_about:
	            Intent viewAbout = new Intent(_context, About.class);
				startActivityForResult(viewAbout, REQUEST_CODE);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


}

/*
 *  This works to a point .. can't figure out how to launch the activity on select. 
 * 
 */
//	private void navSelector() {
//		_navSelector = (Spinner)findViewById(R.id.spinner1) ;
//
//		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
//				android.R.layout.simple_spinner_dropdown_item);
//		//		ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_list_item_1,_navOptions);
//		//		_navSelector.setAdapter(mSpinnerAdapter); 
//
//		OnNavigationListener mOnNavigationListener = null;
//		
//		ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//		actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
//		
//
//		mOnNavigationListener = new OnNavigationListener() {
//			// Get the same strings provided for the drop-down's ArrayAdapter
//			String[] strings = getResources().getStringArray(R.array.action_list);
//
//			@Override
//			public boolean onNavigationItemSelected(int position, long itemId) {
//				
//				launchActivity(position, _context);
//				// Create new fragment from our own Fragment class
//				ListContentFragment newFragment = new ListContentFragment();
//				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//				// Replace whatever is in the fragment container with this fragment
//				//  and give the fragment a tag name equal to the string at the position selected
//				ft.replace(R.id.fragment_container, newFragment, strings[position]);
//				// Apply changes
//				ft.commit();
//				
//				
//				return true;
//				
//			}
//
//		};
//
//	}
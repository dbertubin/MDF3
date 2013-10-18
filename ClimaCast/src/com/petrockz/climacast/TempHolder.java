package com.petrockz.climacast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.petrockz.chucknorris.lib.NetworkConnection;

public class TempHolder extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	public static String _temp;
	public static String _zip;
	static LocationClient mLocationClient;
	static Location mCurrentLocation;

	Button _getWeatherButton;
	Button _saveFavButton;
	Button _viewFavButton;
	Button _showMapButton;
	Button _getGPS; 

	EditText _inputText;
	GridLayout _resultsGrid;
	GridLayout _5dayGrid;
	Boolean _isMobileConn;
	Boolean _isWifiConn;

	String _baseURL;
	String _finalURLString;
	String _inputHolder;


	String _humidity ;
	String _windSpeed ;
	String _windDirection ;
	String _weatherDescValue;


	JSONObject _dataObj;
	int _optionSelected;

	private static boolean _connected;
	private static Context _context;
	public  static String setTemp(String temp){

		_temp = temp;

		return _temp;

	}

	public static String setZip(String zip) {

		_zip = zip;

		return _zip;
	}




	public static String getZipFromGPS() {
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

			

		};
		return _zip;
	
	}

	private static void netCon(){

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

	private ConnectionResult connectionResult;

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



	/// GET WEATHER 

	@SuppressLint("HandlerLeak")
	public void getWeather(String zip) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		netCon();
		Log.i("ONLICK", "hit");
		if(_connected){
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


	/**
	 * Gets the network info.
	 *
	 * @return the network info
	 */
	@SuppressWarnings("unused")
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


}

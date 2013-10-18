package com.petrockz.climacast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationClient;
import com.petrockz.chucknorris.lib.NetworkConnection;

public class TempHolder {

	public static String _temp;
	public static String _zip;
	static LocationClient mLocationClient;
	static Location mCurrentLocation;



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

}

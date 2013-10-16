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

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class FormFragment.
 */
public class FormFragment extends Fragment {

	private FormListener listener; 

	static Context _context;
	Button _getWeatherButton;
	Button _saveFavButton;
	Button _viewFavButton;
	Button _showMapButton;
	Button _getGPS;
	EditText _inputText;
	String _zip;

	ListView _listView;
	ArrayList<String> _activities = new ArrayList<String>();

	/**
	 * The listener interface for receiving form events.
	 * The class that is interested in processing a form
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addFormListener<code> method. When
	 * the form event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see FormEvent
	 */
	public interface FormListener{

		/**
		 * Gets the weather.
		 *
		 * @param zip the zip
		 * @return the weather
		 */
		public void getWeather(String zip);

		/**
		 * Save favorite.
		 *
		 * @param zip the zip
		 */
		public void saveFavorite(String zip);

		/**
		 * View favorites.
		 */
		public void viewFavorites();

		/**
		 * Show map.
		 *
		 * @param zip the zip
		 */
		public void showMap(String zip);

		/**
		 * Gets the zip from gps.
		 *
		 * @return the zip from gps
		 */
		public void getZipFromGPS(); 

		public void showActivityFromSelection(int position);
		

	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.form, container, false);

		_inputText = (EditText) view.findViewById(R.id.editText);
		_inputText.setInputType(InputType.TYPE_CLASS_NUMBER);

		_zip = _inputText.getText().toString();

		/// SAVE FAVORITES 

		_saveFavButton = (Button) view.findViewById(R.id.saveFav);
		_saveFavButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				listener.saveFavorite(_zip);

			}	
		});


		/// GET GPS 
		_getGPS = (Button) view.findViewById(R.id.getGps);
		_getGPS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.getZipFromGPS();
			}
		});


		/// GET MAP 
		_showMapButton = (Button) view.findViewById(R.id.show);
		_showMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				listener.showMap(_zip);	

			}
		}); 

		/// VIEW FAVORITES 
		_viewFavButton  = (Button) view.findViewById(R.id.viewFav);
		_viewFavButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				listener.viewFavorites();

			}
		});


		/// GET WEATHER 

		_getWeatherButton = (Button)view.findViewById(R.id.startButton);
		_getWeatherButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("HandlerLeak")
			@Override
			public void onClick(final View v) {

				listener.getWeather(_zip);
			}

		});
		
		
		
				
				
		return view;
	
	}

	


	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (FormListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must be FormListener");
		}
	}

}

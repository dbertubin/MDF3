/*
 * project	ClimaCast
 * 
 * package 	com.petrockz.climacast
 * 
 * @author 	${author}
 * 
 * date 	Oct 10, 2013
 */

package com.petrockz.climacast;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class FavoritesFragment.
 */
public class FavoritesFragment extends Fragment implements OnItemClickListener{
	
	public static final String FILE_NAME = "favsArray";
	ListView _listView;
	Context _context;
	ArrayList<String> _favorites = new ArrayList<String>();
	
	/**
	 * The listener interface for receiving favorites events.
	 * The class that is interested in processing a favorites
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addFavoritesListener<code> method. When
	 * the favorites event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see FavoritesEvent
	 */
	public interface FavoritesListener{
		
		/**
		 * On favorite selected.
		 *
		 * @param zip the zip
		 */
		public void onFavoriteSelected(String zip);
	
	}

	private FavoritesListener _listener;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		
		try {
		_listener = (FavoritesListener) activity;	
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must be FavoritesListener");
		}
		Log.i("FRAG", "LISTENER ATTACHED");
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		Log.i("VIEW ", "ATTEMPTING TO CREATE");
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.listview, container, false);
		Log.i("VIEW ", "HIT IN FRAG");
		
		
		
		_favorites = MainActivity._favorites;
		
			/// ATTACH LIST ADAPTER
		_listView = (ListView) view.findViewById(R.id.fav_list);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,_favorites);
		_listView.setAdapter(arrayAdapter); 
		
		/// LIST ONITEMCLICKED 
		_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// 
				_listener.onFavoriteSelected(_favorites.get(position));
			}
		});
		
		
		return view;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
}

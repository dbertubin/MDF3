package com.petrockz.climacast;

import com.petrockz.climacast.FormFragment.FormListener;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class ClimaCastWidgetProvider extends AppWidgetProvider{

	@SuppressWarnings("unused")
	private WidgetListener listener;
	private String _zip;
	public interface WidgetListener{

		public String getZipFromGPS();

		public void getWeather(String zip);

	}

	
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		
		
		if (listener != null) {
			Log.i("this", "worked");
		} else {
			Log.i("this", "didnt work");
		}
	}


	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch MainActivity
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);
	
			
				
			views.setTextViewText(R.id.widget_temp, "76¼");

			if (listener != null) {
				Log.i("this", "worked");
			} else {
				Log.i("this", "didnt work");
			}

			// Tell the AppWidgetManager to perform an update on the current app widget

			appWidgetManager.updateAppWidget(appWidgetId, views);

		}
	}



	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

}


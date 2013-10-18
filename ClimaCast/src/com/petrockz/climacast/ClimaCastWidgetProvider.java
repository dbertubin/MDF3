package com.petrockz.climacast;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ClimaCastWidgetProvider extends AppWidgetProvider{

	@SuppressWarnings("unused")
	private WidgetListener listener;
	@SuppressWarnings("unused")
	private String _zip;
	
	public interface WidgetListener{

		public  void getZipFromGPS();

		public void getWeather(String zip);

	}

	
	/*
	 * How do I trigger the get getZipFromGPS and then the getWeather methods from here ????????
	 * (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onEnabled(android.content.Context)
	 */
	
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		

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
	
			
				
			views.setTextViewText(R.id.widget_temp, TempHolder._temp +"¼");

			

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


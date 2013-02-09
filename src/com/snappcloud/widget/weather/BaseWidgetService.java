package com.snappcloud.widget.weather;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

public abstract class BaseWidgetService extends IntentService
{
	public static final String LOG_TAG = "BaseWidgetService";
	protected static final String PREFS_NAME = "WeatherWidget";

	public BaseWidgetService(String name) {
		super(name);
	}
	
	private void updateWidgets(AppWidgetManager appWidgetManager, int[] allWidgetIds, RemoteViews remoteViews)
	{
		for ( int x=0; x<allWidgetIds.length; x++)
		{
			appWidgetManager.updateAppWidget(allWidgetIds[x], remoteViews);
		}
	}
	
	protected void showError(String message, AppWidgetManager appWidgetManager, int[] allWidgetIds)
	{
		RemoteViews errorRemoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.error);
		errorRemoteViews.setTextViewText(R.id.errorMessage, message);
		updateWidgets(appWidgetManager, allWidgetIds, errorRemoteViews);
	}
	
	protected void setClickListeners(RemoteViews remoteViews, int allWidgetIds[])
	{
		for ( int x=0; x<allWidgetIds.length; x++)
		{
			Intent btn_active = new Intent(getApplicationContext(), WeatherWidgetConfigure.class);
			btn_active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, allWidgetIds[x]);
			PendingIntent actionPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, btn_active, 0);
			remoteViews.setOnClickPendingIntent(R.id.button, actionPendingIntent);
		}
	}
	
	public void setRemoteView(AppWidgetManager appWidgetManager, RemoteViews remoteViews, WidgetData widgetData, int[] allWidgetIds) throws Exception
	{
		try
		{
			URL imageUrl = new URL(widgetData.getCurrentConditionsImgUrl()); 
			Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
			if ( bmp != null )
			{
				remoteViews.setImageViewBitmap(R.id.curCondImgView, bmp);
			}
			else
			{
				Log.e(LOG_TAG, "Error loading image from: " + imageUrl);
			}
		} 
		catch (Exception e) 
		{
			Log.e(LOG_TAG, "Invalid img URL: " + widgetData.getCurrentConditionsImgUrl());
		}
		
		if ( StringUtils.isNotBlank( widgetData.getPrediction() ))
		{
			remoteViews.setTextViewText(R.id.curCondTextView, widgetData.getPrediction());
		}
		else
		{
			remoteViews.setTextViewText(R.id.curCondTextView, "Error loading weather");
		}
		
		// trim unit off temp
		String temp = widgetData.getTemperature();
		if ( StringUtils.isNotBlank(temp) )
		{
			temp = temp.substring(0, temp.length() - 1);
		}
		remoteViews.setTextViewText(R.id.tempTextView, temp);
		
		String high = widgetData.getHighTemp();
		if ( StringUtils.isNotBlank(high) )
		{
			high = high.substring(0, high.length() - 1);
			remoteViews.setTextViewText(R.id.highTextView, "H: " + high);
		}
		
		String low = widgetData.getLowTemp();
		if ( StringUtils.isNotBlank(low) )
		{
			low = low.substring(0, low.length() - 1);
			remoteViews.setTextViewText(R.id.lowTextView, "L: " + low);
		}
						
		if ( StringUtils.isNotBlank( widgetData.getLocation() ))
		{
			remoteViews.setTextViewText(R.id.locationTextView, widgetData.getLocation());
		}
		
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
		remoteViews.setTextViewText(R.id.dowTextView, formatter.format(now));

		formatter = new SimpleDateFormat("EEE");
		remoteViews.setTextViewText(R.id.dowTextViewShort, formatter.format(now));
		
		formatter = new SimpleDateFormat("hh:mm a");
		remoteViews.setTextViewText(R.id.timeTextView, formatter.format(now));
		
		updateWidgets(appWidgetManager, allWidgetIds, remoteViews);
	}
	
	protected abstract WidgetData getWidgetData() throws Exception;
	
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		Log.d(LOG_TAG, "onStart()");
		
		int appWidgetId = intent.getExtras() == null ? AppWidgetManager.INVALID_APPWIDGET_ID : intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
		Log.d(LOG_TAG, "appWidgetId -> " + appWidgetId);
		RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.weather);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		ComponentName thisWidget = new ComponentName(getApplicationContext(), WeatherWidget.class);
		int[] allWidgetIds = {};
		
		if ( appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID )
		{
			allWidgetIds = new int[1];
			allWidgetIds[0] = appWidgetId;
		}
		else
		{
			Log.d(LOG_TAG, "Update all appWidgetIds");
			allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		}
		
		setClickListeners(remoteViews, allWidgetIds);
		
		boolean isOnline = HTTPUtil.isOnline(this); 
		if ( !isOnline )
		{
			Log.e(LOG_TAG, "No network connection");
			showError(getString(R.string.error_no_network), appWidgetManager, allWidgetIds);
		}
		else
		{
			Log.i(LOG_TAG, "Is connected to network");
			
			try
			{
				WidgetData widgetData = getWidgetData();
				setRemoteView(appWidgetManager, remoteViews, widgetData, allWidgetIds);
			}
			catch (Exception e)
			{
				showError(getString(R.string.error_occurred), appWidgetManager, allWidgetIds);
			}
		}
	}

}

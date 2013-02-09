package com.snappcloud.widget.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

public class WeatherWidget extends AppWidgetProvider {
	
	private final String LOG_TAG = "WEATHER";
	public static final int UPDATE_RATE = 15*60*1000;  // 15 minutes
	public static final int CLOCK_UPDATE_RATE = 30*1000;  // 30 seconds
	public static String ACTION_WIDGET_RECEIVER = "com.snappcloud.widget.weather.ACTION";
	public static String UPDATE_WIDGET_RECEIVER = "com.snappcloud.widget.weather.REFRESH";
	public static String CLOCK_WIDGET_RECEIVER = "com.snappcloud.widget.weather.CLOCK";
	
	
	@Override
    public void onDeleted(Context context, int[] appWidgetIds) 
	{
		Log.i(LOG_TAG, "onDeleted()");
        super.onDeleted(context, appWidgetIds);
    }
	
	@Override
	public void onEnabled(Context context) 
	{
		super.onEnabled(context);
	}
	
	@Override
	public void onDisabled(Context context) 
	{
		Log.i(LOG_TAG, "onDisabled()");
		cancelAlarm(context);
		stopClock(context);
		context.stopService(new Intent(context, UpdateService.class));
		context.stopService(new Intent(context, ClockService.class));
		super.onDisabled(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) 
	{
		Log.d(LOG_TAG, "onUpdate()");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather);
		for (int appWidgetId : appWidgetIds) {
			setAlarm(context, UPDATE_RATE);
			startClock(context);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
    public void onReceive(Context context, Intent intent) {
		Log.d(LOG_TAG, "onReceive():: Intent-> action: " + intent.getAction() 
				+ ", extras: " + (intent.getExtras() != null ? intent.getExtras().toString() : "null") );

		if ( intent == null || intent.getAction() == null )
		{
			super.onReceive(context, intent);
			return;
		}
		else if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) 
		{
			Log.i(LOG_TAG, "Start service intent");
			if ( intent.getExtras() != null )
			{
				int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
				Log.d(LOG_TAG, "appWidgetId->" + appWidgetId);
				Intent msgIntent = new Intent(context, UpdateService.class);
				msgIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
				context.startService(msgIntent);
			}
		}
		else if (intent.getAction().equals(CLOCK_WIDGET_RECEIVER)) 
		{
			Log.i(LOG_TAG, "Start clock service intent");
			if ( intent.getExtras() != null )
			{
				int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
				Log.d(LOG_TAG, "appWidgetId->" + appWidgetId);
				Intent msgIntent = new Intent(context, ClockService.class);
				msgIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
				context.startService(msgIntent);
			}
		}
		else if (intent.getAction().equals(UPDATE_WIDGET_RECEIVER)) 
		{
			Log.i(LOG_TAG, "Update widget intent");
			if ( intent.getExtras() != null )
			{
				int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
				Log.d(LOG_TAG, "appWidgetId->" + appWidgetId);
				int[] appWidgetIds = {appWidgetId};
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				onUpdate(context, appWidgetManager, appWidgetIds);
			}
		}
		else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
		{
			boolean online = HTTPUtil.isOnline(context);
			Log.i(LOG_TAG, "Network connection status changed -> " + (online ? "online" : "offline"));
			if ( online )
			{
				Intent msgIntent = new Intent(context, UpdateService.class);
				context.startService(msgIntent);
			}
			else
			{
				Intent msgIntent = new Intent(context, UpdateService.class);
				context.stopService(msgIntent);
			}
		}
		super.onReceive(context, intent);
	}

	public void setAlarm(Context context, int updateRate) 
	{
		Log.i(LOG_TAG, "setAlarm() -> " + updateRate);
        PendingIntent newPending = makeControlPendingIntent(context,ACTION_WIDGET_RECEIVER, UpdateService.class);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), updateRate, newPending);
    }
	
	public void cancelAlarm(Context context) 
	{
		Log.i(LOG_TAG, "cancelAlarm()");
        PendingIntent newPending = makeControlPendingIntent(context,ACTION_WIDGET_RECEIVER, UpdateService.class);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(newPending);
    }
	
	public void startClock(Context context) 
	{
		Log.i(LOG_TAG, "startClock()");
        PendingIntent newPending = makeControlPendingIntent(context,CLOCK_WIDGET_RECEIVER, ClockService.class);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), CLOCK_UPDATE_RATE, newPending);
    }
	
	public void stopClock(Context context) 
	{
		Log.i(LOG_TAG, "stopClock()");
        PendingIntent newPending = makeControlPendingIntent(context,CLOCK_WIDGET_RECEIVER, ClockService.class);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(newPending);
    }
	
	public PendingIntent makeControlPendingIntent(Context context, String command, Class classObj) 
	{
		Log.d(LOG_TAG, "makeControlPendingIntent()");
        Intent intent = new Intent(context, classObj);
        intent.setAction(command);
        return(PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
	
}
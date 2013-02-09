package com.snappcloud.widget.weather;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import org.apache.commons.lang.StringUtils;

public class UpdateService extends BaseWidgetService 
{
	public static final String LOG_TAG = "UpdateService";
	
	public UpdateService()
	{
		super(UpdateService.class.getSimpleName());
	}
	
	private void storeWeather(WidgetData widgetData)
	{
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("currentConditionsImgUrl", widgetData.getCurrentConditionsImgUrl());
		editor.putString("highTemp", widgetData.getHighTemp());
		editor.putString("location", widgetData.getLocation());
		editor.putString("lowTemp", widgetData.getLowTemp());
		editor.putString("prediction", widgetData.getPrediction());
		editor.putString("temperature", widgetData.getTemperature());
		editor.commit();
	}
	
	protected WidgetData getWidgetData() throws Exception
	{
		String url = getString(R.string.xml_feed);
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
		String zipcode = prefs.getString("zipcode", "20007");
		if ( StringUtils.isBlank(zipcode) )
		{
			zipcode = "20007";
		}
		Log.i(LOG_TAG, "Zipcode:: " + zipcode);
		url += zipcode;
		
		XMLWeatherBugFeed feed = new XMLWeatherBugFeed();
    	WeatherResponse currentWeather = feed.loadXmlFromNetwork(url);
    	
    	url = getString(R.string.xml_forecast_feed);
    	url += zipcode;
    	
    	XMLWeatherBugForecastFeed forecastFeed = new XMLWeatherBugForecastFeed();
    	WeatherResponse forecast = forecastFeed.loadXmlFromNetwork(url);
    	
    	WidgetData widgetData = new WidgetData();
    	
    	widgetData.setCurrentConditionsImgUrl(currentWeather.getCurrentConditionsImgUrl());
    	widgetData.setHighTemp(currentWeather.getHighTemp());
    	widgetData.setLocation(currentWeather.getLocation());
    	widgetData.setLowTemp(currentWeather.getLowTemp());
    	widgetData.setPrediction(forecast.getPrediction());
    	widgetData.setTemperature(currentWeather.getTemperature());
    	
    	storeWeather(widgetData);
		
		return widgetData;
    	
    	
	}

}

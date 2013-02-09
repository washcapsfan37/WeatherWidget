package com.snappcloud.widget.weather;

import android.content.SharedPreferences;

public class ClockService extends BaseWidgetService 
{
	
	public static final String LOG_TAG = "ClockService";
	
	public ClockService() {
		super(ClockService.class.getSimpleName());
	}

	@Override
	protected WidgetData getWidgetData() throws Exception
	{
		WidgetData widgetData = new WidgetData();
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		widgetData.setCurrentConditionsImgUrl( settings.getString("currentConditionsImgUrl", "") );
		widgetData.setHighTemp( settings.getString("highTemp", "") );
		widgetData.setLocation( settings.getString("location", "") );
		widgetData.setLowTemp( settings.getString("lowTemp", "") );
		widgetData.setPrediction( settings.getString("prediction", "") );
		widgetData.setTemperature( settings.getString("temperature", "") );
		
		return widgetData;
	}

}

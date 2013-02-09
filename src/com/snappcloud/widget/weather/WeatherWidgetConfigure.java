package com.snappcloud.widget.weather;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WeatherWidgetConfigure extends Activity 
{
	
	private final String LOG_TAG = WeatherWidgetConfigure.class.getSimpleName();
	
	private int appWidgetId;
	private int[] allWidgetIds = {};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        // get the appWidgetId of the appWidget being configured
        Intent launchIntent = getIntent();
        Bundle extras = launchIntent.getExtras();
        if ( extras != null )
        {
        	Log.d(LOG_TAG, "Intent has extras");
        	appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        else
        {
        	Log.d(LOG_TAG, "Intent has no extras");
        	appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        }
        Log.d(LOG_TAG, "received launch intent from appWidgetId->" + appWidgetId);
        
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		ComponentName thisWidget = new ComponentName(getApplicationContext(), WeatherWidget.class);
        
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
        
        setContentView(R.layout.configuration);

        Intent cancelResultValue = new Intent();
        cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, cancelResultValue);
        
        Button cancel = (Button) findViewById(R.id.cancelbutton);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    // finish sends the already configured cancel result
                    // and closes activity
                    finish();
            }
        });
        
        Button ok = (Button) findViewById(R.id.okbutton);
        final EditText zipcode = (EditText) findViewById(R.id.zipcode);
        ok.setOnClickListener(new OnClickListener() 
        {
        	@Override
        	public void onClick(View arg0) 
        	{
        		String val = zipcode.getText().toString();
        		
        		if ( StringUtils.isBlank(val) || !val.matches("\\d{5}"))
        		{
        			zipcode.setError("U.S. Zip Codes must be 5 digits.");
        			return;
        		}
        		
        		SharedPreferences prefs = getSharedPreferences("prefs", 0);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("zipcode", val);
                edit.commit();
                
                // change the result to OK
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                
                Context context = getApplicationContext();
                /*
                Intent msgIntent = new Intent(context, UpdateService.class);
                msgIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                context.startService(msgIntent);
                */
                for ( int x=0; x<allWidgetIds.length; x++)
        		{
	                Intent intent = new Intent(context, WeatherWidget.class);
	                intent.setAction(WeatherWidget.UPDATE_WIDGET_RECEIVER);
	                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	                context.sendBroadcast(intent);
        		}
                
                finish();
            }
        });

	}

}

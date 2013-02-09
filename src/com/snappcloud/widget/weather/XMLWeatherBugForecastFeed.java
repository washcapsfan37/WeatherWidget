package com.snappcloud.widget.weather;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XMLWeatherBugForecastFeed extends XMLWeatherFeedAbstract {

private final String LOG_TAG = "XMLWeatherFeedAbstract";
	
	private String getAttribute(XmlPullParser parser, String attributeName) 
	{
	    Map<String,String> attrs=null;
	    int acount=parser.getAttributeCount();
	    if(acount > 0) 
	    {
	        attrs = new HashMap<String,String>(acount);
	        for(int x=0;x<acount;x++) 
	        {
	            String aName = parser.getAttributeName(x);
	            if ( attributeName.equalsIgnoreCase(aName) )
	            {
	            	String aValue = parser.getAttributeValue(x);
	            	return aValue;
	            }
	        }
	    }
	    else 
	    {
	        //throw new Exception("Required entity attributes missing");
	    }
	    return null;
	}
	
	@Override
	public WeatherResponse parse(String xml) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput( new StringReader (xml) );
        int eventType = parser.getEventType();
        
        WeatherResponse response = null;
        boolean foundFirstForecast = false;
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
        	switch(eventType) {
	        	case XmlPullParser.START_DOCUMENT:
	        		response = new WeatherResponse();
	        		break;
	        	case XmlPullParser.START_TAG:
	        		String prefix = parser.getPrefix();
	        		String tagName = parser.getName();
	        		Log.d(LOG_TAG, "Found tag <" + tagName + ">");
	        		if(tagName.equalsIgnoreCase("image")) 
	        		{
	        			String icon = parser.nextText();
	        			Log.d(LOG_TAG, "Setting icon: " + icon);
	        			if ( !foundFirstForecast ) response.setCurrentConditionsImgUrl(icon);
	        		}
	        		else if(tagName.equalsIgnoreCase("prediction")) 
	        		{
	        			String prediction = parser.nextText();
	        			Log.d(LOG_TAG, "Setting prediction: " + prediction);
	        			if ( !foundFirstForecast ) response.setPrediction(prediction);
	        		}
	        		break;
	        	case XmlPullParser.END_TAG:
	        		String endtagName = parser.getName();
	        		if ( StringUtils.equals("forecast", endtagName))
	        		{
	        			foundFirstForecast = true;
	        		}
	        		break;
	        	default: break;
        	}
	        eventType = parser.next();
        }
        
        return response;
    }

}

package com.snappcloud.widget.weather;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;
import android.util.Xml;

public class XMLWeatherBugFeed extends XMLWeatherFeedAbstract 
{
	
	private final String LOG_TAG = "XMLWeatherFeedAbstract";
	private final char DEGREE = '\u00B0';
	
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
	        		if(tagName.equalsIgnoreCase("city-state")) 
	        		{
	        			String location = parser.nextText();
	        			Log.d(LOG_TAG, "Setting location: " + location);
	        			response.setLocation(location);
	        		}
	        		else if(tagName.equalsIgnoreCase("current-condition")) 
	        		{
	        			String url = getAttribute(parser, "icon");
	        			Log.d(LOG_TAG, "Setting curCond: " + url);
	        			
	        			String curCond = parser.nextText();
	        			Log.d(LOG_TAG, "Setting curCond: " + curCond);
	        			
	        			response.setCurrentConditionsImgUrl(url);
	        			response.setCurrentConditions(curCond);
	        		}
	        		else if(tagName.equalsIgnoreCase("temp")) 
	        		{
	        			String units = getAttribute(parser, "units");
	        			String temp = parser.nextText();
	        			try
	        			{
	        				// only want integer portion
	        				int i = temp.indexOf(".");
	        				if ( i > 0 )
	        				{
	        					temp = temp.substring(0, i);
	        				}
	        			}
	        			catch (Exception e) 
	        			{
	        				Log.d(LOG_TAG, "Setting temp: " + temp);
	        			}
	        			temp += DEGREE + "F";
	        			Log.d(LOG_TAG, "Setting temp: " + temp);
	        			response.setTemperature(temp);
	        		}
	        		else if(tagName.equalsIgnoreCase("temp-high")) 
	        		{
	        			String units = getAttribute(parser, "units");
	        			String high = parser.nextText();
	        			high += DEGREE + "F";
	        			Log.d(LOG_TAG, "Setting high: " + high);
	        			response.setHighTemp(high);
	        		}
	        		else if(tagName.equalsIgnoreCase("temp-low")) 
	        		{
	        			String units = getAttribute(parser, "units");
	        			String low = parser.nextText();
	        			low += DEGREE + "F";
	        			Log.d(LOG_TAG, "Setting low: " + low);
	        			response.setLowTemp(low);
	        		}
	        		break;
	        	case XmlPullParser.END_TAG: break;
	        	default: break;
        	}
	        eventType = parser.next();
        }
        
        return response;
    }

}

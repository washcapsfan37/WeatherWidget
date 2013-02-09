package com.snappcloud.widget.weather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public abstract class XMLWeatherFeedAbstract
{
	private static final String LOG_TAG = XMLWeatherFeedAbstract.class.getSimpleName();
	
	public abstract WeatherResponse parse(String xml) throws XmlPullParserException, IOException;
	
	public WeatherResponse loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException 
	{
		WeatherResponse response = null;
        String responseText = downloadUrl(urlString);        
        response = parse(responseText);
		return response;
	}
	
	// Given a string representation of a URL, sets up a connection and gets response
	private String downloadUrl(String urlString) throws IOException 
	{
		Log.d(LOG_TAG, "Retrieving data from: " + urlString);
		InputStream in = null;
		int response = -1;
		URL url = new URL(urlString);
		int BUFFER_SIZE = 2000;
		URLConnection conn = url.openConnection();
		String str = "";
		try
		{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setReadTimeout( 30000 );
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();
			Log.d(LOG_TAG, "HTTP response status: " + response);
			if ( response == HttpURLConnection.HTTP_OK )
			{
				in = httpConn.getInputStream();
			}
			if ( response == HttpURLConnection.HTTP_MOVED_TEMP || 
					response == HttpURLConnection.HTTP_MOVED_PERM || 
					response == 307 )
			{
				String location = conn.getHeaderField("location");
				Log.i(LOG_TAG, "Redirect encounter:: " + location);
				url = new URL(location);
				conn = url.openConnection();
				httpConn = (HttpURLConnection) conn;
				httpConn.connect();
				in = httpConn.getInputStream();
			}
			if ( in != null )
			{
				InputStreamReader isr = new InputStreamReader(in);
				int charRead;
				char[] inputBuffer = new char[BUFFER_SIZE];
				while ( (charRead = isr.read(inputBuffer)) > 0 )
				{
					String readString = String.copyValueOf(inputBuffer, 0, charRead);
					str += readString;
					inputBuffer = new char[BUFFER_SIZE];
				}
				in.close();
			}
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Exception in downloadUrl: " + e);
		}
		Log.d(LOG_TAG, "XML:: " + str);
		return str;

	}
	
}
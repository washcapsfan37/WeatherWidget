package com.snappcloud.widget.weather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HTTPUtil 
{
	
	public static boolean isOnline(Context context)
	{
		if ( context != null )
		{
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			if ( networkInfo != null )
			{
				String status = networkInfo.getState().toString();
				if ( networkInfo != null && networkInfo.isConnected() )
				{
					return true;
				}
			}
		}
		return false;
	}

}

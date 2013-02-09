package com.snappcloud.widget.weather.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class VerdanaFontTextView extends TextView {
	
	private final static String LOG_TAG = (VerdanaFontTextView.class.getSimpleName()); 

	public VerdanaFontTextView(Context context) {
		super(context);
		Log.i(LOG_TAG, "WTF?");
		try
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"fonts/verdana.ttf");
			this.setTypeface(face);
		}
		catch ( Exception e )
		{
			Log.e(LOG_TAG, "Error loading custom font: " + e);
		}
	}

	public VerdanaFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(LOG_TAG, "WTF?");
		try
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"fonts/verdana.ttf");
			this.setTypeface(face);
		}
		catch ( Exception e )
		{
			Log.e(LOG_TAG, "Error loading custom font: " + e);
		}
	}

	public VerdanaFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(LOG_TAG, "WTF?");
		try
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"fonts/verdana.ttf");
			this.setTypeface(face);
		}
		catch ( Exception e )
		{
			Log.e(LOG_TAG, "Error loading custom font: " + e);
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i(LOG_TAG, "WTF?");
	}

}

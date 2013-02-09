package com.snappcloud.widget.weather.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class FuturaFontTextView extends TextView {
	
	private final static String LOG_TAG = (VerdanaFontTextView.class.getSimpleName()); 

	public FuturaFontTextView(Context context) {
		super(context);
		try
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"fonts/medium.ttf");
			this.setTypeface(face);
		}
		catch ( Exception e )
		{
			Log.e(LOG_TAG, "Error loading custom font: " + e);
		}
	}

	public FuturaFontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"fonts/medium.ttf");
			this.setTypeface(face);
		}
		catch ( Exception e )
		{
			Log.e(LOG_TAG, "Error loading custom font: " + e);
		}
	}

	public FuturaFontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		try
		{
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"fonts/medium.ttf");
			this.setTypeface(face);
		}
		catch ( Exception e )
		{
			Log.e(LOG_TAG, "Error loading custom font: " + e);
		}
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

}

/**
 * 
 */
package com.wzm.balloonz;

import android.content.Context;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.view.*;

/**
 * @author wangzhimin
 *
 */
public class BallGameView extends View {
	private Paint textPaint = new Paint();
	private Paint picPaint = new Paint();
	private int width = 480;
	private int height = 800;
	
	public BallGameView(Context context)
	{
		super(context);
		
		DisplayMetrics dm = new DisplayMetrics();
		
		BalloonzActivity ba = (BalloonzActivity)context;
		ba.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}
	
	public void onDraw(Canvas canvas)
	{
		textPaint.setColor(Color.BLUE);
		textPaint.setTextSize(30);
		textPaint.setTypeface(Typeface.MONOSPACE);

		String strText = "¿ªÊ¼ÓÎÏ·";
		float pixelLength = textPaint.measureText(strText);
		float showX = (width - pixelLength) / 2;
		canvas.drawText(strText, showX, 400, textPaint);
	
		picPaint.setColor(Color.GREEN);
		float radius = 100;
		canvas.drawCircle(200, 200, radius, picPaint);
	}

}

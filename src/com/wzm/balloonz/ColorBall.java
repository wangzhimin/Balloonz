package com.wzm.balloonz;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Paint;


public class ColorBall
{	
	private int ballType; //即数组下标,对应一个球的bitmap
	private Bitmap ballBitmap;
	
	public ColorBall(int index, Bitmap bitmap)
	{
		ballType   = index;
		ballBitmap = bitmap;
	}
	
	public boolean equals(ColorBall another)
	{
		return ballType == another.ballType;
	}
	
	public void onDraw(Canvas canvas, int x, int y, Paint ballPaint)
	{
		canvas.drawBitmap(ballBitmap, x, y, ballPaint);
	}
}


package com.wzm.balloonz;

import android.graphics.*;


public class BitmapMenu
{
	private Bitmap _bitmap = null;
	private Rect   _rect = null;
	
	public BitmapMenu(Bitmap bitmap, Rect rect)
	{
		_bitmap = bitmap;
		_rect   = rect;
	}
	
	public boolean contains(int x, int y)
	{
		return _rect.contains(x, y);
	}
	
	public void onDraw(Canvas canvas, Paint menuPaint)
	{
		canvas.drawBitmap(_bitmap, _rect.left, _rect.top, menuPaint);
	}
}

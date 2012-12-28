package com.wzm.balloonz;

import android.graphics.*;


public class BitmapMenu
{
	public Bitmap _bitmap = null;
	public Rect   _rect = null;
	
	public BitmapMenu(Bitmap bitmap, Rect rect)
	{
		_bitmap = bitmap;
		_rect   = rect;
	}
	
	public Bitmap bitmap()
	{
		return _bitmap;
	}
	
	public boolean contains(int x, int y)
	{
		return _rect.contains(x, y);
	}
	
	public int left()
	{
		return _rect.left;
	}
	
	public int top()
	{
		return _rect.top;
	}
}

package com.wzm.balloonz;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;


public class BallGameView extends View
{
	BalloonzActivity balloonzActivity;
	
	private BallPool ballPool = null;
	

	public BallGameView(Context context)
	{
		super(context);
		
		balloonzActivity = (BalloonzActivity) context;

		ballPool = new BallPool(this);
		
		//图片不缩放
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;

		setBackgroundDrawable(getResources().getDrawable(R.drawable.game_back));
		
		setFocusable(true);
	}

	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		ballPool.onDraw(canvas);		
	}
	
	//触屏，消球
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			int touchX = (int) event.getX();
			int touchY = (int) event.getY();

            //通过点击坐标，定位球的index,消球
			ballPool.processTouchEvent(touchX, touchY);
			
			invalidate();
		}

		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		System.out.println("wzm onKeyDown, keyCode = " + keyCode);
		
	    if (keyCode == KeyEvent.KEYCODE_BACK ) //&& event.getAction() == KeyEvent.ACTION_DOWN)
	    {
	    	System.out.println("wzm onKeyDown, KEYCODE_BACK.");
	    	
	    	balloonzActivity.processTouchMsg(TouchMsg.Msg_backtowelcome);   
	    }
	    return true;//return super.onKeyDown(keyCode, event);
	}
	
}

package com.wzm.balloonz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;

public class RankListView extends View
{
	private BalloonzActivity balloonzActivity;
	
	public RankListView(Context context)
	{
		super(context);
		
		balloonzActivity = (BalloonzActivity)context;
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		
		setBackgroundColor(Color.WHITE);
	}
	
	public void onDraw(Canvas canvas)
	{
		
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK &&
	    	event.getAction() == KeyEvent.ACTION_UP)
	    {
	    	balloonzActivity.processGameMsg(GameMsg.Msg_backtowelcome);   
	    }
	    return true; //如果要自己处理,就返回true
	}
}

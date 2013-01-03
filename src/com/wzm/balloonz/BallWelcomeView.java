package com.wzm.balloonz;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author wangzhimin
 * 
 */
public class BallWelcomeView extends View
{
	private BalloonzActivity balloonzActivity;
	
	BitmapMenuGroup menuGroup = null;
	
	private Paint paintPicture = new Paint();

	private int showWidth = 480;
	private int showHeight = 800;

	private Handler handlerWelcome = new Handler(new BallHandlerCallback());
	
	public BallWelcomeView(Context context)
	{
		super(context);

		balloonzActivity = (BalloonzActivity) context;

		// 获取当前view尺寸
		showWidth = balloonzActivity.getWidth();
		showHeight = balloonzActivity.getHeight();

		setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_back));
		
		// 加载游戏资源
		InitGameResources();
		
		setFocusableInTouchMode(true);
	}

	private void InitGameResources()
	{
		menuGroup = new BitmapMenuGroup(this, showWidth);
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		menuGroup.onDraw(canvas, paintPicture);
	}

	// 触屏处理, 根据点击区域,确定选择的菜单
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			int tmpX = (int) event.getX();
			int tmpY = (int) event.getY();

			menuGroup.processTouchEvent(handlerWelcome, tmpX, tmpY);
		}

		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK &&
	    	event.getAction() == KeyEvent.ACTION_UP)
	    {
	    	   
	    }
	    return false;
	}
	
	private class BallHandlerCallback implements Callback
	{
		//处理当前界面的触屏事件
		public boolean handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1: //开始游戏
				balloonzActivity.processGameMsg(GameMsg.Msg_startgame);
				break;

			case 2: //声音开关
				balloonzActivity.processGameMsg(GameMsg.Msg_sound);
				invalidate();
				balloonzActivity.playBackMusic();
				break;
				
			case 5: //退出游戏
				balloonzActivity.processGameMsg(GameMsg.Msg_quitgame);
				break;
			}

			return true;
		}
	}

	public boolean getSoundSwitch()
	{
		return balloonzActivity.getSound();
	}
}

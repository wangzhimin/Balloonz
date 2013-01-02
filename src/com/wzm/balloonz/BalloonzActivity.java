package com.wzm.balloonz;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;


public class BalloonzActivity extends Activity
{

	private BallWelcomeView ballWelcomeView;
	private BallGameView ballGameView = null;
	
	private boolean soundSwitch = false;
	
	private int width = 480;
	private int height = 800;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		/*
		//全屏，但会先显示一下状态栏和标题栏，然后再全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
        */
		//获取整个屏幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		ballWelcomeView = new BallWelcomeView(this);
		
		setContentView(ballWelcomeView);
	}
	
//	public void onBackPressed()
//	{
//	
//	}
	public void processTouchMsg(TouchMsg msg)
	{
		switch(msg)
		{
		case Msg_startgame:
			if (ballGameView == null)
			{
				ballGameView = new BallGameView(this);
			}
			setContentView(ballGameView);
			break;
			
		case Msg_sound:
			soundSwitch = !soundSwitch;
			break;
			
		case Msg_quitgame:
			System.exit(0);
			break;
			
		case Msg_backtowelcome:
			setContentView(ballWelcomeView);
			break;
		}
	}
	
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public boolean getSound()
	{
		return soundSwitch;
	}
}


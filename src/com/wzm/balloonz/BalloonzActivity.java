package com.wzm.balloonz;


import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;


public class BalloonzActivity extends Activity
{
	private BallWelcomeView ballWelcomeView;   //欢迎界面
	private BallGameView ballGameView = null; //游戏界面
	
	private int gameDifficultyLevel = 1;
	
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

		initGame();
	}
	private void initGame()
	{
		// 获取整个屏幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		ballWelcomeView = new BallWelcomeView(this);
		setContentView(ballWelcomeView);
		
		GameSoundSystem.getInstance().initialize(this);
	}
	
	/* 事件响应函数 */
	public void processGameMsg(GameMsg msg)
	{
		switch(msg)
		{
		case Msg_startgame:
			ballGameView = new BallGameView(this);
			setContentView(ballGameView);
			break;
			
		case Msg_level:
			gameDifficultyLevel++;
			if (gameDifficultyLevel > 3)
			{
				gameDifficultyLevel = 1;
			}
			break;
			
		case Msg_quitgame:
			finish();
			break;
			
		case Msg_backtowelcome:
			setContentView(ballWelcomeView);
			break;
		}
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK &&
	    	event.getAction() == KeyEvent.ACTION_UP)
	    {
	    	finish();   
	    }
	    return true;
	}
	
	public void onDestroy()
	{
		GameSoundSystem.getInstance().onDestroy();
		
		super.onDestroy();
	}
	/* 对外接口函数. */
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public int getLevel()
	{
		return gameDifficultyLevel;
	}
	/* 私有函数 */
	
}


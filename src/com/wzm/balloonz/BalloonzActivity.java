package com.wzm.balloonz;


import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;


public class BalloonzActivity extends Activity
{
	private BallWelcomeView ballWelcomeView = null;   //��ӭ����
	private BallGameView ballGameView = null; //��Ϸ����
	
	private int gameDifficultyLevel = GameLevel.Level_Low; //0,1,2
	
	private int width = 480;
	private int height = 800;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		/*
		//ȫ������������ʾһ��״̬���ͱ�������Ȼ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
        */

		initGame();
	}
	private void initGame()
	{
		// ��ȡ������Ļ�ߴ�
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		ballWelcomeView = new BallWelcomeView(this);
		setContentView(ballWelcomeView);
		
		GameSoundSystem.getInstance().initialize(this);
	}
	
	/* �¼���Ӧ���� */
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
			if (gameDifficultyLevel > GameLevel.Level_High)
			{
				gameDifficultyLevel = GameLevel.Level_Low;
			}
			break;
			
		case Msg_rank:
			RankListView rankView = new RankListView(this);
			setContentView(rankView);
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
	
	@Override
	public void onPause ()
	{
		super.onPause();
		
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		WzmLog.log("onResume().");
	}
	
	@Override
	public void onDestroy ()
	{
		GameSoundSystem.getInstance().onDestroy();
	
		ballWelcomeView = null;
		ballGameView = null;
		
		super.onDestroy();
	}
	
	/* ����ӿں���. */
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
	/* ˽�к��� */
	
}


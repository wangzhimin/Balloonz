package com.wzm.balloonz;

import android.graphics.*;
import android.os.Handler;

public class BitmapMenuGroup
{
	private BallWelcomeView _view;
	
	private BitmapMenu menuStartGame;
	private BitmapMenu menuSoundOpen;
	private BitmapMenu menuSoundClose;
	private BitmapMenu menuLevelLow;
	private BitmapMenu menuLevelMid;
	private BitmapMenu menuLevelHigh;
	private BitmapMenu menuRankList;
	private BitmapMenu menuQuitGame;
	
	BitmapFactory.Options bfoOptions = null;
	int centerX = 0;
	
	BitmapMenuGroup(BallWelcomeView view, int viewWidth)
	{
		_view = view;
		bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false; //ͼƬ������
		
		centerX = viewWidth / 2;
		
		InitMenu();
	}
	private void InitMenu()
	{
		menuStartGame  = getMenu(R.drawable.start_game, 200);     //��ʼ��Ϸ
		menuSoundOpen  = getMenu(R.drawable.sound_open, 300);     //��������
		menuSoundClose = getMenu(R.drawable.sound_close, 300);    //
		menuLevelLow   = getMenu(R.drawable.level_l, 400);        //��Ϸ�Ѷ�
		menuLevelMid   = getMenu(R.drawable.level_m, 400);
		menuLevelHigh  = getMenu(R.drawable.level_h, 400);
		menuRankList   = getMenu(R.drawable.rank_list, 500);
		menuQuitGame   = getMenu(R.drawable.quit_game, 600);      //�˳���Ϸ
	}
	
	public void onDraw(Canvas canvas, Paint paintPicture)
	{
		menuStartGame.onDraw(canvas, paintPicture);
		
		if (GameSoundSystem.getInstance().getSoundSwitch())
		{
			menuSoundOpen.onDraw(canvas, paintPicture);
		}
		else
		{
			menuSoundClose.onDraw(canvas, paintPicture);
		}
		
		switch(_view.getLevel())
		{
		case GameLevel.Level_Low:
			menuLevelLow.onDraw(canvas, paintPicture);
			break;
		case GameLevel.Level_Mid:
			menuLevelMid.onDraw(canvas, paintPicture);
			break;
		case GameLevel.Level_High:
			menuLevelHigh.onDraw(canvas, paintPicture);
			break;
		}
		menuRankList.onDraw(canvas, paintPicture);
		menuQuitGame.onDraw(canvas, paintPicture);
	}
	
	public void processTouchEvent(Handler handlerWelcome, int x, int y)
	{
		if (menuStartGame.contains(x, y))
		{
			handlerWelcome.sendEmptyMessage(1);
		}
		else if (menuSoundOpen.contains(x, y))
		{
			handlerWelcome.sendEmptyMessage(2);
		}
		else if (menuLevelLow.contains(x, y))
		{
			handlerWelcome.sendEmptyMessage(3);
		}
		else if (menuRankList.contains(x, y))
		{
			handlerWelcome.sendEmptyMessage(4);
		}
		else if (menuQuitGame.contains(x, y))
		{
			handlerWelcome.sendEmptyMessage(5);
		}
	}
	
	
	//��������menu
	private BitmapMenu getMenu(int bitmapId, int y)
	{
		Bitmap bitmapMenu = BitmapFactory.decodeResource(_view.getResources(), bitmapId, bfoOptions);
		Rect menu_rect = new Rect(centerX - bitmapMenu.getWidth() / 2, y, centerX + bitmapMenu.getWidth() / 2 - 1, y + bitmapMenu.getHeight() - 1);
		
		return (new BitmapMenu(bitmapMenu, menu_rect));
	}
}

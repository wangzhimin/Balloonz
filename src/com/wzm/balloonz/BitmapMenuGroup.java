package com.wzm.balloonz;

import android.graphics.*;
import android.os.Handler;

public class BitmapMenuGroup
{
	private BallWelcomeView _view;
	
	private BitmapMenu menuStartGame;
	private BitmapMenu menuSoundOpen;
	private BitmapMenu menuSoundClose;
	private BitmapMenu menuQuitGame;
	
	BitmapFactory.Options bfoOptions = null;
	int centerX = 0;
	
	BitmapMenuGroup(BallWelcomeView view, int viewWidth)
	{
		_view = view;
		bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false; //图片不缩放
		
		centerX = viewWidth / 2;
		
		InitMenu();
	}
	
	public void onDraw(Canvas canvas, Paint paintPicture)
	{
		menuStartGame.onDraw(canvas, paintPicture);
		
		if (_view.getSoundSwitch())
		{
			menuSoundOpen.onDraw(canvas, paintPicture);
		}
		else
		{
			menuSoundClose.onDraw(canvas, paintPicture);
		}
		
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
		else if (menuQuitGame.contains(x, y))
		{
			handlerWelcome.sendEmptyMessage(5);
		}
	}
	
	private void InitMenu()
	{
		menuStartGame  = getMenu(R.drawable.start_game, 200);
		menuSoundOpen  = getMenu(R.drawable.sound_open, 300);
		menuSoundClose = getMenu(R.drawable.sound_close, 300);
		menuQuitGame   = getMenu(R.drawable.quit_game, 500);
	}
	//创建单个menu
	private BitmapMenu getMenu(int bitmapId, int y)
	{
		Bitmap bitmapMenu = BitmapFactory.decodeResource(_view.getResources(), bitmapId, bfoOptions);
		Rect menu_rect = new Rect(centerX - bitmapMenu.getWidth() / 2, y, centerX + bitmapMenu.getWidth() / 2 - 1, y + bitmapMenu.getHeight() - 1);
		
		return (new BitmapMenu(bitmapMenu, menu_rect));	 
	}
}


package com.wzm.balloonz;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.*;


/**
 * @author wangzhimin
 *
 */
public class BallWelcomeView extends View
{
	private BalloonzActivity balloonzActivity;
	
	private Bitmap bitmapBackGround; //背景图片
	
	private BitmapMenu menuStartGame;
	private BitmapMenu menuQuitGame;
	
	private Paint paintPicture = new Paint();
	private Paint paintText = new Paint();
	
	private int showWidth = 480;
	private int showHeight = 800;
	
	private int touchX = 0;
	private int touchY = 0;
	
	private Handler handlerWelcome = new Handler(new BallHandlerCallback());
	
	public BallWelcomeView(Context context)
	{
		super(context);
		
		balloonzActivity = (BalloonzActivity)context;
		
		//获取当前view尺寸
		showWidth = balloonzActivity.getWidth();
		showHeight = balloonzActivity.getHeight();
				
		//加载图片资源
		InitGameResources();
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		this.setBackgroundColor(Color.WHITE);

		drawBackGroundBitmap(canvas);
		drawWelcomeMenu(canvas);		
		
		paintText.setTextSize(22);
		paintText.setColor(Color.RED);
		paintText.setTextAlign(Align.LEFT);

		canvas.drawText("x=" + touchX, 100, 50, paintText);
		canvas.drawText("y=" + touchY, 100, 100, paintText);

		/*
		myPaint.setTypeface(Typeface.MONOSPACE);	

		String strExitGame = "退出游戏";
		textMenuPaint.setTextSize(30);
		textMenuPaint.setColor(Color.RED);
		textMenuPaint.setTextAlign(Align.CENTER);
		
		canvas.drawText(strExitGame, showWidth/2, showHeight-20, textMenuPaint);
		*/
	}
	
	//触屏处理, 根据点击区域,确定选择的菜单
	public boolean onTouchEvent(MotionEvent event)
	{
		int tmpX = (int) event.getX();
		int tmpY = (int) event.getY();

		if (menuStartGame.contains(tmpX, tmpY))
		{
			handlerWelcome.sendEmptyMessage(1);
		}
		else if (menuQuitGame.contains(tmpX, tmpY))
		{
			handlerWelcome.sendEmptyMessage(2);
		}
		
		return super.onTouchEvent(event);
	}
	
	private class BallHandlerCallback implements Callback
	{
		public boolean handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 1:
				//切换画面
				balloonzActivity.transVew(new GameView(balloonzActivity));
				break;
				
			case 2:
				System.exit(0);
				break;
			}

			return true;
		}
	}

	private void InitGameResources()
	{
		//图片不缩放
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;

		bitmapBackGround = BitmapFactory.decodeResource(getResources(), R.drawable.welcome_back, bfoOptions);

		int centerX = showWidth / 2;
		
		Bitmap bitmapStartGame = BitmapFactory.decodeResource(getResources(), R.drawable.start_game, bfoOptions);
		Rect start_rect = new Rect(centerX-bitmapStartGame.getWidth()/2, 200, centerX+bitmapStartGame.getWidth()/2-1, 200+bitmapStartGame.getHeight()-1);
		menuStartGame = new BitmapMenu(bitmapStartGame, start_rect);
		
		Bitmap bitmapQuitGame = BitmapFactory.decodeResource(getResources(), R.drawable.quit_game, bfoOptions);
		Rect quit_rect = new Rect(centerX-bitmapQuitGame.getWidth()/2, 500, centerX+bitmapQuitGame.getWidth()/2-1, 500+bitmapQuitGame.getHeight()-1);
		menuQuitGame = new BitmapMenu(bitmapQuitGame, quit_rect);
	}
	//显示背景图片
	private void drawBackGroundBitmap(Canvas canvas)
	{		
		canvas.drawBitmap(bitmapBackGround, 0, 0, paintPicture);
	}

	//显示开始界面菜单
	private void drawWelcomeMenu(Canvas canvas)
	{
		canvas.drawBitmap(menuStartGame.bitmap(), menuStartGame.left(), menuStartGame.top(), paintPicture);
		canvas.drawBitmap(menuQuitGame.bitmap(), menuQuitGame.left(), menuQuitGame.top(), paintPicture);
	}
}

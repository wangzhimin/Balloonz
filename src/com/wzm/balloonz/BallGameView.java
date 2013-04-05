
package com.wzm.balloonz;


import android.content.Context;
import android.graphics.*;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class BallGameView extends View
{
	private BalloonzActivity balloonzActivity;
	
	private BitmapMenu menuRestart;
	private Paint picPaint = new Paint();
	
	private BallPool ballPool = null;
	
	private Button buttonRestart;
	
	private Paint textPaint = new Paint();
	
	private int lastKillNum = 0; //���һ�����ĸ���
	private int lastScore = 0;   //���һ������÷���
	private int score = 0; //�ܷ���
	
	public BallGameView(Context context)
	{
		super(context);		
		balloonzActivity = (BalloonzActivity) context;
		
		setBackgroundDrawable(getResources().getDrawable(R.drawable.game_back));
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false; //ͼƬ������
		
		Bitmap bitmapRestart = BitmapFactory.decodeResource(getResources(), R.drawable.restart, bfoOptions);
		Rect rect = new Rect(10, 10, 10+bitmapRestart.getWidth(), 10+bitmapRestart.getHeight());
		menuRestart = new BitmapMenu(bitmapRestart, rect);
		
		initGame();
	}
	private void initGame()
	{
		ballPool = new BallPool(this, balloonzActivity.getLevel());
		
		textPaint.setTextSize(28);
		textPaint.setColor(Color.YELLOW);
		score = 0;
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		menuRestart.onDraw(canvas, picPaint);
		
		ballPool.onDraw(canvas);
		
		canvas.drawText("���� " + lastKillNum + " ��, " + "�÷�: " + lastScore + ", �ܷ���:" + score, 10, 750, textPaint);
		
		if (ballPool.game_over())
		{
			canvas.drawText("��Ϸ����", 200, 400, textPaint);
		}
	}
	
	//����������
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			int touchX = (int) event.getX();
			int touchY = (int) event.getY();

			if (menuRestart.contains(touchX, touchY))
			{
				initGame();
				invalidate();
			}
			else
			{
				GameThread gameThread = new GameThread(touchX, touchY);
	            gameThread.start();	
			}
		}

		return true;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK &&
	    	event.getAction() == KeyEvent.ACTION_UP)
	    {
	    	balloonzActivity.processGameMsg(GameMsg.Msg_backtowelcome);   
	    }
	    return true; //���Ҫ�Լ�����,�ͷ���true
	}
	
	private class GameThread extends Thread
	{
		private int touchX;
		private int touchY;
		
		public GameThread(int x, int y)
		{
			touchX = x;
			touchY = y;
		}
		public void run()
		{
			 //ͨ��������꣬��λ���index,����
			ballPool.processTouchEvent(touchX, touchY);
			
			int killNum = ballPool.getKillNum();
			
			if (killNum >= 2)
			{
				lastKillNum = killNum;
				lastScore = fibonacci(killNum);
				score += lastScore;
				
				postInvalidate();
				
				//д���ݿ�
			}
		}
	}
	
	
	/* ˽�к���. */
	//��������
	private int fibonacci(int n)
	{		
		double Root_of_Five = Math.sqrt(5);
		
		double result = (Math.pow((1 + Root_of_Five)/2, n) - Math.pow((1-Root_of_Five)/2, n)) / Root_of_Five; 
		
		return (int)result;
	}	
}

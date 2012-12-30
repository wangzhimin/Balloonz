package com.wzm.balloonz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.*;

public class BallPool
{
	private final int ROW_NUM    = 8;
	private final int COLUMN_NUM = 8;
	
	private int ballWidth  = 50;
	private int ballHeight = 50;
	
	private int left = 0;
	private int top  = 0;
	
	ArrayList<Bitmap> bitmapCollection = new ArrayList<Bitmap>(); //存放8种球的bitmap，下标对应球类型
	
	private ColorBall[][] ballPool = new ColorBall[ROW_NUM][COLUMN_NUM];
	
	private Paint ballPaint = new Paint();
	
	private Random rand = new Random();
	
	public BallPool(BallGameView view)
	{
		left = 40;
		top  = 200;
		
		LoadResources(view);
		InitBallPool();		
	}
	
	private void LoadResources(BallGameView view)
	{
		// 图片不缩放
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;
		
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.foot_ball, bfoOptions));		
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.basket_ball, bfoOptions));
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.volley_ball, bfoOptions));
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.bowling_ball, bfoOptions));
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.tennis_ball, bfoOptions));
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.billiards_ball, bfoOptions));
	}
	
	private void InitBallPool()
	{
		int max_num = bitmapCollection.size();
		
		for (int row = 0; row < ROW_NUM; ++row)
		{
			for (int column = 0; column < COLUMN_NUM; ++column)
			{
				int ballIndex = rand.nextInt(max_num);
				
				ballPool[row][column] = new ColorBall(ballIndex, bitmapCollection.get(ballIndex));
			}
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		for (int row = 0; row < ROW_NUM; ++row)
		{
			for (int column = 0; column < COLUMN_NUM; ++column)
			{
				int showX = left + column*ballWidth;
				int showY = top + row*ballHeight;
				
				ballPool[row][column].onDraw(canvas, showX, showY, ballPaint);
			}
		}
	}
}

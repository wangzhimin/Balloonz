package com.wzm.balloonz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.*;

public class BallPool
{
	private final int ROW_NUM    = 12;
	private final int COLUMN_NUM = 8;
	
	private int ballWidth  = 50; //小球的尺寸
	private int ballHeight = 50;
	
	private int left = 0;
	private int top  = 0;
	
	private Rect poolRect = null;
	
	ArrayList<Bitmap> bitmapCollection = new ArrayList<Bitmap>(); //存放8种球的bitmap，下标对应球类型
	
	private ColorBall[][] ballPool = new ColorBall[ROW_NUM][COLUMN_NUM];
	
	private Paint ballPaint = new Paint();
	
	private Random rand = new Random();

	private int num_of_same = 0;
	
	public BallPool(BallGameView view)
	{
		left = 40;
		top  = 100;
		
		LoadResources(view);
		InitBallPool();
		
		poolRect = new Rect(left, top, left + ballWidth * COLUMN_NUM, top + ballHeight * ROW_NUM);
	}
	
	private void LoadResources(BallGameView view)
	{
		// 图片不缩放
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;
		
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.foot_ball, bfoOptions));	  //足球	
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.basket_ball, bfoOptions));  //篮球
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.volley_ball, bfoOptions));  //排球
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.bowling_ball, bfoOptions)); //保龄球
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.tennis_ball, bfoOptions));  //网球
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.rugby_ball, bfoOptions));   //橄榄球
		bitmapCollection.add(BitmapFactory.decodeResource(view.getResources(), R.drawable.billiards_ball, bfoOptions)); //台球,黑八
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
				int showX = left + column * ballWidth;
				int showY = top + row * ballHeight;
				
				if (ballPool[row][column] != null)
				{
					ballPool[row][column].onDraw(canvas, showX, showY, ballPaint);
				}
			}
		}
	}
	
	public void processTouchEvent(int x, int y)
	{
		if (poolRect.contains(x, y))
		{
			int rowIndex    = (y - top) / ballHeight; //行下标,用纵坐标算
			int columnIndex = (x - left) / ballWidth;
			
			num_of_same = 0;
			killBall(rowIndex, columnIndex);
			
			if (num_of_same > 0)
			{
				up2down();
			}
		}
	}
	
	private boolean killBall(int rowIndex, int columnIndex)
	{
		ColorBall focus = ballPool[rowIndex][columnIndex];
		if (focus == null)
		{
			return true;
		}
		//先看一下上下左右有没有相同的球
		if (columnIndex > 0) //左
		{	
			if (focus.equals(ballPool[rowIndex][columnIndex-1]))
			{
				num_of_same++;
			}
		}
		if (columnIndex < COLUMN_NUM-1) //右
		{
			if (focus.equals(ballPool[rowIndex][columnIndex+1]))
			{
				num_of_same++;
			}
		}
		if (rowIndex > 0) //上
		{
			if (focus.equals(ballPool[rowIndex-1][columnIndex]))
			{
				num_of_same++;
			}
		}
		if (rowIndex < ROW_NUM-1) //下
		{
			if (focus.equals(ballPool[rowIndex+1][columnIndex]))
			{
				num_of_same++;
			}
		}
		
		//根据相同球的数量,决定是否要消球
		if (num_of_same > 0)
		{
			ballPool[rowIndex][columnIndex] = null;
		
			if (columnIndex > 0) //左
			{	
				if (focus.equals(ballPool[rowIndex][columnIndex-1]))
				{
					killBall(rowIndex, columnIndex-1); //递归左边
				}
			}
			
			if (columnIndex < COLUMN_NUM - 1) // 右
			{
				if (focus.equals(ballPool[rowIndex][columnIndex + 1]))
				{
					killBall(rowIndex, columnIndex+1);
				}
			}
			if (rowIndex > 0) //上
			{
				if (focus.equals(ballPool[rowIndex-1][columnIndex]))
				{
					killBall(rowIndex-1, columnIndex);
				}
			}
			if (rowIndex < ROW_NUM-1) //下
			{
				if (focus.equals(ballPool[rowIndex+1][columnIndex]))
				{
					killBall(rowIndex+1, columnIndex);
				}
			}
		}		
		
		return true;
	}
	
	private void up2down()
	{
		for (int columnIndex = 0; columnIndex < COLUMN_NUM; ++columnIndex) //一列一列的处理
		{
			//类似于冒泡排序,把空的给挪到上面去
			for (int num = 0; num < ROW_NUM; ++num) //趟数
			{
				for (int rowIndex = ROW_NUM - 1; rowIndex > num; --rowIndex)
				{
					if (ballPool[rowIndex][columnIndex] == null)
					{
						ballPool[rowIndex][columnIndex] = ballPool[rowIndex - 1][columnIndex]; // 往下挪一个
						ballPool[rowIndex - 1][columnIndex] = null;
					}
				}
			}
		}
	}
}

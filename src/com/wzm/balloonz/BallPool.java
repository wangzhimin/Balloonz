package com.wzm.balloonz;

import android.content.res.Resources;
import android.graphics.*;


import java.util.*;

public class BallPool
{
	private BallGameView gameView = null;
	
	private final int ROW_NUM    = 12;
	private final int COLUMN_NUM = 8;
	
	private final int ballWidth  = 50; //小球的尺寸
	private final int ballHeight = 50;

	ArrayList<Bitmap> bitmapCollection = new ArrayList<Bitmap>(); //存放8种球的bitmap，下标对应球类型
	
	private Bitmap fireballBitmap;
	private ColorBall ballFireBall;
	
	private int gameLevel = 1; //游戏难度
	
	private int left = 0;
	private int top  = 0;
	
	private ColorBall[][] balls = new ColorBall[ROW_NUM][COLUMN_NUM];
	private Rect poolRect = null;
	private int num_of_killed = 0;//真正消的数量
	private ColorBall ballFocus = null;
	
	private Paint ballPaint = new Paint();
	private Random rand = new Random();
	
	private boolean game_over = false;
	
	public BallPool(BallGameView view, int level)
	{
		gameView = view;
		gameLevel = level;
		
		left = 40; //(gameView.getWidth() - COLUMN_NUM*ballWidth) / 2;
		top  = 100; //(gameView.getHeight() - ROW_NUM*ballHeight) / 2;
		
		WzmLog.log("w = " + gameView.getWidth());
		WzmLog.log("h = " + gameView.getHeight());
		
		LoadResources();
		InitBallPool();
		
		poolRect = new Rect(left, top, left + ballWidth * COLUMN_NUM, top + ballHeight * ROW_NUM);
	}
	private void LoadResources()
	{
		// 图片不缩放
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;
		
		Resources res = gameView.getResources();
		
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.foot_ball, bfoOptions));	  //足球	
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.basket_ball, bfoOptions));  //篮球
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.volley_ball, bfoOptions));  //排球
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.bowling_ball, bfoOptions)); //保龄球
		
		if (gameLevel >= 2)
		{
			bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.tennis_ball, bfoOptions));  //网球
			bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.rugby_ball, bfoOptions));   //橄榄球
		}
		
		if (gameLevel == 3)
		{
			bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.billiards_ball, bfoOptions)); //台球,黑八
		}
		fireballBitmap = BitmapFactory.decodeResource(res, R.drawable.fire_ball, bfoOptions);
		ballFireBall = new ColorBall(0xff, fireballBitmap);
	}
	private void InitBallPool()
	{
		for (int row = 0; row < ROW_NUM; ++row)
		{
			for (int column = 0; column < COLUMN_NUM; ++column)
			{				
				balls[row][column] = createColorBall();
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
				
				if (balls[row][column] != null)
				{
					balls[row][column].onDraw(canvas, showX, showY, ballPaint);
				}
			}
		}
	}
	
	public void processTouchEvent(int x, int y)
	{
		if (game_over)
		{
			return;
		}
		
		if (poolRect.contains(x, y))
		{
			int rowIndex    = (y - top) / ballHeight; //行下标,用纵坐标算
			int columnIndex = (x - left) / ballWidth;
			
			int numToKill = getNumToKill(rowIndex, columnIndex); //消之前预估的数量,只要第一个球预估数大于1即可
			
			if (numToKill > 1) //根据相同球的数量,决定是否要消球,只有第一个球才需要判断
			{
				num_of_killed = 0;
				ballFocus = balls[rowIndex][columnIndex];
				killBall(rowIndex, columnIndex);
				gameView.postInvalidate();
				
				if (num_of_killed > 1) //消了球,才需要重新调整位置
				{				
					up2down();	
					right2left();
					fillRight();
					
					//判断游戏是否已经结束
					game_over = gameOver();
					if (game_over)
					{
						//数据库
						
					}
				}
			}
		}
	}

	/* 对外接口函数. */
	public int getKillNum()
	{
		int num = num_of_killed;
		num_of_killed = 0;
		
		return num;
	}
	public boolean game_over()
	{
		return game_over;
	}
	
	/* 私有函数 */
	private ColorBall createColorBall()
	{
		int max_num = bitmapCollection.size();
		int ballIndex = rand.nextInt(max_num);
		
		return new ColorBall(ballIndex, bitmapCollection.get(ballIndex));
	}
	private void killBall(int rowIndex, int columnIndex)
	{
		ColorBall me = balls[rowIndex][columnIndex];
		if (me == null ||
			!me.equals(ballFocus)) //判断球类型是否相同
		{
			return;
		}
		
		balls[rowIndex][columnIndex] = null; // 先把自己消掉,这样邻居在比较的时候就不需要额外的标记
		num_of_killed++;

		if (columnIndex > 0) // 左
		{
			if (me.equals(balls[rowIndex][columnIndex - 1]))
			{
				killBall(rowIndex, columnIndex - 1); // 递归左边
			}
		}

		if (columnIndex < COLUMN_NUM - 1) // 右
		{
			if (me.equals(balls[rowIndex][columnIndex + 1]))
			{
				killBall(rowIndex, columnIndex + 1);
			}
		}
		if (rowIndex > 0) // 上
		{
			if (me.equals(balls[rowIndex - 1][columnIndex]))
			{
				killBall(rowIndex - 1, columnIndex);
			}
		}
		if (rowIndex < ROW_NUM - 1) // 下
		{
			if (me.equals(balls[rowIndex + 1][columnIndex]))
			{
				killBall(rowIndex + 1, columnIndex);
			}
		}
	}
	
	private int getNumToKill(int rowIndex, int columnIndex)
	{
		int numToKill = 1;
		ColorBall focus = balls[rowIndex][columnIndex];
		
		if (focus != null)
		{
			// 先看一下上下左右有没有相同的球
			if (columnIndex > 0) // 左
			{
				if (focus.equals(balls[rowIndex][columnIndex - 1]))
				{
					numToKill++;
				}
			}
			if (columnIndex < COLUMN_NUM - 1) // 右
			{
				if (focus.equals(balls[rowIndex][columnIndex + 1]))
				{
					numToKill++;
				}
			}
			if (rowIndex > 0) // 上
			{
				if (focus.equals(balls[rowIndex - 1][columnIndex]))
				{
					numToKill++;
				}
			}
			if (rowIndex < ROW_NUM - 1) // 下
			{
				if (focus.equals(balls[rowIndex + 1][columnIndex]))
				{
					numToKill++;
				}
			}
		}
		
		return numToKill;
	}
	
	private void up2down()
	{
		delay(200);
		for (int columnIndex = 0; columnIndex < COLUMN_NUM; ++columnIndex) //一列一列的处理
		{
			//类似于冒泡排序,把空的给挪到上面去
			for (int num = 0; num < ROW_NUM; ++num) //趟数
			{
				for (int rowIndex = ROW_NUM - 1; rowIndex > num; --rowIndex) //最上面一行无需处理
				{
					if (balls[rowIndex][columnIndex] == null)
					{
						balls[rowIndex][columnIndex] = balls[rowIndex - 1][columnIndex]; // 往下挪一个
						balls[rowIndex - 1][columnIndex] = null;
					}
				}
			}
		}
		refresh();
	}
	
	//某一列的最下面一行没球,代表这一列都没球了,右边的所有列向左平移
	private void right2left()
	{
		delay(200);
		for (int columnIndex = COLUMN_NUM-2; columnIndex >= 0; --columnIndex) //最右边一列无需处理
		{
			if (balls[ROW_NUM-1][columnIndex] == null)
			{
				for (int moveIndex = columnIndex; moveIndex < COLUMN_NUM-1; ++moveIndex) 
				{
					for(int row = 0; row < ROW_NUM; ++row)
					{
						balls[row][moveIndex] = balls[row][moveIndex+1];
						balls[row][moveIndex+1] = null;
					}
				}
			}
		}
		refresh();
	}
	private void fillRight()
	{
		delay(200);
		for (int columnIndex = 0; columnIndex < COLUMN_NUM; ++columnIndex)
		{
			if (balls[ROW_NUM-1][columnIndex] == null)
			{
				for (int row = 0; row < ROW_NUM; ++row)
				{
					balls[row][columnIndex] = createColorBall();
				}
				refresh();
				delay(100);
			}
		}
	}
	private boolean gameOver()
	{
		for (int rowIndex = 0; rowIndex < ROW_NUM; ++rowIndex)
		{
			for (int columnIndex = 0; columnIndex < COLUMN_NUM; ++columnIndex)
			{
				if (balls[rowIndex][columnIndex] != null)
				{
					int num = getNumToKill(rowIndex, columnIndex);
					if (num > 1)
					{
						return false;
					}
				}
			}
		}
		
		refresh();
		
		return true;
	}
	
	private void delay(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void refresh()
	{
		gameView.postInvalidate();
	}
}

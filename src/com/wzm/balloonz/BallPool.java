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
	private int gameLevel = 1; //游戏难度
	
	private int left = 0;
	private int top  = 0;
	
	private ColorBall[][] ballPool = new ColorBall[ROW_NUM][COLUMN_NUM];
	private Rect poolRect = null;
	private int num_of_same = 0;
	
	private Paint ballPaint = new Paint();
	private Random rand = new Random();
	
	public BallPool(BallGameView view, int level)
	{
		gameView = view;
		gameLevel = level;
		
		left = 40;
		top  = 100;
		
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
			
			num_of_same = 1;
			killBall(rowIndex, columnIndex);
			gameView.invalidate();
			
			if (num_of_same > 1)
			{
				//硬阻塞
				long now = System.currentTimeMillis();
				for(;;)
				{
					long next = System.currentTimeMillis();
					if (next - now > 400)
					{
						break;
					}
				}
				up2down();
				right2left();
			}
		}
	}

	/* 对外接口函数. */
	public int getKillNum()
	{
		return num_of_same;
	}
	
	/* 私有函数 */
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
		
		if (num_of_same > 1) //根据相同球的数量,决定是否要消球
		{
			ballPool[rowIndex][columnIndex] = null; //先把自己消掉,这样邻居在比较的时候就不需要额外的标记
		
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
				for (int rowIndex = ROW_NUM - 1; rowIndex > num; --rowIndex) //最上面一行无需处理
				{
					if (ballPool[rowIndex][columnIndex] == null)
					{
						ballPool[rowIndex][columnIndex] = ballPool[rowIndex - 1][columnIndex]; // 往下挪一个
						ballPool[rowIndex - 1][columnIndex] = null;
					}
				}
			}
		}
		
		gameView.invalidate(poolRect);
	}
	
	//某一列的最下面一行没球,代表这一列都没球了,右边的所有列向左平移
	private void right2left()
	{
		for (int columnIndex = COLUMN_NUM-2; columnIndex >= 0; --columnIndex) //最右边一列无需处理
		{
			if (ballPool[ROW_NUM-1][columnIndex] == null)
			{
				for (int moveIndex = columnIndex; moveIndex < COLUMN_NUM-1; ++moveIndex) 
				{
					for(int row = 0; row < ROW_NUM; ++row)
					{
						ballPool[row][moveIndex] = ballPool[row][moveIndex+1];
						ballPool[row][moveIndex+1] = null;
					}
				}
			}
		}
		
		gameView.invalidate(poolRect);
	}
}

package com.wzm.balloonz;

import android.content.res.Resources;
import android.graphics.*;


import java.util.*;

public class BallPool
{
	private BallGameView gameView = null;
	
	private final int ROW_NUM    = 12;
	private final int COLUMN_NUM = 8;
	
	private final int ballWidth  = 50; //С��ĳߴ�
	private final int ballHeight = 50;

	ArrayList<Bitmap> bitmapCollection = new ArrayList<Bitmap>(); //���8�����bitmap���±��Ӧ������
	
	private Bitmap fireballBitmap;
	private ColorBall ballFireBall;
	
	private int gameLevel = 1; //��Ϸ�Ѷ�
	
	private int left = 0;
	private int top  = 0;
	
	private ColorBall[][] balls = new ColorBall[ROW_NUM][COLUMN_NUM];
	private Rect poolRect = null;
	private int num_of_killed = 0;//������������
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
		// ͼƬ������
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;
		
		Resources res = gameView.getResources();
		
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.foot_ball, bfoOptions));	  //����	
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.basket_ball, bfoOptions));  //����
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.volley_ball, bfoOptions));  //����
		bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.bowling_ball, bfoOptions)); //������
		
		if (gameLevel >= 2)
		{
			bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.tennis_ball, bfoOptions));  //����
			bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.rugby_ball, bfoOptions));   //�����
		}
		
		if (gameLevel == 3)
		{
			bitmapCollection.add(BitmapFactory.decodeResource(res, R.drawable.billiards_ball, bfoOptions)); //̨��,�ڰ�
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
			int rowIndex    = (y - top) / ballHeight; //���±�,����������
			int columnIndex = (x - left) / ballWidth;
			
			int numToKill = getNumToKill(rowIndex, columnIndex); //��֮ǰԤ��������,ֻҪ��һ����Ԥ��������1����
			
			if (numToKill > 1) //������ͬ�������,�����Ƿ�Ҫ����,ֻ�е�һ�������Ҫ�ж�
			{
				num_of_killed = 0;
				ballFocus = balls[rowIndex][columnIndex];
				killBall(rowIndex, columnIndex);
				gameView.postInvalidate();
				
				if (num_of_killed > 1) //������,����Ҫ���µ���λ��
				{				
					up2down();	
					right2left();
					fillRight();
					
					//�ж���Ϸ�Ƿ��Ѿ�����
					game_over = gameOver();
					if (game_over)
					{
						//���ݿ�
						
					}
				}
			}
		}
	}

	/* ����ӿں���. */
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
	
	/* ˽�к��� */
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
			!me.equals(ballFocus)) //�ж��������Ƿ���ͬ
		{
			return;
		}
		
		balls[rowIndex][columnIndex] = null; // �Ȱ��Լ�����,�����ھ��ڱȽϵ�ʱ��Ͳ���Ҫ����ı��
		num_of_killed++;

		if (columnIndex > 0) // ��
		{
			if (me.equals(balls[rowIndex][columnIndex - 1]))
			{
				killBall(rowIndex, columnIndex - 1); // �ݹ����
			}
		}

		if (columnIndex < COLUMN_NUM - 1) // ��
		{
			if (me.equals(balls[rowIndex][columnIndex + 1]))
			{
				killBall(rowIndex, columnIndex + 1);
			}
		}
		if (rowIndex > 0) // ��
		{
			if (me.equals(balls[rowIndex - 1][columnIndex]))
			{
				killBall(rowIndex - 1, columnIndex);
			}
		}
		if (rowIndex < ROW_NUM - 1) // ��
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
			// �ȿ�һ������������û����ͬ����
			if (columnIndex > 0) // ��
			{
				if (focus.equals(balls[rowIndex][columnIndex - 1]))
				{
					numToKill++;
				}
			}
			if (columnIndex < COLUMN_NUM - 1) // ��
			{
				if (focus.equals(balls[rowIndex][columnIndex + 1]))
				{
					numToKill++;
				}
			}
			if (rowIndex > 0) // ��
			{
				if (focus.equals(balls[rowIndex - 1][columnIndex]))
				{
					numToKill++;
				}
			}
			if (rowIndex < ROW_NUM - 1) // ��
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
		for (int columnIndex = 0; columnIndex < COLUMN_NUM; ++columnIndex) //һ��һ�еĴ���
		{
			//������ð������,�ѿյĸ�Ų������ȥ
			for (int num = 0; num < ROW_NUM; ++num) //����
			{
				for (int rowIndex = ROW_NUM - 1; rowIndex > num; --rowIndex) //������һ�����账��
				{
					if (balls[rowIndex][columnIndex] == null)
					{
						balls[rowIndex][columnIndex] = balls[rowIndex - 1][columnIndex]; // ����Ųһ��
						balls[rowIndex - 1][columnIndex] = null;
					}
				}
			}
		}
		refresh();
	}
	
	//ĳһ�е�������һ��û��,������һ�ж�û����,�ұߵ�����������ƽ��
	private void right2left()
	{
		delay(200);
		for (int columnIndex = COLUMN_NUM-2; columnIndex >= 0; --columnIndex) //���ұ�һ�����账��
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

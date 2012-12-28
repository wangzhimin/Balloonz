package com.wzm.balloonz;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.graphics.*;


public class GameView extends View
{
	private Bitmap bitmapBackGround;
	private Paint backPaint = new Paint();

	public GameView(Context context) {
		super(context);
		
		//Í¼Æ¬²»Ëõ·Å
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options();
		bfoOptions.inScaled = false;

		bitmapBackGround = BitmapFactory.decodeResource(getResources(), R.drawable.game_back, bfoOptions);
	}

	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		this.setBackgroundColor(Color.WHITE);

		drawBackGroundBitmap(canvas);
	}
	
	private void drawBackGroundBitmap(Canvas canvas)
	{
		canvas.drawBitmap(bitmapBackGround, 0, 0, backPaint);
	}
	
	
}

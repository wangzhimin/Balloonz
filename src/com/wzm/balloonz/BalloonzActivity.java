package com.wzm.balloonz;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.util.DisplayMetrics;
import android.widget.*;
import android.graphics.Color;


public class BalloonzActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		BallGameView ballView = new BallGameView(this);
		setContentView(ballView);
		
		
		//setContentView(R.layout.activity_balloonz); //layout后面是文件名字
		/*
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//String strOpt = "手机分辨率 = " + dm.widthPixels + " X " + dm.heightPixels;
		TextView textTest = (TextView)findViewById(R.id.textViewFirst);
		textTest.setText(strOpt);
		textTest.setTextColor(Color.RED);
		
		Button btnExit = (Button)findViewById(R.id.buttonTest);
		btnExit.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				System.exit(0);
			}
		});
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_balloonz, menu);
		return true;
	}

}

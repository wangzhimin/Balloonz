
package com.wzm.balloonz;

import com.wzm.balloonz.R;
import android.media.MediaPlayer;


public class GameSoundSystem
{
	private static GameSoundSystem _inst = null;

	private boolean soundSwitch = false;
	private MediaPlayer audioPlayer = null;
	
	public static GameSoundSystem getInstance()
	{
		if (_inst == null)
		{
			_inst = new GameSoundSystem();
		}
		return _inst;
	}
	
	public void initialize(BalloonzActivity context)
	{
		audioPlayer = MediaPlayer.create(context, R.raw.back_ground);
		if (audioPlayer == null)
		{
			WzmLog.log("initGame audioPlayer == null.");
		}
	}

	public void onDestroy()
	{
		if (audioPlayer != null)
		{
			if (audioPlayer.isPlaying())
			{
				audioPlayer.stop();
			}
			audioPlayer.release();
			audioPlayer = null;
		}
	}
	
	public void processGameMsg(GameMsg msg)
	{
		if (msg == GameMsg.Msg_sound)
		{
			soundSwitch = !soundSwitch;
		}
	}
	public boolean getSoundSwitch()
	{
		return soundSwitch;
	}
	public void playBackMusic()
	{
		if (soundSwitch)
		{
			try
			{
				if (audioPlayer == null)
				{
					return;
				}

				audioPlayer.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			audioPlayer.pause();
		}
	}
}

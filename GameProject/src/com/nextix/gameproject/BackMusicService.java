package com.nextix.gameproject;




import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackMusicService extends Service{
	MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub	
		player=MediaPlayer.create(this,R.raw.backgroundmus);
		player.setLooping(true);
		float volume = (float) 0.5;
		player.setVolume(volume, volume);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		player.start();
	}

	@Override
	public void onDestroy() {
		player.stop();
		super.onDestroy();
	}

}

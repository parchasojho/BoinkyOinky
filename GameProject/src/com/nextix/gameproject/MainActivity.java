package com.nextix.gameproject;



import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	GameView gv;
	Intent bm;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv=new GameView(this);
        ActionBar ab = getActionBar();
        ab.hide();
		setContentView(gv);
		bm = new Intent(this,BackMusicService.class);
		this.startService(bm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	stopService(bm);
    	super.onDestroy();
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	gv.destroyed();
    	super.onPause();
    }
  
    
}

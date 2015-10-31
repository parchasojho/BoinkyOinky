package com.nextix.gameproject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Welcome extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar a = getActionBar();
		a.hide();
		setContentView(R.layout.fragment_welcome);
		int highscore = 0;
		File file = new File(getFilesDir(),GameView.filename);
		if(file.exists()){
			try {
				ObjectInputStream in = new ObjectInputStream(openFileInput(GameView.filename));
				highscore = (int) in.readInt();
				in.close();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		TextView tv = (TextView) findViewById(R.id.hs);
		tv.setText(""+highscore);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startGame(View v){
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
	}
	
	public void endGame(View v){
		finish();
        System.exit(0);
	}
	
	public void helpGame(View v){
		Intent help = new Intent(this,HelpActivity.class);
		startActivity(help);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	 @Override
	 public void onBackPressed() {
	    super.onBackPressed();
	    this.finish();
	 }
	

}

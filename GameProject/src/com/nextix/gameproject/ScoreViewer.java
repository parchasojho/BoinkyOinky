package com.nextix.gameproject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreViewer extends Activity {
	int score,highscore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar a = getActionBar();
		a.hide();
		setContentView(R.layout.fragment_score_viewer);
		TextView  tv= (TextView) findViewById(R.id.score);
		score = getIntent().getIntExtra("score", 0);
		tv.setText(""+score);
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
		
		}else{
			highscore = 0;
		}
		
		if(score>highscore){
			Toast.makeText(this, "Congratulations! You've got a new High Score", Toast.LENGTH_LONG).show();
			highscore=score;
			changeHighscore();
		}
		TextView hs = (TextView) findViewById(R.id.highscore);
		hs.setText(""+highscore);
		Log.v("game", "score end");

	}
	
	public void changeHighscore(){
		try {
			ObjectOutputStream out = new ObjectOutputStream(openFileOutput(GameView.filename, Context.MODE_PRIVATE));
			out.writeInt(score);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_viewer, menu);
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
	
	public void Restart(View v){
		Intent i = new Intent(this,MainActivity.class);
		startActivity(i);
		finish();
	}

	


}

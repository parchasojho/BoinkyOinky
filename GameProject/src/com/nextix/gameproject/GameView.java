package com.nextix.gameproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameView extends SurfaceView implements Runnable, OnTouchListener{
	public final static String filename = "piggybank";
	Bitmap sky = null, b=null, grass = null, fire=null, pig1 = null, pig2 = null, pig = null,pigjump1,pigjump2,pigjump3,deadpig,forest;
	SurfaceHolder holder=null;
	Activity mycontext;
	Thread hilo=null;
	boolean isrunning=false,isOver=false;
	int fx=0,forestx=0,forestx1=0;
	int flag=10;
	int y, ygrass, yfire, ypig, yforest;
	Paint red,scorePaint, hscorePaint;
	int score=-1,maxht=100,ht=0,minht,px=100,firespeed,highscore;
	boolean toUp,downed,one, flagscore = false,working=true;
	private static SoundPool sounds, sounds1, sounds3;
	int oinkSound, jump, yell;
	Intent bm;
	

	public GameView(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder=this.getHolder();
		mycontext=context;
		firespeed = 2;
		File file = new File(mycontext.getFilesDir(),filename);
		if(file.exists()){
			try {
				ObjectInputStream in = new ObjectInputStream(mycontext.openFileInput(filename));
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
		
		b=BitmapFactory.decodeResource(getResources(),R.drawable.ground);
		grass=BitmapFactory.decodeResource(getResources(),R.drawable.grass);
		fire = BitmapFactory.decodeResource(getResources(),R.drawable.fire);
		pig=pig1 = BitmapFactory.decodeResource(getResources(), R.drawable.pig1);
		pig2 = BitmapFactory.decodeResource(getResources(), R.drawable.pig2);
		pigjump1=BitmapFactory.decodeResource(getResources(), R.drawable.pigjump1);
		pigjump2 =BitmapFactory.decodeResource(getResources(), R.drawable.pigjump2);
		pigjump3 =BitmapFactory.decodeResource(getResources(), R.drawable.pigjump3);
		deadpig =BitmapFactory.decodeResource(getResources(), R.drawable.deadpig);
		forest = BitmapFactory.decodeResource(getResources(), R.drawable.forest); 
		sky = BitmapFactory.decodeResource(getResources(), R.drawable.forestsky); 
		
		scorePaint = new Paint();
		scorePaint.setColor(Color.WHITE);
		scorePaint.setAntiAlias(true);
		scorePaint.setTextSize(20);
		
		hscorePaint = new Paint();
		hscorePaint.setColor(Color.WHITE);
		hscorePaint.setAntiAlias(true);
		hscorePaint.setTextSize(20);
		
		red=new Paint();
		red.setColor(Color.WHITE);
		red.setAntiAlias(true);
        this.setOnTouchListener(this);
        
        sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		oinkSound = sounds.load(mycontext, R.raw.oink, 1);
		
		sounds1 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		jump = sounds1.load(mycontext, R.raw.jump,  1);
		
		sounds3 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		yell = sounds3.load(mycontext, R.raw.yell, 1);
		
		
		
		hilo=new Thread(this);
		hilo.start();
		toUp=false;
		downed=true;
		isrunning=true;
		one = false;
	}

	@Override
	public void run() {
		// actual gameEngine.....
		// game renderer
		// game analysis
		while(isrunning)
		{	
			//flag:1-20 for running pig 
			if(flag>20)
			{
				flag=1;
			}
			if(!holder.getSurface().isValid())
			{
				continue;		
			}
			// do whatever you want
			Canvas c=holder.lockCanvas();
			
			//setting x of fire to start at last (hidden)
			if(fx <= 0){
				fx = c.getWidth() + 10;
				//score increment for successful jump over the fire
				score++;
				if(score > highscore){
					highscore = score;
					hscorePaint.setColor(Color.YELLOW);
					flagscore = true;
				}
				//speed of fire increases every score of multiple 5
				if(score!=0&&score%6==0){
					firespeed = firespeed + 1;
				}
			}
			
			if(one){
				if((forestx1+forest.getWidth())<=c.getWidth()){
					one = false;
					forestx = c.getWidth()-firespeed;
					
				}
			}else{
				if((forestx+forest.getWidth())<=c.getWidth()){
					one = true;
					forestx1 = c.getWidth()-firespeed;
				}
			}

			initVals(c);
			//if pig and fire collide
			if((((fx+20) < (px + pig.getWidth())) && ((fx + fire.getWidth())>(px+20)))&& ((ypig +pig.getHeight())>(yfire+30))){
				gameover();
			}else{

				animateme();// animate();
			}
			todraw(c); // render
			flag++;
			holder.unlockCanvasAndPost(c);
		}
		//if game over 
		if(working){
			AudioManager audioManager = (AudioManager) mycontext.getSystemService(Context.AUDIO_SERVICE);
			float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	        sounds3.play(yell, volume, volume, 1, 0, 1); 
		Intent i = new Intent(mycontext,ScoreViewer.class);
		i.putExtra("score", score);
		mycontext.startActivity(i);
		mycontext.finish();
		}
	}
	public void gameover(){
		isrunning = false;
		pig=deadpig;
	}
	
	public void animateme()
	{
		//for fire to run
		fx=fx-firespeed;
		
		if(one){
			if(forestx+forest.getWidth()>0)
				forestx = forestx - firespeed;
			forestx1 = forestx1 - firespeed;
		}else{
			if(forestx1+forest.getWidth()>0)
				forestx1 = forestx1 - firespeed;
			forestx = forestx - firespeed;
		}		
		
		//for jumping
		if(toUp){
			if(ht<=maxht){
				ht=ht+2;
				pig = pigjump1;
			}else{
				toUp=false;
				pig = pigjump2;
			}
		}else{
			if(ht>=0){
				ht=ht-2;
				pig = pigjump3;
			}else{
				downed=true;
				if(flag == 10){
					pig = pig1;
				}
				if(flag == 20){
					pig = pig2;
				}
			}
		}
	}
	
	public void initVals(Canvas c){
		y = c.getHeight() - b.getHeight();
		yfire = (c.getHeight() - fire.getHeight())-40;
		ygrass = c.getHeight() - grass.getHeight();
		yforest = (c.getHeight() - forest.getHeight())+20;
		minht= (c.getHeight() - pig.getHeight())-40;
		ypig = minht - ht;
	}
	
	public void todraw(Canvas c)
	{
		c.drawRect(0,0,this.getWidth(),this.getHeight(),red);
		if(one){
			if(forestx+forest.getWidth()>0){
				c.drawBitmap(sky, forestx,0 ,null);
				c.drawBitmap(forest, forestx, yforest,null);
				
			}

			c.drawBitmap(sky, forestx1, 0,null);
			c.drawBitmap(forest, forestx1, yforest,null);
		}else{
			if(forestx1+forest.getWidth()>0){
				c.drawBitmap(sky, forestx1,0 ,null);
				c.drawBitmap(forest, forestx1, yforest,null);
			}
			c.drawBitmap(sky, forestx,0 ,null);
			c.drawBitmap(forest, forestx, yforest,null);
		}
		c.drawBitmap(fire,fx,yfire,null);
		c.drawBitmap(pig,px,ypig,null);
		c.drawText("Your score : "+score, 5, 60,  scorePaint);
		if(flagscore){
			c.drawText("NEW Best Score! "+highscore, 5, 30, hscorePaint);
		}else{
			c.drawText("Best Score: "+highscore, 5, 30, hscorePaint);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {	
		
		if(downed){
			toUp=true;
			downed=false;
		}
		AudioManager audioManager = (AudioManager) mycontext.getSystemService(Context.AUDIO_SERVICE);
		float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volume1 = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sounds1.play(jump, volume1, volume1, 1, 0, 1); 
        sounds.play(oinkSound, volume, volume, 1, 0, 1); 
		return false;
	}
	
	public void destroyed(){
		isrunning = false;
		working = false;
	}
	

}

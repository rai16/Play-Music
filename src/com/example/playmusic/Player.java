package com.example.playmusic;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Player extends Activity implements OnCompletionListener, OnSeekBarChangeListener {
	ImageButton play,pause,rewind,forward,next,prev,repeat,shuffle;
	SeekBar sb;
	TextView time_el,total_time;
	MediaPlayer mp=new MediaPlayer();
	int songCount,position;
	private Handler myHandler = new Handler();
	private long startTime = 0;
	private long finalTime = 0;
	private int forwardTime = 5000;
	private int backwardTime = 5000;
	boolean isRepeat=false;
	boolean isShuffle=false;
	private Utilities utils=new Utilities();
	ArrayList<Long> id=new ArrayList<Long>();
	ArrayList<String> title=new ArrayList<String>();
	ArrayList<String> artist=new ArrayList<String>();
	ArrayList<String> art=new ArrayList<String>();
	
	
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		position=getIntent().getIntExtra("position", 0);
	//	Toast.makeText(this, "reached 0", Toast.LENGTH_LONG ).show();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
	//	Toast.makeText(this, "reached 1", Toast.LENGTH_LONG ).show();
		position=getIntent().getIntExtra("position", -1);
		ActionBar actionBar =getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		SongsManager sm=new SongsManager();
		id=sm.getSongId(this);
		title=sm.getSongtitle(this);
		artist=sm.getSongartist(this);
		art=sm.getSongart(this);
		play=(ImageButton) findViewById(R.id.btnPlay);
		rewind=(ImageButton) findViewById(R.id.btnBackward);
		forward=(ImageButton) findViewById(R.id.btnForward);
		next=(ImageButton) findViewById(R.id.btnNext);
		prev=(ImageButton) findViewById(R.id.btnPrevious);
		repeat=(ImageButton) findViewById(R.id.btnRepeat);
		shuffle=(ImageButton) findViewById(R.id.btnShuffle);
		time_el=(TextView) findViewById(R.id.elapsedT);
		total_time=(TextView) findViewById(R.id.totalT);
		sb=(SeekBar) findViewById(R.id.songProgressBar);
		sb.setOnSeekBarChangeListener(this);
		mp.setOnCompletionListener(this);
	//	Toast.makeText(this, "reached 3", Toast.LENGTH_LONG ).show();
		playSong(position);
	//	Toast.makeText(this, "reached 4", Toast.LENGTH_LONG ).show();
		
		play.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        play.setImageResource(R.drawable.btn_play);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        play.setImageResource(R.drawable.btn_pause);
                    }
                }
 
            }
        });
		
		forward.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + forwardTime <= mp.getDuration()){
                    // forward song
                    mp.seekTo(currentPosition + forwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });
		
		
		rewind.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - backwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - backwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }
 
            }
        });
 
		
		
		next.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(position < (id.size() - 1))
                 {
                    playSong(position + 1);
                    position = position + 1;
                }else{
                    // play first song
                    playSong(0);
                    position = 0;
                }
 
            }
        });
		
		
		
		prev.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                if(position > 0){
                    playSong(position - 1);
                    position = position - 1;
                }else{
                    // play last song
                    playSong(id.size() - 1);
                    position = position - 1;
                }
 
            }
        });
		
		
		
		repeat.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                if(isRepeat){
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                   repeat.setImageResource(R.drawable.btn_repeat);
                }else{
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    repeat.setImageResource(R.drawable.btn_repeat_focused);
                    shuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });
		
		
		
		shuffle.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                if(isShuffle){
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    shuffle.setImageResource(R.drawable.btn_shuffle);
                }else{
                    // make repeat to true
                    isShuffle= true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    shuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    repeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });
		
	}
	

	
	long getId(Object object){
    	ContentResolver musicResolver=getContentResolver();
    	Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor=musicResolver.query(musicUri, null, null, null, null);
		while(musicCursor.moveToNext()){
			if(musicCursor.getString(musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)).equals(object)){
		            return (musicCursor.getLong(musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)));
			}
		}
		return -1;
    }
	
	
	 public void  playSong(int songIndex){
	        ActionBar ab=getActionBar();
	        ab.setTitle(title.get(songIndex));
	        ab.setSubtitle(artist.get(songIndex));
	        try {
	            mp.reset();
	            Uri trackUri=ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.get(songIndex));
	           mp.setDataSource(this, trackUri);
	            mp.prepare();
	            mp.start();
	           
	 
	            // Changing Button Image to pause image
	            play.setImageResource(R.drawable.btn_pause);
	 
	            // set Progress bar values
	            finalTime = mp.getDuration();
	            startTime = mp.getCurrentPosition();
	            sb.setMax((int) finalTime);
	            sb.setProgress((int)startTime);
	            total_time.setText(utils.milliSecondsToTimer(finalTime));
	            
	            myHandler.postDelayed(UpdateSongTime,100);
	            // Updating progress bar
	           
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 
	 
	 private Runnable UpdateSongTime = new Runnable() {
	      public void run() {
	         startTime = mp.getCurrentPosition();
	         time_el.setText(utils.milliSecondsToTimer(startTime));
	         sb.setProgress((int)startTime);
	         myHandler.postDelayed(this, 100);
	      }
	   };
	   
	   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		   
	    }
	 
	    /**
	     * When user starts moving the progress handler
	     * */
	    
	    public void onStartTrackingTouch(SeekBar seekBar) {
	        // remove message Handler from updating progress bar
	        myHandler.removeCallbacks(UpdateSongTime);
	    }
	 
	    /**
	     * When user stops moving the progress hanlder
	     * */
	  
	    public void onStopTrackingTouch(SeekBar seekBar) {
	        myHandler.removeCallbacks(UpdateSongTime);
	        
	        int currentPosition = sb.getProgress();
	 
	       
	        mp.seekTo(currentPosition);
	 
	        
	        myHandler.postDelayed(UpdateSongTime, 100);
	    }
	    
	    
	   @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			mp.stop();
		}



	public void onCompletion(MediaPlayer arg0) {
		   
	       
	        if(isRepeat){
	            
	            playSong(position);
	        } else if(isShuffle){
	            
	            Random rand = new Random();
	            position = rand.nextInt((id.size() - 1) - 0 + 1) + 0;
	            playSong(position);
	        } else{
	           
	            if(position < (id.size() - 1)){
	                playSong(position+ 1);
	                position = position + 1;
	            }else{
	                // play first song
	                playSong(0);
	                position = 0;
	            }
	        }
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

}

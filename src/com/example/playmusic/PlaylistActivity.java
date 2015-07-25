package com.example.playmusic;

import java.io.FileDescriptor;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class PlaylistActivity extends Activity {
	private ArrayList<String> songs=new ArrayList<String>();
	private ArrayList<String> albums=new ArrayList<String>();
	private ArrayList<Long> durations=new ArrayList<Long>();

	ListView songList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist);
		songList=(ListView) findViewById(R.id.listView1);
		getSongList();
		
		CustomList adapter=new CustomList(this, songs, albums, durations);
		songList.setAdapter(adapter);
		songList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 Intent i=new Intent("com.example.playmusic.PlayerActivity");
				 i.putExtra("position", arg2);
				 startActivity(i);
				 
				
			}
		});

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playlist, menu);
		return true;
	}
	
	public void getSongList(){
		ContentResolver musicResolver=getContentResolver();
		Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor=musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			
			int titleColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
			int durationColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DURATION);
			int albumCoumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM);
			do{
				songs.add(musicCursor.getString(titleColumn));
				albums.add(musicCursor.getString(albumCoumn));
				durations.add(musicCursor.getLong(durationColumn));
			
			}
			
			while(musicCursor.moveToNext());
		}
		
	}
	
	
	
	

}

package com.example.playmusic;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class SongsManager {
	
	private ArrayList<Long> id=new ArrayList<Long>();
	private ArrayList<String> title=new ArrayList<String>();
	private ArrayList<String> artist=new ArrayList<String>();
	private ArrayList<String> art=new ArrayList<String>();
	
	
	
	public ArrayList<Long> getSongId(Context context){
		ContentResolver musicResolver=context.getContentResolver();
		Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor=musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			int idColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			do{
				
				id.add(musicCursor.getLong(idColumn));
			}
			
			while(musicCursor.moveToNext());
		}
		return id;
	}
	
	public ArrayList<String> getSongtitle(Context context){
		ContentResolver musicResolver=context.getContentResolver();
		Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor=musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			int titleColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			do{
				
				title.add(musicCursor.getString(titleColumn));
			}
			
			while(musicCursor.moveToNext());
		}
		return title;
	}

	public ArrayList<String> getSongartist(Context context){
		ContentResolver musicResolver=context.getContentResolver();
		Uri musicUri=android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor=musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			int artistColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
			do{
				
				artist.add(musicCursor.getString(artistColumn));
			}
			
			while(musicCursor.moveToNext());
		}
		return artist;
	}

	
	public ArrayList<String> getSongart(Context context){
		ContentResolver musicResolver=context.getContentResolver();
		
		String[] columns = { android.provider.MediaStore.Audio.Albums._ID, android.provider. MediaStore.Audio.Albums.ALBUM,android.provider.MediaStore.Audio.Albums.ALBUM_ART };
		int album_column_index=0; 
		Cursor cursor = musicResolver.query( MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, null, null, null);
		
		
		
		
		
		if(cursor!=null && cursor.moveToFirst()){
			
			do{
				
				
                
                int albumID = cursor.getInt(album_column_index);
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumID);
                art.add(cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART)));
			}
			
			while(cursor.moveToNext());
		}
		return art;
	}


}

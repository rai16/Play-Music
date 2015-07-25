package com.example.playmusic;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
	private final Activity context;
	private final ArrayList<String> song;
	private final ArrayList<String> album;
	private final ArrayList<Long> duration;

	public CustomList(Activity context,ArrayList<String> song,ArrayList<String> album,ArrayList<Long> duration){
		super(context, R.layout.playlist_item, song);
		this.context=context;
		this.song=song;
		this.album=album;
		this.duration=duration;
		
	
	}
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.playlist_item, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
		TextView txtTitle1 = (TextView) rowView.findViewById(R.id.textView1);
		TextView d=(TextView) rowView.findViewById(R.id.duration);
		txtTitle.setText(song.get(position));
		txtTitle1.setText(album.get(position));
		Utilities utils=new Utilities();
		d.setText(utils.milliSecondsToTimer(duration.get(position)));
		
		return rowView;
		}

}

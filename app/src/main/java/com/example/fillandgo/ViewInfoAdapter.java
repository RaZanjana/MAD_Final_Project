package com.example.fillandgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ViewInfoAdapter implements GoogleMap.InfoWindowAdapter {
private final View mWindow;
private Context mContext;
public ViewInfoAdapter(Context context){
    mContext=context;
    mWindow= LayoutInflater.from(context).inflate(R.layout.custom,null);
}
private void rendoWindow(Marker marker,View view){
    String title=marker.getTitle();
    TextView textView = (TextView)view.findViewById(R.id.title);
    if(!title.equals("")){
        textView.setText(title);
    }
    String snippet=marker.getSnippet();
    TextView textView1 = (TextView)view.findViewById(R.id.title1);
    if(!snippet.equals("")){
        textView1.setText(snippet);
    }
}
    @Override
    public View getInfoWindow(Marker marker) {
    rendoWindow(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendoWindow(marker,mWindow);
        return mWindow;
    }
}

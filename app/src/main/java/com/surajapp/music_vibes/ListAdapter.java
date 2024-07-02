package com.surajapp.music_vibes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

public class ListAdapter extends ArrayAdapter<String> {
    public ListAdapter(@NonNull Context context,int resource, String[] item) {
        super(context,R.layout.activity_my_adapter,item);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_my_adapter,parent,false);
        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView songName = convertView.findViewById(R.id.songName1);
        // set image
        setRandomColor(imageView);
        //imageView.setBackgroundResource(android.R.color.transparent);
        songName.setText(getItem(position));

        return convertView;
    }

    private void setRandomColor(ImageView img) {
        Random random = new Random();
        int color = Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
        img.setBackgroundColor(color);
    }
}
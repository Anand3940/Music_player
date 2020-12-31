package com.Music_play.musicplay;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    String[] array;
    LayoutInflater layoutInflater;
    public CustomAdapter(Activity context, String[] objects) {
        super(context, R.layout.listview, objects);
        array = objects;
        layoutInflater = context.getLayoutInflater();
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview, parent, false);
            ImageView imageView=(ImageView)view.findViewById(R.id.adapterimageview);
            TextView textView=(TextView)view.findViewById(R.id.adaptertextview);
            textView.setText(array[position]);
        }
        return view;
    }
}

package com.example.students.songsconcordance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Guy on 01/09/2015.
 */

class CustomAdapter extends ArrayAdapter<String>{
    public CustomAdapter(Context context, String[] options) {
        super(context, R.layout.custom_row, options);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater menuInflater = LayoutInflater.from(getContext());
        View customView = menuInflater.inflate(R.layout.custom_row, parent, false);

        String singleTextItem = getItem(position);
        TextView textView = (TextView) customView.findViewById(R.id.menuText);
        ImageView imageView = (ImageView) customView.findViewById(R.id.menuImage);

        textView.setText(singleTextItem);
        imageView.setImageResource(R.mipmap.ic_launcher);
        return customView;
    }
}

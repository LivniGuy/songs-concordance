package com.example.students.songsconcordance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Guy on 13/09/2015.
 */
public class SongAdapter extends ArrayAdapter<Song> {

    private Context context;
    private List<Song> songList;

    public SongAdapter(Context context, int resource, List<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.songList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_song, parent, false);

        //Display song name in the TextView widget
        Song song = songList.get(position);
        TextView textSongName = (TextView) view.findViewById(R.id.textSongName);
        textSongName.setText(song.getNAME());
        TextView textPerformerName = (TextView) view.findViewById(R.id.textPerformerName);
        textPerformerName.setText(song.getPERFORMER_NAME());
        //CharSequence test = song.getALBUM_NAME();
        //Save ID in invisible view for songslyrics.php query parameter
        TextView textSongID = (TextView) view.findViewById(R.id.textSongID);
        textSongID.setText(String.valueOf(song.getID()));

        TextView textAlbumName = (TextView) view.findViewById(R.id.textSongAlbumName);
        textAlbumName.setText(song.getALBUM_NAME());

        TextView textSongWriterName = (TextView) view.findViewById(R.id.textSongWriterName);
        textSongWriterName.setText(song.getSONG_WRITER_NAME());

        return view;
    }

    class SongAndView {
        public Song song;
        public View view;
    }
}

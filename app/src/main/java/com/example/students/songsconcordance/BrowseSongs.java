package com.example.students.songsconcordance;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.students.songsconcordance.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BrowseSongs extends AppCompatActivity {

    public ProgressBar pb;
    public List<Song> songList;
    public int chosenSongID;
    public String chosenSongName;
    public String chosenSongPerformerName;
    public String chosenSongAlbumName;
    public String chosenSongWriterName;

    public Song chosenSong;
    public SongSearchParams ssp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_songs);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        //songID = intent.getStringExtra(MainActivity.SONG);
        ssp = (SongSearchParams) intent.getSerializableExtra(MainActivity.SEARCH_PARAMS);

        if (Utils.isOnline(getBaseContext())) {
            requestData();

            //Handle songsListView clicks
            ListView lv = (ListView) findViewById(android.R.id.list);
            lv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            // get songid
                            TextView tv = (TextView) view.findViewById(R.id.textSongID);
                            chosenSongID = Integer.parseInt(tv.getText().toString());
                            //chosenSong.setID(Integer.parseInt(chosenSongID));

                            // get songname
                            tv = (TextView) view.findViewById(R.id.textSongName);
                            chosenSongName = String.valueOf(tv.getText());
//                            chosenSong.setNAME(chosenSongName);

                            // get songperformername
                            tv = (TextView) view.findViewById(R.id.textPerformerName);
                            chosenSongPerformerName = String.valueOf(tv.getText());
//                            chosenSong.setPERFORMER_NAME(chosenSongPerformerName);

                            // get songalbumname
                            tv = (TextView) view.findViewById(R.id.textSongAlbumName);
                            chosenSongAlbumName = String.valueOf(tv.getText());
//                            chosenSong.setALBUM_NAME(chosenSongAlbumName);

                            // get songwritername
                            tv = (TextView) view.findViewById(R.id.textSongWriterName);
                            chosenSongWriterName = String.valueOf(tv.getText());

                            chosenSong = new Song(chosenSongID, chosenSongName, chosenSongPerformerName, chosenSongAlbumName, 0,
                                    0, chosenSongWriterName);
                            //Toast.makeText(BrowseSongs.this, chosenSongID, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(BrowseSongs.this, SongLyrics.class);
                            //intent.putExtra(MainActivity.SONG_ID, chosenSongID);
                            intent.putExtra(MainActivity.SONG, chosenSong);
                            startActivity(intent);
                        }
                    }
            );
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestData() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        SongsAPI api = adapter.create(SongsAPI.class);

        Map<String, String> params = new HashMap<String, String>();

        if (ssp != null) {
            if (!ssp.getNAME().isEmpty()) {
                params.put("NAME", "\"" + ssp.getNAME() + "\"");
            }
            if (!ssp.getPERFORMER_NAME().isEmpty()) {
                params.put("PERFORMER_NAME", "\"" + ssp.getPERFORMER_NAME() + "\"");
            }
            if (!ssp.getALBUM_NAME().isEmpty()) {
                params.put("ALBUM_NAME", "\"" + ssp.getALBUM_NAME() + "\"");
            }
            if (!ssp.getSONG_WRITER_NAME().isEmpty()) {
                params.put("SONG_WRITER_NAME", "\"" + ssp.getSONG_WRITER_NAME() + "\"");
            }
            if (!ssp.getFilterWords().isEmpty()) {
                params.put("WORDS", "\"" + ssp.getFilterWords().toString() + "\"");
            }
            Toast.makeText(this, params.toString(), Toast.LENGTH_LONG).show();
        }

        api.getSongFeed(params, new Callback<List<Song>>() {
            @Override
            public void success(List<Song> Songs, Response response) {
                songList = Songs;
                updateDisplay();
                pbInvisible();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", "failure");
                pbInvisible();
            }
        });
    }

    private void pbInvisible() {
        pb.setVisibility(View.INVISIBLE);
    }

    private void updateDisplay() {
        //Use SongAdapter to display data
        SongAdapter adapter = new SongAdapter(this, R.layout.item_song, songList);
        ListView songsListView = (ListView) findViewById(android.R.id.list);
        songsListView.setAdapter(adapter);
//        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse_songs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

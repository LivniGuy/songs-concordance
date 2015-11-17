package com.example.students.songsconcordance;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongLyrics extends AppCompatActivity {

    public String songLyrics;
    public Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_lyrics);

        Intent intent = getIntent();
        song = (Song) intent.getSerializableExtra(MainActivity.SONG);
        Toast.makeText(this, song.getNAME(), Toast.LENGTH_SHORT).show();

        TextView tv = (TextView) findViewById(R.id.textSongName);
        tv.setText(song.getNAME());

        tv = (TextView) findViewById(R.id.textSongPerformerName);
        tv.setText("By: " + song.getPERFORMER_NAME());

        tv = (TextView) findViewById(R.id.textSongAlbumName);
        tv.setText("Album: " + song.getALBUM_NAME());

        tv = (TextView) findViewById(R.id.textSongWriterName);
        tv.setText("Writer: " + song.getSONG_WRITER_NAME());

        if (isOnline()) {
            requestData();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestData() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        SongsAPI api = adapter.create(SongsAPI.class);

        api.getSongLyricsFeed(String.valueOf(song.getID()), new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                songLyrics = s;
                updateDisplay();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", "failure");
            }
        });


    }

    private void updateDisplay() {
        TextView textLyrics = (TextView) findViewById(R.id.textLyrics);

        textLyrics.setText(songLyrics);
        textLyrics.setMovementMethod(new ScrollingMovementMethod());
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_lyrics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_song_lyrics_index) {
            showSongLyricsIndex();
            return true;
        }
        if (id == R.id.action_search_word_by_location) {
            showWordSearchByLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSongLyricsIndex() {
        Intent intent = new Intent(SongLyrics.this, SongLyricsIndex.class);
        intent.putExtra(MainActivity.SONG, song);
        startActivity(intent);
    }

    private void showWordSearchByLocation() {
        Intent intent = new Intent(SongLyrics.this, WordSearchByLocation.class);
        intent.putExtra(MainActivity.SONG, song);
        startActivity(intent);
    }
}

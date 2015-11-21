package com.example.students.songsconcordance;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.students.songsconcordance.utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WordSearchByLocation extends AppCompatActivity {

    public Song song;
    public String wordResult;
    RestAdapter adapter;
    SongsAPI api;
    String wordLine, wordInLine, stanza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word_by_location);

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

        adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        api = adapter.create(SongsAPI.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_lyrics_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchWord(View view) {
        //Getting the search params from the layout
        EditText editWordLine = (EditText) findViewById(R.id.editLine);
        EditText editWordInLine = (EditText) findViewById(R.id.editWordInLine);
        EditText editStanza = (EditText) findViewById(R.id.editStanza);

        //Setting the search params for the Browse Songs activity
        WordByLocationParams wblp = new WordByLocationParams(String.valueOf(song.getID()),
                editWordLine.getText().toString(),
                editWordInLine.getText().toString(),
                editStanza.getText().toString());

        this.wordLine = wblp.getWordLine();
        this.wordInLine = wblp.getWordInLine();
        this.stanza = wblp.getStanza();

        if (Utils.isOnline(getBaseContext())) {
            requestData();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestData() {
        api.getWordByLocationFeed(String.valueOf(song.getID()), wordLine,wordInLine,stanza,
                new Callback<String>() {
                    @Override
                    public void success(String w, Response response) {
                        wordResult = w;
                        updateDisplay();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("ERROR: " , "failure - " + error.getMessage());
                    }
                });
    }

    private void updateDisplay() {
        TextView textLyricsIndex = (TextView) findViewById(R.id.wordResult);

        textLyricsIndex.setText("Word in requested location is: " + wordResult);
        textLyricsIndex.setMovementMethod(new ScrollingMovementMethod());
    }
}

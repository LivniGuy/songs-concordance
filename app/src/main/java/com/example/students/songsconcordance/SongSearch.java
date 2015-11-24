package com.example.students.songsconcordance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongSearch extends AppCompatActivity {

    public ListView listWords;
    public SongSearchParams ssp;
    public ArrayList<String> filterWords;
    public ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        listWords = (ListView) findViewById(R.id.listWords);
        filterWords = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                filterWords);

        listWords.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_search, menu);
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

    public void addWord(View view) {
        EditText editWordInSong = (EditText) findViewById(R.id.editWordInSong);
        filterWords.add(editWordInSong.getText().toString());
        arrayAdapter.notifyDataSetChanged();
    }

    public void searchSongs(View view) {
        //Getting the search params from the layout
        EditText editSongName = (EditText) findViewById(R.id.editSongName);
        EditText editPerformerName = (EditText) findViewById(R.id.editPerformerName);
        EditText editAlbumName = (EditText) findViewById(R.id.editAlbumName);
        EditText editSongWriterName = (EditText) findViewById(R.id.editSongWriterName);

        //Setting the search params for the Browse Songs activity
        ssp = new SongSearchParams(editSongName.getText().toString(),
                editPerformerName.getText().toString(),
                editAlbumName.getText().toString(),
                editSongWriterName.getText().toString(),
                filterWords);

        Intent intent = new Intent(SongSearch.this, BrowseSongs.class);
        //intent.putExtra(MainActivity.SONG_ID, chosenSongID);
        intent.putExtra(MainActivity.SEARCH_PARAMS, ssp);
        startActivity(intent);
    }
}

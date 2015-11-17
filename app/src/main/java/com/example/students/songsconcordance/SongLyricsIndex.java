package com.example.students.songsconcordance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongLyricsIndex extends AppCompatActivity {
    static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();
    public Song song;
    public String songLyricsIndex;
    public RestAdapter adapter;
    public SongsAPI api;
    public String chosenWordGroupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_lyrics_index);

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

        if (isOnline()) {
            requestData();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestData() {
        api.getSongLyricsIndexFeed(String.valueOf(song.getID()),
                userInstance.getUser().getID(),
                chosenWordGroupID,
                new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                songLyricsIndex = s;
                updateDisplay();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", "failure");
            }
        });
    }

    private void updateDisplay() {
        TextView textLyricsIndex = (TextView) findViewById(R.id.textLyricsIndex);

        textLyricsIndex.setText(songLyricsIndex);
        textLyricsIndex.setMovementMethod(new ScrollingMovementMethod());
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
        getMenuInflater().inflate(R.menu.menu_song_lyrics_index, menu);
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
        if (id == R.id.action_index_for_word_group) {
            showWordGroupDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showWordGroupDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SongLyricsIndex.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Word Group:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                SongLyricsIndex.this,
                android.R.layout.select_dialog_singlechoice);
        final ArrayList<String> wordGroupIDs = new ArrayList<String>();

        // Get user's word groups from server
        api.getWordGroupsFeed(userInstance.getUser().getID(), new Callback<List<JsonObject>>() {
            @Override
            public void success(List<JsonObject> jsonObjects, Response response) {
                for (JsonObject jo : jsonObjects) {
                    arrayAdapter.add(jo.get("WORD_GROUP_NAME").getAsString());
                    wordGroupIDs.add(jo.get("WORD_GROUP_ID").getAsString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", "failure");
            }
        });

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        chosenWordGroupID = wordGroupIDs.get(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                SongLyricsIndex.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builderInner.show();
                        requestData();
                    }
                });
        builderSingle.show();
    }
}

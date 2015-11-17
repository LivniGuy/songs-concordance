package com.example.students.songsconcordance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SongLyrics extends AppCompatActivity {
    static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();

    public TextView textLyrics;
    public String songLyrics;
    public Song song;

    public RestAdapter adapter;
    public SongsAPI api;
    public String chosenLinguisticExpressionID;
    public String chosenLinguisticExpression;


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

        textLyrics = (TextView) findViewById(R.id.textLyrics);

        if (isOnline()) {
            requestData();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    private void requestData() {
        adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        api = adapter.create(SongsAPI.class);

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
        if (id == R.id.action_search_linguistic_expression) {
            showLinguisticExpressionDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLinguisticExpressionDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SongLyrics.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Linguistic Expression:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                SongLyrics.this,
                android.R.layout.select_dialog_singlechoice);
        final ArrayList<String> linguisticExpressionIDs = new ArrayList<String>();

        // Get user's linguistic expressions from server
        api.getLingExpsFeed(userInstance.getUser().getID(),
                new Callback<List<JsonObject>>() {
                    @Override
                    public void success(List<JsonObject> jsonObjects, Response response) {
                        for (JsonObject jo : jsonObjects) {
                            arrayAdapter.add(jo.get("LINGUISTIC_EXPRESSION").getAsString());
                            linguisticExpressionIDs.add(jo.get("USER_LING_EXP_ID").getAsString());
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
                        chosenLinguisticExpression = strName;
                        chosenLinguisticExpressionID = linguisticExpressionIDs.get(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                SongLyrics.this);
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

                        markText();
                    }
                });
        builderSingle.show();
    }

    private void markText() {
        int lastIndex = 0;
        int count = 0;
        ArrayList<Integer> spanIndex = new ArrayList<>();
        SpannableString styledString = new SpannableString(textLyrics.getText());
        BackgroundColorSpan[] backgroundColorSpans = styledString.getSpans(0, styledString.length(), BackgroundColorSpan.class);
        ForegroundColorSpan[] foregroundColorSpans = styledString.getSpans(0, styledString.length(), ForegroundColorSpan.class);

        for (BackgroundColorSpan span : backgroundColorSpans) {
            styledString.removeSpan(span);
        }
        for (ForegroundColorSpan span : foregroundColorSpans) {
            styledString.removeSpan(span);
        }

        // Find all occurences in text
        while(lastIndex != -1){
            lastIndex = styledString.toString().toLowerCase().indexOf(chosenLinguisticExpression.toLowerCase(), lastIndex);

            if(lastIndex != -1){
                spanIndex.add(lastIndex);
                count ++;
                lastIndex += chosenLinguisticExpression.length();
            }
        }

        // Mark all occurences in text
        for (Integer index:spanIndex) {
            styledString.setSpan(new BackgroundColorSpan(Color.LTGRAY), index, index+chosenLinguisticExpression.length(), 0);
            styledString.setSpan(new ForegroundColorSpan(Color.BLACK), index, index+chosenLinguisticExpression.length(), 0);
        }

        textLyrics.setText(styledString);
        
        Toast.makeText(this, "Found " + count + " matches", Toast.LENGTH_LONG).show();

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

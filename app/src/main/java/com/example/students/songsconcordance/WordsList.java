package com.example.students.songsconcordance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.students.songsconcordance.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WordsList extends AppCompatActivity {

    public RestAdapter adapter;
    public SongsAPI api;
    public String chosenSongID;
    public String chosenSong;

    public ListView listWords;
    public ArrayList<String> wordsArray;
    public ArrayAdapter<String> wordsArrayAdapter;

    public SongSearchParams ssp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        listWords = (ListView)findViewById(R.id.listWords);

        wordsArray = new ArrayList<>();

        wordsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsArray);
        listWords.setAdapter(wordsArrayAdapter);

        listWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> filterWords = new ArrayList<>();

                //Setting the search params for the Browse Songs activity
                filterWords.add(adapterView.getItemAtPosition(i).toString());
                ssp = new SongSearchParams("", "", "", "", filterWords);

                Intent intent = new Intent(WordsList.this, BrowseSongs.class);
                //intent.putExtra(MainActivity.SONG_ID, chosenSongID);
                intent.putExtra(MainActivity.SEARCH_PARAMS, ssp);
                intent.putExtra(MainActivity.WORD_TO_MARK, filterWords.get(0));
                startActivity(intent);
            }
        });


        if (Utils.isOnline(getBaseContext())) {
            showSongsDialog();
        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }

    public void showSongsDialog() {
        adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        api = adapter.create(SongsAPI.class);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(WordsList.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Song:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                WordsList.this,
                android.R.layout.select_dialog_singlechoice);
        final ArrayList<Integer> songIDs = new ArrayList<>();

        // Get songs from server
        Map<String, String> params = new HashMap<String, String>();

        api.getSongFeed(params, new Callback<List<Song>>() {
            @Override
            public void success(List<Song> songs, Response response) {
                for (Song song : songs) {
                    arrayAdapter.add(song.getNAME() + ", " + song.getPERFORMER_NAME());
                    songIDs.add(song.getID());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", "failure");
            }
        });

        builderSingle.setNegativeButton(
                "All Songs",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Toast.makeText(WordsList.this, "All...", Toast.LENGTH_LONG).show();

                        api.getWordsList("", new Callback<List<JsonObject>>() {
                            @Override
                            public void success(List<JsonObject> jsonObjects, Response response) {
                                updateDisplay(jsonObjects);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(WordsList.this, "Error fetching wordslist", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        chosenSong = strName;
                        chosenSongID = songIDs.get(which).toString();
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                WordsList.this);
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

                        //markText();
                        api.getWordsList(chosenSongID, new Callback<List<JsonObject>>() {
                            @Override
                            public void success(List<JsonObject> jsonObjects, Response response) {
                                updateDisplay(jsonObjects);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(WordsList.this, "Error fetching wordslist", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
        builderSingle.show();
    }

    public void updateDisplay(List<JsonObject> jsonObjects) {
        for (JsonObject jo: jsonObjects
             ) {
            wordsArray.add(jo.get("WORD").getAsString());
        }

        wordsArrayAdapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_words_list, menu);
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

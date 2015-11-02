package com.example.students.songsconcordance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Guy on 13/09/2015.
 */
public class Song implements Serializable {

    private int ID;
    private String NAME;
    private String PERFORMER_NAME;
    private String ALBUM_NAME;
    private int WORD_COUNT;
    private int ROWS_COUNT;
    private String SONG_WRITER_NAME;

    public Song(int ID, String NAME, String PERFORMER_NAME, String ALBUM_NAME, int WORD_COUNT, int ROWS_COUNT, String SONG_WRITER_NAME) {
        this.ID = ID;
        this.NAME = NAME;
        this.PERFORMER_NAME = PERFORMER_NAME;
        this.ALBUM_NAME = ALBUM_NAME;
        this.WORD_COUNT = WORD_COUNT;
        this.ROWS_COUNT = ROWS_COUNT;
        this.SONG_WRITER_NAME = SONG_WRITER_NAME;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPERFORMER_NAME() {
        return PERFORMER_NAME;
    }

    public void setPERFORMER_NAME(String PERFORMER_NAME) {
        this.PERFORMER_NAME = PERFORMER_NAME;
    }

    public String getALBUM_NAME() {
        return ALBUM_NAME;
    }

    public void setALBUM_NAME(String ALBUM_NAME) {
        this.ALBUM_NAME = ALBUM_NAME;
    }

    public int getWORD_COUNT() {
        return WORD_COUNT;
    }

    public void setWORD_COUNT(int WORD_COUNT) {
        this.WORD_COUNT = WORD_COUNT;
    }

    public int getROWS_COUNT() {
        return ROWS_COUNT;
    }

    public void setROWS_COUNT(int ROWS_COUNT) {
        this.ROWS_COUNT = ROWS_COUNT;
    }

    public String getSONG_WRITER_NAME() {
        return SONG_WRITER_NAME;
    }

    public void setSONG_WRITER_NAME(String SONG_WRITER_NAME) {
        this.SONG_WRITER_NAME = SONG_WRITER_NAME;
    }

    public static class LingExpDef extends AppCompatActivity {
        static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();
        ListView listLingExps;
        ArrayList<String> arrayLingExps;
        ArrayAdapter<String> arrayAdapter;
        EditText editLingExp;
        RestAdapter adapter;
        SongsAPI api;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ling_exp_def);
            editLingExp = (EditText) findViewById(R.id.editLingExp);

            listLingExps = (ListView) findViewById(R.id.listLingExps);
            arrayLingExps = new ArrayList<String>();

            arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    arrayLingExps );

            listLingExps.setAdapter(arrayAdapter);

            listLingExps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LingExpDef.this);
                    builder.setMessage("Delete row?");

                    // Add the buttons
                    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            api.postDeleteObject(userInstance.getUser().getID(),
                                    "LINGUISTIC_EXPRESSION",
                                    adapterView.getItemAtPosition(i).toString(),
                                    new Callback<String>() {
                                        @Override
                                        public void success(String s, Response response) {
                                            //Toast.makeText(LingExpDef.this, s, Toast.LENGTH_LONG).show();
                                            //requestData();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.e("ERROR", "failure");
                                        }
                                    });
                            arrayLingExps.remove(i);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
            });

            adapter = new RestAdapter.Builder()
                    .setEndpoint(MainActivity.ENDPOINT)
                    .build();

            api = adapter.create(SongsAPI.class);

            requestData();
        }

        private void requestData() {
            if (isOnline()) {
                api.getLingExpsFeed(userInstance.getUser().getID(),
                        new Callback<List<JsonObject>>() {
                            @Override
                            public void success(List<JsonObject> jsonObjects, Response response) {
                                arrayLingExps.clear();
                                for (JsonObject jo : jsonObjects) {
                                    arrayLingExps.add(jo.get("LINGUISTIC_EXPRESSION").getAsString());
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("ERROR", "failure");
                            }
                        });
            } else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }
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


        public void addLingExp(View view) {
            if (!editLingExp.getText().toString().equals("")) {
                if (!arrayLingExps.contains(editLingExp.getText().toString())) {
                    arrayLingExps.add(editLingExp.getText().toString());
                    arrayAdapter.notifyDataSetChanged();

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("USER_ID", "\"" + userInstance.getUser().getID() + "\"");
                    params.put("LINGUISTIC_EXPRESSION", "\"" + editLingExp.getText().toString() + "\"");
                    editLingExp.setText("");

                    //Toast.makeText(this, params.toString(), Toast.LENGTH_LONG).show();

                    api.postDefineLingExp(params, new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            //Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                            //requestData();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("ERROR", "failure");
                        }
                    });
                }
            } else {
                Toast.makeText(this, "No linguistic expression entered", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_ling_exp_def, menu);
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
}

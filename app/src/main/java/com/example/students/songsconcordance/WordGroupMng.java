package com.example.students.songsconcordance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.students.songsconcordance.utils.Utils;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WordGroupMng extends AppCompatActivity {
    static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();
    ListView listWordGroups;
    ArrayList<String> arrayWordGroups;
    ArrayAdapter<String> arrayAdapter;
    RestAdapter adapter;
    SongsAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_group_mng);

        listWordGroups = (ListView) findViewById(R.id.listWordGroups);
        arrayWordGroups = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayWordGroups );

        listWordGroups.setAdapter(arrayAdapter);

        listWordGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WordGroupMng.this);
                builder.setMessage("Select option:");

                // Add the buttons
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked DELETE button
                        api.postDeleteObject(userInstance.getUser().getID(),
                                "WORD_GROUP",
                                adapterView.getItemAtPosition(i).toString(),
                                new Callback<String>() {
                                    @Override
                                    public void success(String s, Response response) {
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.e("ERROR", "failure");
                                    }
                                });
                        arrayWordGroups.remove(i);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked EDIT button
                        Intent intent = new Intent(WordGroupMng.this, WordGroupDef.class);
                        // Pass word_group_name to intent
                        intent.putExtra(MainActivity.WORD_GROUP_NAME, adapterView.getItemAtPosition(i).toString());
                        startActivity(intent);
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
        if (Utils.isOnline(getBaseContext())) {
            api.getWordGroupsFeed(userInstance.getUser().getID(),
                    new Callback<List<JsonObject>>() {
                        @Override
                        public void success(List<JsonObject> jsonObjects, Response response) {
                            arrayWordGroups.clear();
                            for (JsonObject jo : jsonObjects) {
                                arrayWordGroups.add(jo.get("WORD_GROUP_NAME").getAsString());
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

    public void navToWordGroupDef(View view) {
        Intent intent = new Intent(this, WordGroupDef.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_group_mng, menu);
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

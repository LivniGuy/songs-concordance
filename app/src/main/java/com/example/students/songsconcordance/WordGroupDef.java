package com.example.students.songsconcordance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WordGroupDef extends AppCompatActivity {
    static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();
    ListView listWords;
    ArrayList<String> arrayWords;
    ArrayAdapter<String> arrayAdapter;
    EditText editWordGroupName;
    EditText editWordInWordGroup;
    Button buttonDefine;

    public RestAdapter adapter;
    public SongsAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_group_def);
        editWordGroupName = (EditText) findViewById(R.id.editWordGroupName);
        editWordInWordGroup = (EditText) findViewById(R.id.editWordInWordGroup);
        listWords = (ListView) findViewById(R.id.listWords);
        arrayWords = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayWords );

        listWords.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        editWordGroupName.setText(intent.getStringExtra(MainActivity.WORD_GROUP_NAME));

        if (!editWordGroupName.getText().toString().equals("")) {
            editWordGroupName.setEnabled(false);
            buttonDefine = (Button) findViewById(R.id.buttonDefine);
            buttonDefine.setText("UPDATE WORD GROUP");
            getExistingWordGroupWords();
        }
    }

    private void getExistingWordGroupWords() {
        adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        api = adapter.create(SongsAPI.class);

        api.getWordGroupWordsFeed(userInstance.getUser().getID(),
                editWordGroupName.getText().toString(),
                new Callback<List<JsonObject>>() {
                    @Override
                    public void success(List<JsonObject> jsonObjects, Response response) {
                        for (JsonObject jo : jsonObjects) {
                            addWordToArray(jo.get("WORD").getAsString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    public void addWord(View view) {
        addWordToArray(editWordInWordGroup.getText().toString());
        editWordInWordGroup.setText("");
    }

    public void addWordToArray(String wordToAdd) {
        if (!wordToAdd.equals("")) {
            arrayWords.add(wordToAdd);
            arrayAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "No word entered", Toast.LENGTH_LONG).show();
        }
    }

    public void defineWordGroup(View view) {
        if (!editWordGroupName.getText().toString().equals("") && !arrayWords.isEmpty()) {
            adapter = new RestAdapter.Builder()
                    .setEndpoint(MainActivity.ENDPOINT)
                    .build();

            api = adapter.create(SongsAPI.class);
            EditText editWordGroupName = (EditText) findViewById(R.id.editWordGroupName);
            Map<String, String> params = new HashMap<String, String>();

            params.put("USER_ID", "\"" + userInstance.getUser().getID() + "\"");
            params.put("WORD_GROUP_NAME", "\"" + editWordGroupName.getText().toString() + "\"");
            params.put("WORD_GROUP_WORDS", "\"" + arrayWords.toString() + "\"");

            api.postDefineWordGroup(params, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("ERROR", "failure");
                }
            });
        } else {
            Toast.makeText(this, "Please enter all relevant fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_group_def, menu);
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

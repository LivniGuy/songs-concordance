package com.example.students.songsconcordance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
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
    }

    public void addWord(View view) {
        if (!editWordInWordGroup.getText().toString().equals("")) {
            arrayWords.add(editWordInWordGroup.getText().toString());
            editWordInWordGroup.setText("");
            arrayAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "No word entered", Toast.LENGTH_LONG).show();
        }
    }

    public void defineWordGroup(View view) {
        if (!editWordGroupName.getText().toString().equals("") && !arrayWords.isEmpty()) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(MainActivity.ENDPOINT)
                    .build();

            SongsAPI api = adapter.create(SongsAPI.class);
            EditText editWordGroupName = (EditText) findViewById(R.id.editWordGroupName);
            Map<String, String> params = new HashMap<String, String>();

            params.put("USER_ID", "\"" + userInstance.getUser().getID() + "\"");
            params.put("WORD_GROUP_NAME", "\"" + editWordGroupName.getText().toString() + "\"");
            params.put("WORD_GROUP_WORDS", "\"" + arrayWords.toString() + "\"");

//        Toast.makeText(this, params.toString(), Toast.LENGTH_LONG).show();

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

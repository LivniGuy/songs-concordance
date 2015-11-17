package com.example.students.songsconcordance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();
    EditText editUsername, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void login(View view) {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(MainActivity.ENDPOINT)
                .build();

        SongsAPI api = adapter.create(SongsAPI.class);
        Map<String, String> params = new HashMap<String, String>();

        params.put("USER_NAME", "\"" + editUsername.getText().toString() + "\"");
        params.put("PASSWORD", "\"" + editPassword.getText().toString() + "\"");

        api.getLogin(params, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (user != null) {
                    if (user.getUSER_NAME().equals(editUsername.getText().toString()) &&
                            user.getPASSWORD().equals(editPassword.getText().toString())) {
                        userInstance.setUser(user);

                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed, wrong credentials!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed, wrong credentials!", Toast.LENGTH_LONG).show();
                }
           }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(LoginActivity.this, "Problem trying to login...", Toast.LENGTH_LONG).show();
            }
        });
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

package com.example.students.songsconcordance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    static SingletonUser userInstance = SingletonUser.getSingletonUserInstance();
    String optionSelected;
    public static final String ENDPOINT =
            "http://songsopenu.dx.am";
//            "http://songsopenu.atwebpages.com";
//            "http://192.168.0.105/songsopenu";

    public final static String SONG = "com.example.students.songsconcordance.SONG";
    public final static String SONG_ID = "com.example.students.songsconcordance.SONGID";
    public final static String SEARCH_PARAMS = "com.example.students.songsconcordance.SEARCHPARAMS";
    public final static String USER = "com.example.students.songsconcordance.USER";
    public final static String WORD_TO_MARK = "com.example.students.songsconcordance.WORDTOMARK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handle option listview
        String[] options = {
                "Browse songs from list",
                "Search for song",
                "Manage word groups",
                "Manage linguistic expressions",
                "Show words list"
        };

        ListAdapter optionsAdapter = new CustomAdapter(this, options);
        ListView optionsListView = (ListView) findViewById(R.id.optionsListView);
        optionsListView.setAdapter(optionsAdapter);

        optionsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = null;
                        optionSelected = String.valueOf(adapterView.getItemAtPosition(position));
                        switch (position) {
                            case 0:
                                intent = new Intent(MainActivity.this, BrowseSongs.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(MainActivity.this, SongSearch.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(MainActivity.this, WordGroupMng.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(MainActivity.this, LingExpDef.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(MainActivity.this, WordsList.class);
                                startActivity(intent);
                                break;
                        }

                        //Toast.makeText(MainActivity.this, optionSelected, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_user_name).setTitle(userInstance.getUser().getUSER_NAME());
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

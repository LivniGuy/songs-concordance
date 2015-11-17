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

}

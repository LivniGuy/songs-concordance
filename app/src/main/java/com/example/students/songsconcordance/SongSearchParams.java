package com.example.students.songsconcordance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 22/09/2015.
 */
public class SongSearchParams implements Serializable {
    private String NAME;
    private String PERFORMER_NAME;
    private String ALBUM_NAME;
    private String SONG_WRITER_NAME;
    private ArrayList<String> filterWords;

    public SongSearchParams(String NAME, String PERFORMER_NAME, String ALBUM_NAME, String SONG_WRITER_NAME, ArrayList<String> filterWords) {
        this.NAME = NAME;
        this.PERFORMER_NAME = PERFORMER_NAME;
        this.ALBUM_NAME = ALBUM_NAME;
        this.SONG_WRITER_NAME = SONG_WRITER_NAME;
        this.filterWords = filterWords;
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

    public String getSONG_WRITER_NAME() {
        return SONG_WRITER_NAME;
    }

    public void setSONG_WRITER_NAME(String SONG_WRITER_NAME) {
        this.SONG_WRITER_NAME = SONG_WRITER_NAME;
    }

    public List<String> getFilterWords() {
        return filterWords;
    }

    public void setFilterWords(ArrayList<String> filterWords) {
        this.filterWords = filterWords;
    }
}

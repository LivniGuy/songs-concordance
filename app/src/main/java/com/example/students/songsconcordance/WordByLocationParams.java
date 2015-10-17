package com.example.students.songsconcordance;

import java.io.Serializable;

/**
 * Created by eladb on 10/16/15.
 */
public class WordByLocationParams implements Serializable {

    private String songId;
    private String wordLine;
    private String wordInLine;
    private String stanza;

    public WordByLocationParams(String songId, String wordLine, String wordInLine, String stanza) {
        this.songId = songId;
        this.wordLine = wordLine;
        this.wordInLine = wordInLine;
        this.stanza = stanza;
    }

    public String getWordLine() {
        return wordLine;
    }

    public String getWordInLine() {
        return wordInLine;
    }

    public String getStanza() {
        return stanza;
    }
}

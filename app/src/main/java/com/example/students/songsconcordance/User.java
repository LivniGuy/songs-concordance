package com.example.students.songsconcordance;

import java.io.Serializable;

/**
 * Created by Guy on 30/09/2015.
 */
public class User implements Serializable {
    private String ID;
    private String USER_NAME;
    private String FIRST_NAME;
    private String LAST_NAME;
    private String EMAIL;
    private String PASSWORD;


    public User() {
        this.ID = "";
        this.USER_NAME = "";
        this.FIRST_NAME = "";
        this.LAST_NAME = "";
        this.EMAIL = "";
        this.PASSWORD = "";
    }

    public User(String ID, String USER_NAME, String FIRST_NAME, String LAST_NAME, String EMAIL, String PASSWORD) {
        this.ID = ID;
        this.USER_NAME = USER_NAME;
        this.FIRST_NAME = FIRST_NAME;
        this.LAST_NAME = LAST_NAME;
        this.EMAIL = EMAIL;
        this.PASSWORD = PASSWORD;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}

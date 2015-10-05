package com.example.students.songsconcordance;

/**
 * Created by Guy on 01/10/2015.
 */
public class SingletonUser {
    private User user = new User();
    private static SingletonUser singletonUserInstance = null;

    public static SingletonUser getSingletonUserInstance() {
        if (singletonUserInstance == null) {
            singletonUserInstance = new SingletonUser();
        }
        return singletonUserInstance;
    }

    public void setUser(User user) {
        synchronized (user) {
            this.user = user;
        }
    }

    public User getUser() {
        return user;
    }

}

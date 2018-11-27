package com.sparingan;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Referensi
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User {

    public String username;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username) {
        this.username = username;
    }
}
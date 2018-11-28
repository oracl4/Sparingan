package com.sparingan;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Referensi
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User {

    public String username,email;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}


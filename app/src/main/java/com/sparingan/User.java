package com.sparingan;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Referensi
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User {

    public String username,email,phone,linkwa,imageurl;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String username, String email, String phone,String linkwa) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.linkwa = linkwa;
    }

    public User(String imageurl){
        this.imageurl = imageurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}


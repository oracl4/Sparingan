package com.sparingan;

public class Partner {
    public String userP,sportP,locationP,dateP,linkwaP,phoneP,imageP;

    public Partner(){

    }


    public Partner(String userP, String sportP, String locationP, String dateP, String linkwaP, String phoneP, String imageP) {
        this.userP = userP;
        this.sportP = sportP;
        this.locationP = locationP;
        this.dateP = dateP;
        this.linkwaP = linkwaP;
        this.phoneP = phoneP;
        this.imageP = imageP;
    }
    public String getUserP() {
        return userP;
    }

    public void setUserP(String userP) {
        this.userP = userP;
    }

    public String getSportP() {
        return sportP;
    }

    public void setSportP(String sportP) {
        this.sportP = sportP;
    }

    public String getLocationP() {
        return locationP;
    }

    public void setLocationP(String locationP) {
        this.locationP = locationP;
    }

    public String getDateP() {
        return dateP;
    }

    public void setDateP(String dateP) {
        this.dateP = dateP;
    }

    public String getLinkwaP() {
        return linkwaP;
    }

    public void setLinkwaP(String linkwaP) {
        this.linkwaP = linkwaP;
    }

    public String getPhoneP() {
        return phoneP;
    }

    public void setPhoneP(String phoneP) {
        this.phoneP = phoneP;
    }



}

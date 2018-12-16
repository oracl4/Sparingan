package com.sparingan;

public class Schedule {
    public String sport,location,date;

    public Schedule(){

    }

    public Schedule(String sport, String location, String date) {
        this.sport = sport;
        this.location = location;
        this.date = date;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


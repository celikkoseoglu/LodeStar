package com.lodestarapp.cs491.lodestar.Adapters;

public class ADDITIONAL_USER {

    private String email;
    private String username;
    private String trips;
    private String posts;
    private String favorites;

    public ADDITIONAL_USER() {

    }


    public String getfavorites() {
        return favorites;
    }

    public void setfavorites(String favorites) {
        this.favorites = favorites;
    }


    public String getposts() {
        return posts;
    }

    public void setposts(String posts) {
         this.posts = posts;
    }

    public String getemail() {
        return email;
    }

    public String gettrips() {
        return trips;
    }

    public void settrips(String trips) {
        this.trips = trips;
    }

    public String getusername() {
        return username;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public void setusername(String username) {
        this.username = username;
    }

}

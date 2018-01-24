package com.lodestarapp.cs491.lodestar.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HÜSEYİN on 24.1.2018.
 */

public class FlightInfo implements Parcelable{
    private String dest;
    private String orig;

    private String dest_gate;
    private String orig_gate;

    private String orig_airport;
    private String dest_airport;

    private String orig_date;
    private String dest_date;

    private String orig_localtime;
    private String dest_localtime;

    private int distance;
    private int speed;

    private String aircraft;
    private String link;


    public FlightInfo() {

    }

    public FlightInfo(Parcel in) {
        dest = in.readString();
        orig = in.readString();
        dest_gate = in.readString();
        orig_gate = in.readString();
        orig_airport = in.readString();
        dest_airport = in.readString();
        orig_date = in.readString();
        dest_date = in.readString();
        orig_localtime = in.readString();
        dest_localtime = in.readString();
        distance = in.readInt();
        speed = in.readInt();
        aircraft = in.readString();
        link = in.readString();
    }

    public static final Creator<FlightInfo> CREATOR = new Creator<FlightInfo>() {
        @Override
        public FlightInfo createFromParcel(Parcel in) {
            return new FlightInfo(in);
        }

        @Override
        public FlightInfo[] newArray(int size) {
            return new FlightInfo[size];
        }
    };


    public String getDest(){
        return dest;
    }

    public String getOrig(){
        return orig;
    }

    public void setDest(String newDest){
        dest = newDest;
    }

    public void setOrig(String newOrig){
        orig = newOrig;
    }

    public String getDest_gate(){
        return dest_gate;
    }
    public String getOrig_gate(){
        return orig_gate;
    }
    public void setDest_gate(String newD){
        dest_gate = newD;
    }
    public void setOrig_gate(String newO){
        orig_gate = newO;
    }

    public String getOrig_airport(){
        return orig_airport;
    }

    public String getDest_airport(){
        return dest_airport;
    }

    public void setOrig_airport(String newO){
        orig_airport = newO;
    }

    public void setDest_airport(String newD){
        dest_airport = newD;
    }

    public String getOrig_localtime(){
        return orig_localtime;
    }

    public String getDest_localtime(){
        return dest_localtime;
    }

    public void setOrig_localtime(String newO){
        orig_localtime = newO;
    }

    public void setDest_localtime(String newO){
        dest_localtime = newO;
    }

    public int getDistance(){
        return distance;
    }

    public int getSpeed(){
        return speed;
    }

    public void setDistance(int d){
        distance = d;
    }

    public void setSpeed(int s){
        speed = s;
    }

    public String getOrig_date(){
        return orig_date;
    }

    public String getDest_date(){
        return dest_date;
    }

    public void setOrig_date(String newO){
        orig_date = newO;
    }

    public void setDest_date(String newD){
        dest_date = newD;
    }

    public String getAircraft(){
        return aircraft;
    }

    public void setAircraft(String a){
        aircraft = a;
    }

    public void setLink(String l){
        link = l;
    }

    public String getLink(){
        return link;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dest);
        parcel.writeString(orig);
        parcel.writeString(dest_gate);
        parcel.writeString(orig_gate);
        parcel.writeString(orig_airport);
        parcel.writeString(dest_airport);
        parcel.writeString(orig_date);
        parcel.writeString(dest_date);
        parcel.writeString(orig_localtime);
        parcel.writeString(dest_localtime);
        parcel.writeInt(distance);
        parcel.writeInt(speed);
        parcel.writeString(aircraft);
        parcel.writeString(link);
    }
}

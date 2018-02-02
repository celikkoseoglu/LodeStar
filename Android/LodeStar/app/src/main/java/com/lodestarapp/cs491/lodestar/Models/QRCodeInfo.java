package com.lodestarapp.cs491.lodestar.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class QRCodeInfo implements Parcelable {

    private String from;
    private String to;
    private String flightCode;

    public QRCodeInfo() {

    }

    protected QRCodeInfo(Parcel in) {

        from = in.readString();
        to = in.readString();
        flightCode = in.readString();
    }

    public static final Creator<QRCodeInfo> CREATOR = new Creator<QRCodeInfo>() {
        @Override
        public QRCodeInfo createFromParcel(Parcel in) {
            return new QRCodeInfo(in);
        }

        @Override
        public QRCodeInfo[] newArray(int size) {
            return new QRCodeInfo[size];
        }
    };

    public void setFlightCode(String flightCode){
        this.flightCode = flightCode;
    }

    public void setTo(String to){
        this.to = to;
    }

    public void setFrom(String from){
        this.from = from;
    }


    public String getFlightCode() {
        return flightCode;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeString(flightCode);
    }
}

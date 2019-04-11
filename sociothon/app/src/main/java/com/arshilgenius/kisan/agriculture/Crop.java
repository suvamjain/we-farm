package com.arshilgenius.kisan.agriculture;

import android.os.Parcel;
import android.os.Parcelable;

public class Crop implements Parcelable {

    private String name, soil, temp, mnth, zone, detail;

    public Crop() {}

    public Crop(String name, String soil, String temp, String mnth, String zone, String detail) {
        this.name = name;
        this.soil = soil;
        this.temp = temp;
        this.mnth = mnth;
        this.detail = detail;
        this.zone = zone;
    }

    protected Crop(Parcel in) {
        name = in.readString();
        soil = in.readString();
        temp = in.readString();
        mnth = in.readString();
        zone = in.readString();
        detail = in.readString();
    }

    public static final Creator<Crop> CREATOR = new Creator<Crop>() {
        @Override
        public Crop createFromParcel(Parcel in) {
            return new Crop(in);
        }

        @Override
        public Crop[] newArray(int size) {
            return new Crop[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMnth() {
        return mnth;
    }

    public void setMnth(String mnth) {
        this.mnth = mnth;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getZone() { return zone; }

    public void setZone(String zone) { this.zone = zone; }

    @Override
    public String toString() {
        return "Crop{" +
                "name='" + name + '\'' +
                ", soil='" + soil + '\'' +
                ", temp='" + temp + '\'' +
                ", mnth='" + mnth + '\'' +
                ", zone='" + zone + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(soil);
        parcel.writeString(temp);
        parcel.writeString(mnth);
        parcel.writeString(zone);
        parcel.writeString(detail);
    }
}

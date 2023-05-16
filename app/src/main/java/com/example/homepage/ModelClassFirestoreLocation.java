package com.example.homepage;


public class ModelClassFirestoreLocation {

    String ChildID,ParentID;
    double Longitude,Latitude,Altitude,DateTime;
    float Speed;

    public ModelClassFirestoreLocation(){

    }

    public ModelClassFirestoreLocation(String childID, String parentID, double longitude, double latitude, double altitude,float speed, double dateTime) {
        ChildID = childID;
        ParentID = parentID;
        Longitude = longitude;
        Latitude = latitude;
        Altitude = altitude;
        DateTime = dateTime;
        Speed=speed;
    }

    public String getChildID() {
        return ChildID;
    }

    public void setChildID(String childID) {
        ChildID = childID;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getAltitude() {
        return Altitude;
    }

    public void setAltitude(double altitude) {
        Altitude = altitude;
    }

    public double getDateTime() {
        return DateTime;
    }

    public void setDateTime(double dateTime) {
        DateTime = dateTime;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }
}


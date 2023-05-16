package com.example.homepage;

//FOR CALL LOG
public class ModelClassFirestore {
    long timestamp;
    String childid;
    String parentid;
    String databaseid;
    String name;
    String number;
    String type;
    String date;
    String duration;
    String callid;

    public ModelClassFirestore() {

    }

    public ModelClassFirestore(long timestamp, String childid, String parentid, String databaseid, String name, String number, String type, String date, String duration,String callid) {
        this.timestamp = timestamp;
        this.childid = childid;
        this.parentid = parentid;
        this.databaseid = databaseid;
        this.name = name;
        this.number = number;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.callid=callid;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChildid() {
        return childid;
    }

    public void setChildid(String childid) {
        this.childid = childid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getDatabaseid() {
        return databaseid;
    }

    public void setDatabaseid(String databaseid) {
        this.databaseid = databaseid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

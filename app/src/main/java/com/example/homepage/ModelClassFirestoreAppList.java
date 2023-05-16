package com.example.homepage;

import java.util.List;
import java.util.Map;

public class ModelClassFirestoreAppList {
    String ChildID,ParentID;
    String currentDate;
    List<String> installedApps;

    public ModelClassFirestoreAppList(){

    }

    public ModelClassFirestoreAppList(String childID, String parentID, String currentDate, List<String> installedApps) {
        ChildID = childID;
        ParentID = parentID;
        this.currentDate = currentDate;
        this.installedApps = installedApps;
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

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public List<String> getInstalledApps() {
        return installedApps;
    }

    public void setInstalledApps(List<String> installedApps) {
        this.installedApps = installedApps;
    }
}

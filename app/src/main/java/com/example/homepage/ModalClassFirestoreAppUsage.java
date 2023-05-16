package com.example.homepage;

import java.util.Map;

public class ModalClassFirestoreAppUsage {
    String ChildID,ParentID;
    Map<String, Long> finalResult;
    String currentDate;

    public ModalClassFirestoreAppUsage(){

    }

    public ModalClassFirestoreAppUsage(String childID, String parentID, Map<String, Long> FinalResult, String currentDate) {
        ChildID = childID;
        ParentID = parentID;
        finalResult = FinalResult;
        this.currentDate = currentDate;
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

    public Map<String, Long> getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Map<String, Long> FinalResult) {
        finalResult = FinalResult;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}


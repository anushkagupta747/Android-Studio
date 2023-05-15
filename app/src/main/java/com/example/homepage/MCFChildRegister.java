package com.example.homepage;

public class MCFChildRegister {
    String parent_token;
    String child_name;
    String parent_id;
    String child_age;
    String child_gender;
    String child_password;

    public MCFChildRegister(){

    }

    public MCFChildRegister(String parent_token, String child_name, String parent_id, String child_age, String child_gender, String child_password) {
        this.parent_token = parent_token;
        this.child_name = child_name;
        this.parent_id = parent_id;
        this.child_age = child_age;
        this.child_gender = child_gender;
        this.child_password = child_password;
    }

    public String getParent_token() {
        return parent_token;
    }

    public void setParent_token(String parent_token) {
        this.parent_token = parent_token;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getChild_age() {
        return child_age;
    }

    public void setChild_age(String child_age) {
        this.child_age = child_age;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }

    public String getChild_password() {
        return child_password;
    }

    public void setChild_password(String child_password) {
        this.child_password = child_password;
    }
}

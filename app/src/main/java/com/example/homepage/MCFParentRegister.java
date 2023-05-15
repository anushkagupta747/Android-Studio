package com.example.homepage;

public class MCFParentRegister {

    String parent_name,email,phone,password;

    public MCFParentRegister(){

    }

    public MCFParentRegister(String parent_name, String email, String phone, String password) {
        this.parent_name = parent_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

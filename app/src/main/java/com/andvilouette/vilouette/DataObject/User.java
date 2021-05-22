package com.andvilouette.vilouette.DataObject;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("customerID") private String id;

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    @SerializedName("password") private String password;
    @SerializedName("confirm_password") private String confirmpassword;

    @SerializedName("email") private String emailAddress;
    @SerializedName("name") private String username;
    @SerializedName("phone") private String bphn;
    @SerializedName("category") private String bcategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBphn() {
        return bphn;
    }

    public void setBphn(String bphn) {
        this.bphn = bphn;
    }

    public String getBcategory() {
        return bcategory;
    }

    public void setBcategory(String bcategory) {
        this.bcategory = bcategory;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("Message") private String message;

    public String getUser_phn() {
        return user_phn;
    }

    public void setUser_phn(String user_phn) {
        this.user_phn = user_phn;
    }

    @SerializedName("user_phone") private String user_phn;

}

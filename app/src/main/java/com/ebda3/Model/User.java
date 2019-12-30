package com.ebda3.Model;

/**
 * Created by work on 11/10/2017.
 */

public class User {
    public String ID;
    public String Name;
    public String Phone;
    public String Address;
    public String Password;
    public String Email;
    public String Image;

    public User() {
        ID = "";
        Name = "";
        Phone = "";
        Address = "";
        Password = "";
        Email = "";
        Image = "";
    }

    public User(String ID, String name, String phone, String address, String password, String email, String image) {
        this.ID = ID;
        this.Name = name;
        this.Phone = phone;
        this.Address = address;
        this.Password = password;
        this.Email = email;
        this.Image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}

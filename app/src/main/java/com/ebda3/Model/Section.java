package com.ebda3.Model;

/**
 * Created by work on 19/10/2017.
 */

public class Section {
    String name;
    String ID;

    public Section() {
        this.name = "";
        this.ID = "";
    }

    public Section(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

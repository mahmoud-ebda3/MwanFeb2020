package com.ebda3.Model;

import android.widget.EditText;

import java.util.ArrayList;

public class KitchenView {

    public String id ;
    public String name_room;
    public EditText length_txt ;
    public EditText width_txt;
    public EditText height_txt;

    public KitchenView() {
    }

    public KitchenView(String id, String name_room, EditText length_txt, EditText width_txt, EditText height_txt) {
        this.id = id;
        this.name_room = name_room;
        this.length_txt = length_txt;
        this.width_txt = width_txt;
        this.height_txt = height_txt;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_room() {
        return name_room;
    }

    public void setName_room(String name_room) {
        this.name_room = name_room;
    }

    public EditText getLength_txt() {
        return length_txt;
    }

    public void setLength_txt(EditText length_txt) {
        this.length_txt = length_txt;
    }

    public EditText getWidth_txt() {
        return width_txt;
    }

    public void setWidth_txt(EditText width_txt) {
        this.width_txt = width_txt;
    }

    public EditText getHeight_txt() {
        return height_txt;
    }

    public void setHeight_txt(EditText height_txt) {
        this.height_txt = height_txt;
    }
}

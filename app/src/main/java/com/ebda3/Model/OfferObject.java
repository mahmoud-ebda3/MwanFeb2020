package com.ebda3.Model;

public class OfferObject {
    private String name;
    private String photo;

    public OfferObject( ) {

    }

    public OfferObject(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

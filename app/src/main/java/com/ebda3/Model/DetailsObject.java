package com.ebda3.Model;

public class DetailsObject {
    private String ID;
    private String sectionName;
    private String detailsResponse;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public DetailsObject(String ID, String sectionName, String detailsResponse) {
        this.ID = ID;
        this.sectionName = sectionName;
        this.detailsResponse = detailsResponse;
    }

    public String getSectionName() {
        return sectionName;
    }


    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDetailsResponse() {
        return detailsResponse;
    }

    public void setDetailsResponse(String detailsResponse) {
        this.detailsResponse = detailsResponse;
    }
}

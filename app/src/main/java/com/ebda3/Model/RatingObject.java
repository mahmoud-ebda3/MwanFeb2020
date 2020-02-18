package com.ebda3.Model;

public class RatingObject {

    private String rateNumber;
    private String rateDate;
    private String rateNote;

    public RatingObject(String rateNumber, String rateDate, String rateNote) {
        this.rateNumber = rateNumber;
        this.rateDate = rateDate;
        this.rateNote = rateNote;
    }

    public String getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(String rateNumber) {
        this.rateNumber = rateNumber;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getRateNote() {
        return rateNote;
    }

    public void setRateNote(String rateNote) {
        this.rateNote = rateNote;
    }
}

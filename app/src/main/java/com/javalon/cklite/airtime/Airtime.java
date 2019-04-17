package com.javalon.cklite.airtime;

public class Airtime {

    private String mobileNumber;
    private String mobileNetwork;
    private String airtimeAmount;

    // default constructor
    public Airtime() { }

    public Airtime(String mobileNumber, String mobileNetwork, String airtimeAmount) {
        this.mobileNumber = mobileNumber;
        this.mobileNetwork = mobileNetwork;
        this.airtimeAmount = airtimeAmount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNetwork() {
        return mobileNetwork;
    }

    public void setMobileNetwork(String mobileNetwork) {
        this.mobileNetwork = mobileNetwork;
    }

    public String getAirtimeAmount() {
        return airtimeAmount;
    }

    public void setAirtimeAmount(String airtimeAmount) {
        this.airtimeAmount = airtimeAmount;
    }
}
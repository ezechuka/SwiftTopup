package com.javalon.cklite.dataBundle;

public class DataBundle {

    private String mobileNumber;
    private String mobileNetwork;
    private String dataPlan;

    public DataBundle() {
    }

    public DataBundle(String mobileNumber, String mobileNetwork, String dataPlan) {
        this.mobileNumber = mobileNumber;
        this.mobileNetwork = mobileNetwork;
        this.dataPlan = dataPlan;
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

    public String getDataPlan() {
        return dataPlan;
    }

    public void setDataPlan(String dataPlan) {
        this.dataPlan = dataPlan;
    }
}

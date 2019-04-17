package com.javalon.cklite.cableTV;

public class CableTVSub {
    private String cableTV;
    private String packageType;
    private String smartCardNo;

    public CableTVSub(String cableTV, String packageType, String smartCardNo) {
        this.cableTV = cableTV;
        this.packageType = packageType;
        this.smartCardNo = smartCardNo;
    }

    public String getCableTV() {
        return cableTV;
    }

    public void setCableTV(String cableTV) {
        this.cableTV = cableTV;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getSmartCardNo() {
        return smartCardNo;
    }

    public void setSmartCardNo(String smartCardNo) {
        this.smartCardNo = smartCardNo;
    }
}
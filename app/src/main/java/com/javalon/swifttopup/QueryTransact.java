package com.javalon.swifttopup;

public class QueryTransact {

    private String date;
    private String networkIcon;
    private String time;
    private String orderid;
    private String statuscode;
    private String status;
    private String remark;
    private String mobilenetwork;
    private String mobilenumber;
    private String ordertype;
    private String amountcharged;
    private String walletbalance;
    private String transactionReference;

    public String getDate() {
        return date;
    }

    public String getNetworkIcon() {
        return networkIcon;
    }

    public void setNetworkIcon(String networkIcon) {
        this.networkIcon = networkIcon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public String getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public String getMobilenetwork() {
        return mobilenetwork;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public String getAmountcharged() {
        return amountcharged;
    }

    public String getWalletbalance() {
        return walletbalance;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
}
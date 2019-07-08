package com.javalon.swifttopup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyPhoneNumber {

    @SerializedName("valid")
    @Expose
    private boolean valid;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("local_format")
    @Expose
    private String localFormat;
    @SerializedName("international_format")
    @Expose
    private String internationalFormat;
    @SerializedName("country_prefix")
    @Expose
    private String countryPrefix;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("carrier")
    @Expose
    private String carrier;
    @SerializedName("line_type")
    @Expose
    private String lineType;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocalFormat() {
        return localFormat;
    }

    public void setLocalFormat(String localFormat) {
        this.localFormat = localFormat;
    }

    public String getInternationalFormat() {
        return internationalFormat;
    }

    public void setInternationalFormat(String internationalFormat) {
        this.internationalFormat = internationalFormat;
    }

    public String getCountryPrefix() {
        return countryPrefix;
    }

    public void setCountryPrefix(String countryPrefix) {
        this.countryPrefix = countryPrefix;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
}
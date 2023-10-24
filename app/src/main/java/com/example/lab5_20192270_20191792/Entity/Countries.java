package com.example.lab5_20192270_20191792.Entity;

public class Countries {
    private String countryId;
    private String countryName;
    private Regions regionsId;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Regions getRegionsId() {
        return regionsId;
    }

    public void setRegionsId(Regions regionsId) {
        this.regionsId = regionsId;
    }
}

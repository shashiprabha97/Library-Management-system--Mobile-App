package com.example.indika.myapplication.http_handlers;

import java.util.Date;

public class UserRegester {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String regDate;
    private String userType;
    private String idNumber;
    private String district;
    private String countryCode;
    private String passportId;

    public UserRegester(String username, String password, String firstName, String lastName, String regDate, String userType, String idNumber, String district,
                        String countryCode,String passportId) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regDate = regDate;
        this.userType = userType;
        this.idNumber = idNumber;
        this.district = district;
        this.countryCode = countryCode;
        this.passportId = passportId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getUserType() {
        return userType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getDistrict() {
        return district;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPassportId() {
        return passportId;
    }
}
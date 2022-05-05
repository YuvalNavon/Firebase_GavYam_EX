
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 *  The class for the Company database.
 *  Used to set the fields of the table.
 */



package com.example.firebase_gavyam_ex;

public class Company {

    public String companyName;
    public String taxCompany;
    public String mainPhoneNumber;
    public String secondaryPhoneNumber;
    public int companyActive;


    public Company(){
        this.companyName = "ERROR";
        this.taxCompany = "ERROR";
        this.mainPhoneNumber = "ERROR";
        this.secondaryPhoneNumber = "ERROR";
        this.companyActive = 0;

    }

    public Company(String companyName, String taxCompany, String mainPhoneNumber, String secondaryPhoneNumber) {
        this.companyName = companyName;
        this.taxCompany = taxCompany;
        this.mainPhoneNumber = mainPhoneNumber;
        this.secondaryPhoneNumber = secondaryPhoneNumber;
        this.companyActive = 1;
    }

    public Company(String companyName, String taxCompany, String mainPhoneNumber, String secondaryPhoneNumber, int companyActive) {
        this.companyName = companyName;
        this.taxCompany = taxCompany;
        this.mainPhoneNumber = mainPhoneNumber;
        this.secondaryPhoneNumber = secondaryPhoneNumber;
        this.companyActive = companyActive;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxCompany() {
        return taxCompany;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public int getCompanyActive() {
        return companyActive;
    }
}

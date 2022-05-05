
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 *  The class for the Hazmana database.
 *  Used to set the fields of the table.
 */



package com.example.firebase_gavyam_ex;

public class Hazmana {


    public String workerID;
    public String workerLastName;
    public String workerFirstName;
    public String companyName;
    public Meal meal;
    public String date;
    public String hour;



    public Hazmana(){
        this.workerID = "ERROR";
        this.workerLastName = "ERROR";
        this.workerFirstName = "ERROR";
        this.companyName = "ERROR";
        this.meal = null;
        this.date = "ERROR";
        this.hour = "ERROR";

    }

    public Hazmana(String workerID, String workerLastName, String workerFirstName, String companyName, Meal meal, String date, String hour) {
        this.workerID = workerID;
        this.workerLastName = workerLastName;
        this.workerFirstName = workerFirstName;
        this.companyName = companyName;
        this.meal = meal;
        this.date = date;
        this.hour = hour;
    }

    public String getWorkerID() {
        return workerID;
    }

    public String getWorkerLastName() {
        return workerLastName;
    }

    public String getWorkerFirstName() {
        return workerFirstName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Meal getMeal() {
        return meal;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }
}

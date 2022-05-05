
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 *  The class for the Worker database.
 *  Used to set the Worker tree.
 */


package com.example.firebase_gavyam_ex;

public class Worker {
    public String firstName;
    public String lastName;
    public String workCompany;
    public String idNumber;
    public String phoneNumber;
    public int workerActive;


    public Worker(){
        this.firstName = "ERROR";
        this.lastName = "ERROR";
        this.workCompany = "ERROR";
        this.idNumber = "ERROR";
        this.phoneNumber = "ERROR";
        this.workerActive = 0;

    }

    public Worker(String firstName, String lastName, String workCompany, String idNumber, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.workCompany = workCompany;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.workerActive = 1;

    }

    public Worker(String firstName, String lastName, String workCompany, String idNumber, String phoneNumber, int active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.workCompany = workCompany;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.workerActive = active;

    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getWorkCompany() {
        return workCompany;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getWorkerActive() {
        return workerActive;
    }
}

/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 *  The class for the Meal database.
 *  Used to set the fields of the table.
 */



package com.example.firebase_gavyam_ex;

public class Meal {

    public String appetizer;
    public String mainMeal;
    public String extra;
    public String dessert;
    public String drink;


    public Meal() {
        this.appetizer = "ERROR";
        this.mainMeal = "ERROR";
        this.extra = "ERROR";
        this.dessert = "ERROR";
        this.drink = "ERROR";
    }

    public Meal(String appetizer, String mainMeal, String extra, String dessert, String drink) {
        this.appetizer = appetizer;
        this.mainMeal = mainMeal;
        this.extra = extra;
        this.dessert = dessert;
        this.drink = drink;
    }

    public String getAppetizer() {
        return appetizer;
    }

    public String getMainMeal() {
        return mainMeal;
    }

    public String getExtra() {
        return extra;
    }

    public String getDessert() {
        return dessert;
    }

    public String getDrink() {
        return drink;
    }
}

package com.example.database_gevyam_ex_3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.example.database_gevyam_ex_3.Worker.*;
import static com.example.database_gevyam_ex_3.Company.*;
import static com.example.database_gevyam_ex_3.Meal.*;
import static com.example.database_gevyam_ex_3.Hazmana.*;


public class HelperDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GevYam.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        strCreate="CREATE TABLE "+TABLE_WORKER;
        strCreate+=" ("+KEY_NUMBER+" INTEGER PRIMARY KEY,";
        strCreate+=" "+FIRST_NAME+" TEXT,";
        strCreate+=" "+LAST_NAME+" TEXT,";
        strCreate+=" "+WORK_COMPANY+" TEXT,";
        strCreate+=" "+ID_NUMBER+" TEXT,";
        strCreate+=" "+PHONE_NUMBER+" TEXT,";
        strCreate+=" "+ WORKER_ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_COMPANY;
        strCreate+=" ("+COMPANY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+NAME+" TEXT,";
        strCreate+=" "+TAX_COMPANY+" TEXT,";
        strCreate+=" "+MAIN_NUMBER+" TEXT,";
        strCreate+=" "+SECONDARY_NUMBER+" TEXT,";
        strCreate+=" "+ COMPANY_ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_MEAL;
        strCreate+=" ("+MEAL_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+APPETIZER+" TEXT,";
        strCreate+=" "+MAIN_MEAL+" TEXT,";
        strCreate+=" "+EXTRA+" TEXT,";
        strCreate+=" "+DESSERT+" TEXT,";
        strCreate+=" "+DRINK+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_HAZMANA;
        strCreate+=" ("+ORDER_NUMBER+" INTEGER PRIMARY KEY,";
        strCreate+=" "+DATE+" TEXT,";
        strCreate+=" "+HOUR+" TEXT,";
        strCreate+=" "+WORKER+" TEXT,";
        strCreate+=" "+MEAL+" TEXT,";
        strCreate+=" "+COMPANY+" TEXT";
        strCreate+= ");";
        db.execSQL(strCreate);





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        strDelete="DROP TABLE IF EXISTS "+TABLE_WORKER;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+TABLE_COMPANY;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+TABLE_MEAL;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+TABLE_HAZMANA;
        db.execSQL(strDelete);





        onCreate(db);
    }
}




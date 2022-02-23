package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MakeOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner companyChoose;
    int spinnerselect;
    EditText cardID, appetizer, mainCourse, extra, dessert, drink;
    String cardID_st, companyID_st, appetizer_st, mainCourse_st, extra_st , dessert_st , drink_st, workerActive_st, workerName_st, companyName_st;
    ArrayList<String> tblCompanyNames, tblkeyCompanies, companies, tblWorkerActive, tblWorkerNames;

    SQLiteDatabase db;
    ContentValues cv;
    HelperDB hlp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);

        hlp = new HelperDB(this);


        cardID = (EditText) findViewById(R.id.cardID);
        appetizer = (EditText) findViewById(R.id.appetizer);
        mainCourse = (EditText) findViewById(R.id.mainCourse);
        extra = (EditText) findViewById(R.id.extra);
        dessert = (EditText) findViewById(R.id.dessert);
        drink = (EditText) findViewById(R.id.drink);

        companies = allActiveCompanies();
        companyChoose = (Spinner) findViewById(R.id.companychoosespinner);
        companyChoose.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,companies);
        companyChoose.setAdapter(adp);

    }


    public void Add(View view)
    {
        cardID_st = cardID.getText().toString();
        appetizer_st = appetizer.getText().toString();
        mainCourse_st = mainCourse.getText().toString();
        extra_st = extra.getText().toString();
        dessert_st = dessert.getText().toString();
        drink_st = drink.getText().toString();

        allWorkersStatus();
        if (!cardID_st.isEmpty()){
            if (Integer.parseInt(cardID_st)>tblWorkerActive.size() || Integer.parseInt(cardID_st)<=0){
                Toast toast = Toast.makeText(getApplicationContext(), "NONEXISTENT worker", Toast.LENGTH_LONG);
                toast.show();
            }
            else if (tblWorkerActive.get(Integer.parseInt(cardID_st)-1).equals("0") ){
                Toast toast = Toast.makeText(getApplicationContext(), "Worker is INACTIVE", Toast.LENGTH_LONG);
                toast.show();
            }
            else if (!cardID_st.isEmpty() && !appetizer_st.isEmpty() && !mainCourse_st.isEmpty() && !extra_st.isEmpty() && !dessert_st.isEmpty() && !drink_st.isEmpty() )
            {
                cv = new ContentValues();

                cv.put(Meal.APPETIZER, appetizer_st);
                cv.put(Meal.MAIN_MEAL, mainCourse_st);
                cv.put(Meal.EXTRA, extra_st);
                cv.put(Meal.DESSERT, dessert_st);
                cv.put(Meal.DRINK, drink_st);
                db = hlp.getWritableDatabase();
                long meal_ID = db.insert(Meal.TABLE_MEAL, null, cv );
                db.close();


                Calendar calendar = Calendar.getInstance();
                int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                String hour = "" + Integer.toString(hour24hrs) + ":" + Integer.toString(minutes);
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


                cv = new ContentValues();

                cv.put(Hazmana.WORKER, cardID_st);
                cv.put(Hazmana.WORKER_NAME, tblWorkerNames.get(Integer.parseInt(cardID_st)-1));
                cv.put(Hazmana.COMPANY, companyID_st);
                cv.put(Hazmana.COMPANY_NAME, tblCompanyNames.get(spinnerselect));
                cv.put(Hazmana.MEAL, Long.toString(meal_ID) );
                cv.put(Hazmana.HOUR, hour);
                cv.put(Hazmana.DATE, currentDate );
                db = hlp.getWritableDatabase();
                db.insert(Hazmana.TABLE_HAZMANA, null, cv );
                db.close();

                cardID.setText("");
                appetizer.setText("");
                mainCourse.setText("");
                extra.setText("");
                dessert.setText("");
                drink.setText("");

                Toast toast = Toast.makeText(getApplicationContext(), "Order received!", Toast.LENGTH_LONG);
                toast.show();

            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields!", Toast.LENGTH_LONG);
            toast.show();
        }




    }

    public void clear(View view){
        companyChoose.setSelection(0);
        cardID.setText("");
        appetizer.setText("");
        mainCourse.setText("");
        extra.setText("");
        dessert.setText("");
        drink.setText("");
        cardID_st = "";
        companyID_st = "";
        appetizer_st = "";
        mainCourse_st = "";
        extra_st = "";
        dessert_st = "";
        drink_st = "";
    }

    public void ReaderCompany(Cursor cursr){
        db=hlp.getWritableDatabase();
        tblCompanyNames = new ArrayList<>();
        tblkeyCompanies = new ArrayList<>();
        int col_key = cursr.getColumnIndex(Company.COMPANY_ID);
        int col_name = cursr.getColumnIndex(Company.NAME);
        cursr.moveToFirst();
        while (!cursr.isAfterLast()){
            String key_st = cursr.getString(col_key);
            String name_st = cursr.getString(col_name);
            tblkeyCompanies.add(key_st);
            tblCompanyNames.add(name_st);
            cursr.moveToNext();
        }
        cursr.close();
        db.close();
    }

    public ArrayList<String> allActiveCompanies(){
        db=hlp.getReadableDatabase();
        Cursor crsr = db.query(Company.TABLE_COMPANY, null, Company.COMPANY_ACTIVE+ "=?", new String[]{"1"}, null,null, null, null);
        ReaderCompany(crsr);
        crsr.close();
        db.close();
        return tblCompanyNames;

    }

    public void ReaderWorker(Cursor cursr){
        db=hlp.getWritableDatabase();
        tblWorkerActive = new ArrayList<>();
        tblWorkerNames = new ArrayList<>();
        int col_active = cursr.getColumnIndex(Worker.WORKER_ACTIVE);
        int col_name = cursr.getColumnIndex(Worker.LAST_NAME);
        cursr.moveToFirst();
        while (!cursr.isAfterLast()){
            workerActive_st = cursr.getString(col_active);
            tblWorkerActive.add(workerActive_st);
            workerName_st = cursr.getString(col_name);
            tblWorkerNames.add(workerName_st);
            cursr.moveToNext();
        }
        cursr.close();
        db.close();
    }

    public void allWorkersStatus(){
        db=hlp.getReadableDatabase();
        Cursor crsr = db.query(Worker.TABLE_WORKER, null, null, null, null,null, null, null);
        ReaderWorker(crsr);
        crsr.close();
        db.close();


    }

    public void goBack(View view){
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        companyID_st = tblkeyCompanies.get(i);
        System.out.println(companyID_st);
        spinnerselect = i;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    String workerlastname_st, workerID_st, company_st, companyID_st, date_st, hour_st, mealID_st, appetizer_st, mainCourse_st, extra_st, dessert_st, drink_st;
    int key;
    boolean revOrder;

    Spinner spin;
    String [] sorts;

    TextView order, worker, hour, appetizer, maincourse, extra, dessert, drink;

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;

    ListView listofOrders;
    ArrayList<String> tbl, tblrev;
    ArrayList<String[]> tblfull, tblMeals;
    ArrayAdapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);



        revOrder = false;

        hlp = new HelperDB(this);



        listofOrders = (ListView) findViewById(R.id.listOfOrders);
        listofOrders.setOnItemClickListener(this);
        listofOrders.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        sorts = new String[]{"First added","Last added","Worker: A-Z", "Worker: Z-A", "Company: A-Z","Company: Z-A"};
        spin = (Spinner) findViewById(R.id.sortSpinnerOrder);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,sorts);
        spin.setAdapter(adp);

        order = findViewById(R.id.OrderNumber);
        worker = findViewById(R.id.WorkerIDOrder);
        hour = findViewById(R.id.HourOrder);
        appetizer = findViewById(R.id.AppetizerOrder);
        maincourse = findViewById(R.id.MainCourseOrder);
        extra = findViewById(R.id.ExtraOrder);
        dessert = findViewById(R.id.DessertOrder);
        drink = findViewById(R.id.DrinkOrder);

        order.setText("Order number - ");
        worker.setText("");
        hour.setText("");
        appetizer.setText("");
        maincourse.setText("");
        extra.setText("");
        dessert.setText("");
        drink.setText("");



    }

    @Override
    protected void onResume() {
        super.onResume();
        spin.setSelection(0);
        db=hlp.getReadableDatabase();
        crsr = db.query(Hazmana.TABLE_HAZMANA, null, null, null, null, null, null, null);
        Reader(crsr);
        crsr.close();
        db.close();

    }


    public String getWorkerName(String workerID){
        db=hlp.getWritableDatabase();
        Cursor cursr = db.query(Worker.TABLE_WORKER, null, null, null, null, null, null, null);
        int col_key = cursr.getColumnIndex(Worker.KEY_NUMBER);
        int col_last = cursr.getColumnIndex(Worker.LAST_NAME);
        String worker_name = "ERROR";
        cursr.moveToFirst();
        while (!cursr.isAfterLast()){
            if (workerID.equals(Integer.toString(cursr.getInt(col_key)))){
                worker_name = cursr.getString(col_last);
            }

            cursr.moveToNext();
        }
        cursr.close();
        db.close();
        return worker_name;
    }

    public String getCompanyName(String companyID){
        db=hlp.getWritableDatabase();
        Cursor cursr = db.query(Company.TABLE_COMPANY, null, null, null, null, null, null, null);
        int col_key = cursr.getColumnIndex(Company.COMPANY_ID);
        int col_name = cursr.getColumnIndex(Company.NAME);
        String company_name = "ERROR";
        cursr.moveToFirst();
        while (!cursr.isAfterLast()){
            if (companyID.equals(Integer.toString(cursr.getInt(col_key)))){
                company_name = cursr.getString(col_name);
            }

            cursr.moveToNext();
        }
        cursr.close();
        db.close();
        return company_name;
    }
    public void ReaderMeal(Cursor cursr){
        db=hlp.getWritableDatabase();
        int col_appetizer = cursr.getColumnIndex(Meal.APPETIZER);
        int col_mainCourse = cursr.getColumnIndex(Meal.MAIN_MEAL);
        int col_extra = cursr.getColumnIndex(Meal.EXTRA);
        int col_dessert = cursr.getColumnIndex(Meal.DESSERT);
        int col_drink = cursr.getColumnIndex(Meal.DRINK);
        cursr.moveToFirst();
        appetizer_st = cursr.getString(col_appetizer);
        mainCourse_st = cursr.getString(col_mainCourse);
        extra_st = cursr.getString(col_extra);
        dessert_st = cursr.getString(col_dessert);
        drink_st = cursr.getString(col_drink);
        tblMeals.add(new String[]{appetizer_st, mainCourse_st, extra_st, dessert_st, drink_st});
    }

    public void addToMeals(String mealID){
        db=hlp.getReadableDatabase();
        Cursor cursr = db.query(Meal.TABLE_MEAL, null, Meal.MEAL_ID +"=?", new String[]{mealID}, null, null, null, null);
        ReaderMeal(cursr);
    }

    public ArrayList<String> reverse_tbl(){
        tblrev = new ArrayList<>();
        for (int i = tbl.size()-1; i>=0; i--){
            tblrev.add(tbl.get(i));
        }
        return tblrev;
    }

    public void Reader(Cursor cursr){
        db=hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        tblfull = new ArrayList<>();
        tblMeals = new ArrayList<>();
        int col_key = cursr.getColumnIndex(Hazmana.ORDER_NUMBER);
        int col_workerID = cursr.getColumnIndex(Hazmana.WORKER);
        int col_companyID = cursr.getColumnIndex(Hazmana.COMPANY);
        int col_mealID = cursr.getColumnIndex(Hazmana.MEAL);
        int col_date = cursr.getColumnIndex(Hazmana.DATE);
        int col_hour = cursr.getColumnIndex(Hazmana.HOUR);
        cursr.moveToFirst();
        while (!cursr.isAfterLast()){
            key = cursr.getInt(col_key);
            workerID_st = cursr.getString(col_workerID);
            companyID_st = cursr.getString(col_companyID);
            mealID_st = cursr.getString(col_mealID);
            addToMeals(mealID_st);
            date_st = cursr.getString(col_date);
            hour_st = cursr.getString(col_hour);
            workerlastname_st = getWorkerName(workerID_st);
            company_st = getCompanyName(companyID_st);
            String tmp = "" + key + ": " + workerlastname_st + ", " + company_st + ", " + date_st;
            System.out.println(tmp);
            String[] tmp2 = new String[]{String.valueOf(key), workerID_st, companyID_st, mealID_st, date_st, hour_st} ;
            tbl.add(tmp);
            System.out.println("TBL:" +  tbl);
            tblfull.add(tmp2);
            cursr.moveToNext();
        }
        cursr.close();
        db.close();
        if (revOrder) tbl = reverse_tbl();
        adp = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
        listofOrders.setAdapter(adp);
        revOrder = false;



    }







    public void goBack(View view){
        finish();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        order.setText("Order number - " + tblfull.get(i)[0]);
        hour.setText(tblfull.get(i)[5]);
        worker.setText(tblfull.get(i)[1]);
        appetizer.setText(tblMeals.get(i)[0]);
        maincourse.setText(tblMeals.get(i)[1]);
        extra.setText(tblMeals.get(i)[2]);
        dessert.setText(tblMeals.get(i)[3]);
        drink.setText(tblMeals.get(i)[4]);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String orderBy = null;

        if (i==0){
            revOrder = false;
        }

        else if (i==1){
            revOrder = true;


        }


        else if (i==2){
            orderBy = Hazmana.WORKER_NAME;


        }
        else if (i==3){
            orderBy = Hazmana.WORKER_NAME + " DESC";


        }

        else if (i==4){
            orderBy = Hazmana.COMPANY_NAME;

        }

        else if (i==5){
            orderBy = Hazmana.COMPANY_NAME + " DESC";


        }

        db=hlp.getReadableDatabase();
        crsr = db.query(Hazmana.TABLE_HAZMANA, null, null, null, null, null, orderBy, null);
        Reader(crsr);
        crsr.close();
        db.close();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
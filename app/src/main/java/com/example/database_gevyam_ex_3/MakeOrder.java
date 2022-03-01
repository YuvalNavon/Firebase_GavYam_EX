
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version 	1
 * @since		1/3/2022
 * This activity is used to make new orders and add them to the database.
 */

package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    /**
     * Saves the inputted information of the new order, verifies it and if it is valid, it adds the order to the database.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void Add(View view)
    {
        cardID_st = cardID.getText().toString();
        appetizer_st = appetizer.getText().toString();
        mainCourse_st = mainCourse.getText().toString();
        extra_st = extra.getText().toString();
        dessert_st = dessert.getText().toString();
        drink_st = drink.getText().toString();

        allActiveWorkers();
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
                if (minutes<10) hour = "" + Integer.toString(hour24hrs) + ":" + "0" + Integer.toString(minutes);

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


    /**
     * Clears the EditText fields.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
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

    /**
     * Closes the activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void goBack(View view){
        finish();
    }


    /**
     * Reads the Company table from the database.
     * <p>
     *
     * @param	cursr - the cursor that was made for the reading of the database
     * @return	None
     */
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


    /**
     * Makes a global ArrayList of all active companies from the Company table.
     * <p>
     *
     * @return	tblCompanyNames - ArrayList<String> of all active companies.
     */
    public ArrayList<String> allActiveCompanies(){
        db=hlp.getReadableDatabase();
        Cursor crsr = db.query(Company.TABLE_COMPANY, null, Company.COMPANY_ACTIVE+ "=?", new String[]{"1"}, null,null, null, null);
        ReaderCompany(crsr);
        crsr.close();
        db.close();
        return tblCompanyNames;

    }

    /**
     * Reads the Worker table from the database.
     * <p>
     *
     * @param	cursr - the cursor that was made for the reading of the database
     * @return	None
     */
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


    /**
     * makes a global ArrayList of all active workers from the Worker table.
     * <p>
     *
     * @return	None
     */
    public void allActiveWorkers(){
        db=hlp.getReadableDatabase();
        Cursor crsr = db.query(Worker.TABLE_WORKER, null, null, null, null,null, null, null);
        ReaderWorker(crsr);
        crsr.close();
        db.close();


    }

    /**
     * Saves the company that the user chose from the Spinner.
     * <p>
     *
     * @param	adapterView - the Spinner that was clicked
     *          view - the item that was clicked
     *          i - the position of the item that was clicked in the Adapter
     *          l - the row that was clicked in the Spinner
     * @return	None
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        companyID_st = tblkeyCompanies.get(i);
        spinnerselect = i;


    }


    /**
     * Mandatory method, isn't used
     * <p>
     *
     * @param	adapterView - the Spinner that was clicked
     * @return	None
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Creates the Options Menu, allowing the user to navigate to the Home screen, Credits screen
     * or the OrderHistory activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 0, "Order History");
        return true;
    }

    /**
     * Starts the MainActivity, CreditsScreen activity, or OrderHistory activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("Order History")){
            Intent si = new Intent(this, OrderHistory.class);
            startActivity(si);

        }
        if (st.equals("Home Screen")){
            Intent si = new Intent(this, MainActivity.class);
            startActivity(si);

        }

        if (st.equals("Credits Screen")){
            Intent si = new Intent(this, CreditsScreen.class);
            startActivity(si);

        }

        return true;
    }

}
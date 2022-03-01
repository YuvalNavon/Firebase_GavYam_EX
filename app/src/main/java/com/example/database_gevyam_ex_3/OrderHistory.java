
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		1/3/2022
 * This activity is used to show the full details of all orders.
 */


package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
     * Makes a global ArrayList that contains all of the orders in reverse order (last added come first).
     * <p>
     *
     * @return	tblrev - ArrayList<String> of all orders in reverse order.
     */
    public ArrayList<String> reverse_tbl(){
        tblrev = new ArrayList<>();
        for (int i = tbl.size()-1; i>=0; i--){
            tblrev.add(tbl.get(i));
        }
        return tblrev;
    }
    /**
     * gets the last name of the worker that is being checked during the reading of the
     * Order table from the database.
     * <p>
     *
     * @param	workerID -the KeyID of the worker that is being checked
     * @return	worker_name - the last name of the worker that was checked.
     */
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

    /**
     * gets the name of the company that is being checked during the reading of the
     * Order table from the database.
     * <p>
     *
     * @param	companyID -the KeyID of the company that is being checked
     * @return	company_name - the name of the company that was checked.
     */
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

    /**
     * Reads the Meal table from the database and saves each meal's full information in a global
     * ArrayList<String[]> called tblMeals.
     * <p>
     *
     * @param	cursr - the cursor that was made for the reading of the database
     * @return	None
     */
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

    /**
     * Initilizes the cursor and calls the ReaderMeal function.
     * <p>
     *
     * @param	mealID - the KeyID of the meal that is being added to tblMeals
     * @return	None
     */
    public void addToMeals(String mealID){
        db=hlp.getReadableDatabase();
        Cursor cursr = db.query(Meal.TABLE_MEAL, null, Meal.MEAL_ID +"=?", new String[]{mealID}, null, null, null, null);
        ReaderMeal(cursr);
    }


    /**
     * Reads the Order table from the database and saves each order's full information in a global
     * ArrayList<String[]> called tblfull. the information that is presented to the user is saved in
     * a global ArrayList<String> tbl.
     * <p>
     *
     * @param	cursr - the cursor that was made for the reading of the database
     * @return	None
     */
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
            String[] tmp2 = new String[]{String.valueOf(key), workerID_st, companyID_st, mealID_st, date_st, hour_st} ;
            tbl.add(tmp);
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



    /**
     * Shows the full information of the order that the user selected.
     * <p>
     *
     * @param	adapterView - the ListView that was clicked
     *          view - the item that was clicked
     *          i - the position of the item that was clicked in the Adapter
     *          l - the row that was clicked in the ListView
     * @return	None
     */
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


    /**
     * Sorts the list of companies according to the user's choice:
     *  - By first added first.
     *  - By last added first.
     *  - By worker last name A-Z first.
     *  - By worker last name Z-A first.
     *  - By company name A-Z first.
     *  - By company name Z-A first.
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
     * or the MakeOrder activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 0, "Make a New Order!");
        return true;
    }

    /**
     * Starts the MainActivity, CreditsScreen activity, or MakeOrder activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("Make a New Order!")){
            Intent si = new Intent(this, MakeOrder.class);
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
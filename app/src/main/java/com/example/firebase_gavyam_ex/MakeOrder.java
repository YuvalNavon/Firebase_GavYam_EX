
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version 	1
 * @since		5/5/2022
 * This activity is used to make new orders and add them to the database.
 */

package com.example.firebase_gavyam_ex;

import static com.example.firebase_gavyam_ex.FBref.refCompanies;
import static com.example.firebase_gavyam_ex.FBref.refOrders;
import static com.example.firebase_gavyam_ex.FBref.refWorkers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MakeOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner companyChoose;
    int spinnerselect;
    EditText ID, appetizer, mainCourse, extra, dessert, drink;
    String ID_st,  appetizer_st, mainCourse_st, extra_st , dessert_st , drink_st;
    ArrayList<String> activeCompaniesNames, activeWorkerLastNames, activeWorkerFirstNames, activeWorkerID, inactiveWorkerID;


    ValueEventListener workListener,companyListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);



        ID = (EditText) findViewById(R.id.workerID);
        appetizer = (EditText) findViewById(R.id.appetizer);
        mainCourse = (EditText) findViewById(R.id.mainCourse);
        extra = (EditText) findViewById(R.id.extra);
        dessert = (EditText) findViewById(R.id.dessert);
        drink = (EditText) findViewById(R.id.drink);


        companyChoose = (Spinner) findViewById(R.id.companychoosespinner);
        companyChoose.setOnItemSelectedListener(this);

        Query quer = refCompanies.orderByChild("TaxCompany");
        ReaderCompany(quer);



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (workListener!=null) {
            refWorkers.removeEventListener(workListener);
        }
        if (companyListener!=null) {
            refCompanies.removeEventListener(companyListener);
        }
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
        ID_st = ID.getText().toString();
        appetizer_st = appetizer.getText().toString();
        mainCourse_st = mainCourse.getText().toString();
        extra_st = extra.getText().toString();
        dessert_st = dessert.getText().toString();
        drink_st = drink.getText().toString();

        Query quer = refWorkers.orderByChild("lastName");
        ReaderWorker(quer);




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
        ID.setText("");
        appetizer.setText("");
        mainCourse.setText("");
        extra.setText("");
        dessert.setText("");
        drink.setText("");
        ID_st = "";
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
     * Makes an ArrayList of all active companies' names.
     * <p>
     *
     * @param	quer - the Query that was selected.
     * @return	None
     */
    public void ReaderCompany(Query quer){
        activeCompaniesNames = new ArrayList<>();
        companyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                for (DataSnapshot data : dS.getChildren()) {

                    Company comptmp = data.getValue(Company.class);

                    int Active_num = comptmp.getCompanyActive();

                    if (Active_num == 1) {
                        activeCompaniesNames.add(comptmp.getCompanyName());
                    }


                }


                ArrayAdapter<String> adp = new ArrayAdapter<String>(MakeOrder.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, activeCompaniesNames);
                companyChoose.setAdapter(adp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
          };
         quer.addValueEventListener(companyListener);

        }




    /**
     * Makes an ArrayList of all active worker's ID's, first names, and last names.
     * <p>
     *
     * @param	quer - the Query that was selected.
     * @return	None
     */
    public void ReaderWorker(Query quer){
        activeWorkerID = new ArrayList<>();
        inactiveWorkerID = new ArrayList<>();
        activeWorkerLastNames = new ArrayList<>();
        activeWorkerFirstNames = new ArrayList<>();
        workListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                for (DataSnapshot data : dS.getChildren()) {

                    Worker worktmp = data.getValue(Worker.class);

                    int Active_num = worktmp.getWorkerActive();

                    if (Active_num == 1) {
                        activeWorkerID.add(worktmp.getIdNumber());
                        activeWorkerLastNames.add(worktmp.getLastName());
                        activeWorkerFirstNames.add(worktmp.getFirstName());
                    }
                    else inactiveWorkerID.add(worktmp.getIdNumber());


                }

                if (!ID_st.isEmpty()){
                    if (idCheck() == -1){
                        Toast toast = Toast.makeText(getApplicationContext(), "Worker is INACTIVE", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else if (idCheck() == -2 ){
                        Toast toast = Toast.makeText(getApplicationContext(), "NONEXISTENT worker", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else if (!appetizer_st.isEmpty() && !mainCourse_st.isEmpty() && !extra_st.isEmpty() && !dessert_st.isEmpty() && !drink_st.isEmpty() )
                    {

                        Meal meal = new Meal(appetizer_st, mainCourse_st, extra_st, dessert_st, drink_st);




                        Calendar calendar = Calendar.getInstance();
                        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        String hour = "" + Integer.toString(hour24hrs) + ":" + Integer.toString(minutes);
                        if (minutes<10) hour = "" + Integer.toString(hour24hrs) + ":" + "0" + Integer.toString(minutes);

                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                        Hazmana order = new Hazmana(ID_st, activeWorkerLastNames.get(idCheck()),activeWorkerFirstNames.get(idCheck()), activeCompaniesNames.get(spinnerselect), meal, currentDate, hour );
                        refOrders.child(ID_st+ " - " + currentDate).setValue(order);


                        ID.setText("");
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
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };
        quer.addValueEventListener(workListener);



    }

    /**
     *  Checks the inputted ID to see if it belongs to an existing worker.
     *  If it does, the function checks if the worker is active or not.
     *  If they are active, the function returns the worker's index in the arrayLists containing
     *  their information. Otherwise, the function returns -1 in order to display an appropriate
     *  message.
     *  If the inputted ID does not belong to an existing worker, the function return -2 in order
     *  to display an appropriate message.
     *
     * <p>
     *
     * @param
     * @return	None
     */
    public int idCheck(){
        for (int i=0; i<activeWorkerID.size(); i++){
            if (ID_st.equals(activeWorkerID.get(i))) return i;


        }

        for (int i=0; i<inactiveWorkerID.size(); i++){
            if (ID_st.equals(inactiveWorkerID.get(i))) return -1;


        }
        return -2;

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
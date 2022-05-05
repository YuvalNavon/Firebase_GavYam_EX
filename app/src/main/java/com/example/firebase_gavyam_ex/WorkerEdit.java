
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 * This activity is used to to show the full details of a selected worker, and edit them.
 */


package com.example.firebase_gavyam_ex;

import static com.example.firebase_gavyam_ex.FBref.refWorkers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class WorkerEdit extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Button cancel, editconfirm;
    EditText first, last, id, company, phone;
    Switch activeSwitch;

    String  first_st, last_st, id_st, company_st, phone_st,  first_st_new, last_st_new, company_st_new, phone_st_new;
    int edit, active_int;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_edit);


        activeSwitch = (Switch) findViewById(R.id.activeWorker);
        activeSwitch.setOnCheckedChangeListener(this);


        Intent gi = getIntent();
        first_st = gi.getStringExtra("First");
        last_st = gi.getStringExtra("Last");
        id_st = gi.getStringExtra("ID");
        company_st = gi.getStringExtra("Company");
        phone_st = gi.getStringExtra("Phone");
        active_int = gi.getIntExtra("ActiveNum", 0);

        if (active_int==0){

            activeSwitch.setChecked(false);
            activeSwitch.setTextColor(Color.RED);
        }
        else if (active_int == 1) {

            activeSwitch.setChecked(true);
            activeSwitch.setTextColor(Color.BLUE);
        }

        first = (EditText) findViewById(R.id.FirstName2);
        last = (EditText) findViewById(R.id.LastName2);
        id = (EditText) findViewById(R.id.ID2);
        company = (EditText) findViewById(R.id.Company2);
        phone = (EditText) findViewById(R.id.PhoneNumber2);

        first.setText(first_st);
        last.setText(last_st);
        id.setText(id_st);
        company.setText(company_st);
        phone.setText(phone_st);






        cancel = (Button) findViewById(R.id.CancelWorker);
        editconfirm = (Button) findViewById(R.id.EditConfirmWorker);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setClickable(false);
        editconfirm.setText("EDIT");

        edit = 0;
        etDisabler();


        id.setEnabled(false);
    }

    /**
     *  When first pressed - lets the user edit the selected worker's details (goes into "EDIT MODE").
     *  When pressed again - verifies the new info that the user inputted, and if its valid it
     *  updates the selected worker's details (goes into "READ MODE").
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void EditConfirm(View view){

        if (edit == 0){
            cancel.setVisibility(View.VISIBLE);
            cancel.setClickable(true);
            editconfirm.setText("CONFIRM");
            first_st = first.getText().toString();
            last_st = last.getText().toString();
            id_st = id.getText().toString();
            company_st = company.getText().toString();
            phone_st = phone.getText().toString();


            etEnabler();
            edit = 1;

        }
        else if (edit == 1){
            first_st_new = first.getText().toString();
            last_st_new = last.getText().toString();
            company_st_new = company.getText().toString();
            phone_st_new = phone.getText().toString();




            if (!phoneCheck(phone_st_new)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT);
                toast.show();
            }

            if (!first_st_new.isEmpty() && !last_st_new.isEmpty()  && !company_st_new.isEmpty() && !phone_st_new.isEmpty() &&
                    phoneCheck(phone_st_new))
            {
                if (activeSwitch.isChecked()) active_int = 1;
                else active_int = 0;

                Worker worker = new Worker(first_st_new, last_st_new, company_st_new, id_st, phone_st_new, active_int);
                refWorkers.child(id_st).setValue(worker);


                cancel.setVisibility(View.INVISIBLE);
                cancel.setClickable(false);
                editconfirm.setText("EDIT");

                etDisabler();


                edit = 0;
                Toast toast = Toast.makeText(getApplicationContext(), "Changes saved successfully", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields correctly!", Toast.LENGTH_SHORT);
                toast.show();
            }



        }


    }


    /**
     * Resets the EditText to their original state (pre edit by the user) and resets the
     * edit/confirm Button.
     * Sets the activity into "READ MODE".
     * Only visible and usable when in "EDIT MODE".
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void Cancel(View view){
        cancel.setVisibility(View.INVISIBLE);
        cancel.setClickable(false);
        editconfirm.setText("EDIT");

        first.setText(first_st);
        last.setText(last_st);
        id.setText(id_st);
        company.setText(company_st);
        phone.setText(phone_st);

        etDisabler();




        edit = 0;
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
     * Checks if the phone number that the user inputted is valid or not.
     * <p>
     *
     * @param phone - the phone number that the user inputted.
     * @return	true - if the phone is valid, else - false.
     */
    public static boolean phoneCheck(String phone){
        if (phone.length()!= 10){
            return false;
        }
        String kidomet = phone.substring(0,3);
        if (!kidomet.equals("050") && !kidomet.equals("051") && !kidomet.equals("052") && !kidomet.equals("053") && !kidomet.equals("054") && !kidomet.equals("055")
                && !kidomet.equals("056") && !kidomet.equals("057") && !kidomet.equals("058")){
            return false;
        }
        return true;

    }

    /**
     * Disables all EditTexts and sets their little lines as invisible.
     * Used when in "READ MODE".
     * <p>
     *
     * @return	None
     */
    public void etDisabler(){
        first.setEnabled(false);
        last.setEnabled(false);
        company.setEnabled(false);
        phone.setEnabled(false);
        activeSwitch.setEnabled(false);

    }


    /**
     * Enables all EditTexts and sets their little lines as visible.
     * Used when in "EDIT MODE".
     * <p>
     *
     * @return	None
     */
    public void etEnabler(){
        first.setEnabled(true);
        last.setEnabled(true);
        company.setEnabled(true);
        phone.setEnabled(true);
        activeSwitch.setEnabled(true);

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            activeSwitch.setText("ACTIVE");
            activeSwitch.setTextColor(Color.BLUE);
        }
        else {
            activeSwitch.setText("INACTIVE");
            activeSwitch.setTextColor(Color.RED);
        }
    }


    /**
     * Creates the Options Menu, allowing the user to navigate to the Home screen, Credits screen
     * or the AddWorker activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Starts the MainActivity or CreditsScreen activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();

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
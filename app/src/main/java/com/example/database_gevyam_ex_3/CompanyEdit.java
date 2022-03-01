
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		1/3/2022
 * This activity is used to to show the full details of a selected company, and edit them.
 */


package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class CompanyEdit extends AppCompatActivity {
    Button cancel, editconfirm;
    EditText name, tax, mainPhone, secondaryPhone;
    Switch activeSwitch;

    String key_st, name_st, tax_st, mainPhone_st, secondaryPhone_st, active_st, name_st_new, tax_st_new, mainPhone_st_new, secondaryPhone_st_new, active_st_new, active_DB;
    int edit;


    SQLiteDatabase db;
    ContentValues cv;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit);

        hlp = new HelperDB(this);

        activeSwitch = (Switch) findViewById(R.id.activeCompany);


        Intent gi = getIntent();
        key_st = gi.getStringExtra("KEY");
        name_st = gi.getStringExtra("Name");
        tax_st = gi.getStringExtra("Tax");
        mainPhone_st = gi.getStringExtra("MainPhone");
        secondaryPhone_st = gi.getStringExtra("SecondaryPhone");
        active_st = gi.getStringExtra("ActiveNum");

        if (active_st.equals("INACTIVE") ){
            active_DB = "0";
            activeSwitch.setChecked(false);
        }
        else if (active_st.equals("ACTIVE")) {
            active_DB = "1";
            activeSwitch.setChecked(true);

        }

        name = (EditText) findViewById(R.id.companyName);
        tax = (EditText) findViewById(R.id.taxCompany);
        mainPhone = (EditText) findViewById(R.id.mainNumber);
        secondaryPhone = (EditText) findViewById(R.id.seco);

        name.setText(name_st);
        tax.setText(tax_st);
        mainPhone.setText(mainPhone_st);
        secondaryPhone.setText(secondaryPhone_st);






        cancel = (Button) findViewById(R.id.CancelCompany);
        editconfirm = (Button) findViewById(R.id.EditConfirmCompany);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setClickable(false);
        editconfirm.setText("EDIT");

        edit = 0;
        etDisabler();
        name.setVisibility(View.VISIBLE);
        tax.setVisibility(View.VISIBLE);
        mainPhone.setVisibility(View.VISIBLE);
        secondaryPhone.setVisibility(View.VISIBLE);
    }


    /**
     *  When first pressed - lets the user edit the selected company's details (goes into "EDIT MODE").
     *  When pressed again - verifies the new info that the user inputted, and if its valid it
     *  updates the selected company's details (goes into "READ MODE").
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
            name_st = name.getText().toString();
            tax_st = tax.getText().toString();
            mainPhone_st = mainPhone.getText().toString();
            secondaryPhone_st = secondaryPhone.getText().toString();



            etEnabler();
            edit = 1;

        }
        else if (edit == 1){
            name_st_new = name.getText().toString();
            tax_st_new = tax.getText().toString();
            mainPhone_st_new = mainPhone.getText().toString();
            secondaryPhone_st_new = secondaryPhone.getText().toString();


            if (!phoneCheck(mainPhone_st_new)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid MAIN phone number!", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (!phoneCheck(secondaryPhone_st_new)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid SECONDARY phone number!", Toast.LENGTH_SHORT);
                toast.show();
            }

            if (!name_st_new.isEmpty() && !tax_st_new.isEmpty() && !mainPhone_st_new.isEmpty() && !secondaryPhone_st_new.isEmpty()  && phoneCheck(mainPhone_st_new) && phoneCheck(secondaryPhone_st_new))
                {
                    db = hlp.getWritableDatabase();
                    cv = new ContentValues();
                    cv.put(Company.NAME, name_st_new);
                    cv.put(Company.TAX_COMPANY, tax_st_new);
                    cv.put(Company.MAIN_NUMBER, mainPhone_st_new);
                    cv.put(Company.SECONDARY_NUMBER, secondaryPhone_st_new);

                    if (activeSwitch.isChecked()) {
                        active_DB = "1";
                        active_st_new = "ACTIVE";
                    }
                    else {
                        active_DB = "0";
                        active_st_new = "INACTIVE";
                    }


                    cv.put(Company.COMPANY_ACTIVE, active_DB);
                    db.update(Company.TABLE_COMPANY, cv, Company.COMPANY_ID+"=?", new String[]{key_st});



                    db.close();

                    cancel.setVisibility(View.INVISIBLE);
                    cancel.setClickable(false);
                    editconfirm.setText("EDIT");

                    etDisabler();
                    name.setVisibility(View.VISIBLE);
                    tax.setVisibility(View.VISIBLE);
                    mainPhone.setVisibility(View.VISIBLE);
                    secondaryPhone.setVisibility(View.VISIBLE);

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

        name.setText(name_st);
        tax.setText(tax_st);
        mainPhone.setText(mainPhone_st);
        secondaryPhone.setText(secondaryPhone_st);

        etDisabler();
        name.setVisibility(View.VISIBLE);
        tax.setVisibility(View.VISIBLE);
        mainPhone.setVisibility(View.VISIBLE);
        secondaryPhone.setVisibility(View.VISIBLE);



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
        name.setEnabled(false);
        tax.setEnabled(false);
        mainPhone.setEnabled(false);
        secondaryPhone.setEnabled(false);
        activeSwitch.setEnabled(false);
        name.setVisibility(View.INVISIBLE);
        tax.setVisibility(View.INVISIBLE);
        mainPhone.setVisibility(View.INVISIBLE);
        secondaryPhone.setVisibility(View.INVISIBLE);
    }

    /**
     * Enables all EditTexts and sets their little lines as visible.
     * Used when in "EDIT MODE".
     * <p>
     *
     * @return	None
     */
    public void etEnabler(){
        name.setEnabled(true);
        tax.setEnabled(true);
        mainPhone.setEnabled(true);
        secondaryPhone.setEnabled(true);
        activeSwitch.setEnabled(true);
        name.setVisibility(View.VISIBLE);
        tax.setVisibility(View.VISIBLE);
        mainPhone.setVisibility(View.VISIBLE);
        secondaryPhone.setVisibility(View.VISIBLE);
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
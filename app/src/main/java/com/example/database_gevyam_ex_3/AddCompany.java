
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version 	1
 * @since		1/3/2022
 * This activity is used to add new companies to the database.
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
import android.widget.EditText;
import android.widget.Toast;

public class AddCompany extends AppCompatActivity {
    EditText name, tax, main, secondary;
    String name_st, tax_st, main_st, secondary_st;
    SQLiteDatabase db;
    ContentValues cv;
    HelperDB hlp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        name = (EditText) findViewById(R.id.Name);
        tax = (EditText) findViewById(R.id.TaxCompany);
        main = (EditText) findViewById(R.id.MainNumber);
        secondary = (EditText) findViewById(R.id.SecondaryNumber);
        hlp = new HelperDB(this);
    }


    /**
     * Saves the inputted information of the new company, verifies it and if it is valid, it adds the company to the database.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void Add(View view)
    {
        name_st = name.getText().toString();
        tax_st = tax.getText().toString();
        main_st = main.getText().toString();
        secondary_st = secondary.getText().toString();

        if (!phoneCheck(main_st)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid MAIN phone number!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!phoneCheck(secondary_st)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid SECONDARY phone number!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!name_st.isEmpty() && !tax_st.isEmpty() && !main_st.isEmpty() && !secondary_st.isEmpty() && phoneCheck(main_st) && phoneCheck(secondary_st))
            {
                cv = new ContentValues();
                cv.put(Company.NAME, name_st);
                cv.put(Company.TAX_COMPANY, tax_st);
                cv.put(Company.MAIN_NUMBER, main_st);
                cv.put(Company.SECONDARY_NUMBER, secondary_st);
                cv.put(Company.COMPANY_ACTIVE, "1");


                db = hlp.getWritableDatabase();
                db.insert(Company.TABLE_COMPANY, null, cv );
                db.close();
                name.setText("");
                tax.setText("");
                main.setText("");
                secondary.setText("");
                Toast toast = Toast.makeText(getApplicationContext(), "Company added successfully!", Toast.LENGTH_LONG);
                toast.show();

            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields correctly!", Toast.LENGTH_SHORT);
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
        name.setText("");
        tax.setText("");
        main.setText("");
        secondary.setText("");
        name_st = "";
        tax_st = "";
        main_st = "";
        secondary_st ="";
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
        System.out.println(kidomet);
        if (!kidomet.equals("050") && !kidomet.equals("051") && !kidomet.equals("052") && !kidomet.equals("053") && !kidomet.equals("054") && !kidomet.equals("055")
                && !kidomet.equals("056") && !kidomet.equals("057") && !kidomet.equals("058")){
            return false;
        }
        return true;

    }

    /**
     * Creates the Options Menu, allowing the user to navigate to the Home screen, Credits screen
     * or the CompanyDetails activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 0, "Companies Details");
        return true;
    }

    /**
     * Starts the MainActivity, CreditsScreen activity, or CompanyDetails activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("Companies Details")){
            Intent si = new Intent(this, CompanyDetails.class);
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

/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version 	1
 * @since		5/5/2022
 * The home menu for the GYNBus Manager app.
 */


package com.example.firebase_gavyam_ex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Starts the AddWorker activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */

    public void AddWorker(View view){
        Intent si = new Intent(this,AddWorker.class);
        startActivity(si);
    }

    /**
     * Starts the DetailsWorker activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void DetailsWorker(View view){
        Intent si = new Intent(this,WorkerDetails.class);
        startActivity(si);
    }


    /**
     * Starts the AddCompany activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void AddCompany(View view){
        Intent si = new Intent(this,AddCompany.class);
        startActivity(si);
    }

    /**
     * Starts the DetailsCompany activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void DetailsCompany(View view){
        Intent si = new Intent(this,CompanyDetails.class);
        startActivity(si);
    }

    /**
     * Starts the MakeOrder activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void MakeOrder(View view){
        Intent si = new Intent(this,MakeOrder.class);
        startActivity(si);
    }

    /**
     * Starts the OrderHistory activity.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void OrderHistory(View view){
        Intent si = new Intent(this,OrderHistory.class);
        startActivity(si);
    }


    /**
     * Creates the Options Menu, allowing the user to navigate to the Credits Screen.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.removeItem(R.id.Home);
        return true;
    }

    /**
     * Starts the CreditsScreen activity.
     * <p>
     *
     * @param	item - the MenuItem that is clicked (in this case, only the Credits Screen option).
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
       Intent si = new Intent(this, CreditsScreen.class);
       startActivity(si);
        return true;
    }

}
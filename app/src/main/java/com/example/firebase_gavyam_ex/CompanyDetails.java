
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 * This activity is used to show the main details of all companies, and allows the user to edit each
 * company by clicking on them.
 */


package com.example.firebase_gavyam_ex;

import static com.example.firebase_gavyam_ex.FBref.refCompanies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CompanyDetails extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    String active_st;
    boolean revOrder, nameAtoZ, taxUp, onlyActive, onlyInactive;

    Spinner spin;
    String [] sorts, listofFilters;


    AlertDialog.Builder adbFilter;
    AlertDialog adFilter;



    ListView listofCompanies;
    ArrayList<String> companyList;
    ArrayList<Company> companyValues;
    ArrayAdapter adp;

    ValueEventListener companyListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        nameAtoZ = false;
        taxUp = false;
        onlyActive = false;
        onlyInactive = false;


        listofCompanies = (ListView) findViewById(R.id.listOfCompanies);
        listofCompanies.setOnItemClickListener(this);
        listofCompanies.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        sorts = new String[]{"Tax Company ASC.","Tax Company DESC.","Name: A-Z","Name: Z-A"};
        spin = (Spinner) findViewById(R.id.sortSpinnerCompany);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,sorts);
        spin.setAdapter(adp);

        listofFilters = new String[]{"Active", "Inactive", "All Companies"};

        adbFilter = new AlertDialog.Builder(this);
        adbFilter.setTitle("Filter Companies");
        adbFilter.setCancelable(false);
        adbFilter.setItems(listofFilters, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    onlyActive = true;
                    onlyInactive = false;

                }
                if (i == 1){
                    onlyActive = false;
                    onlyInactive = true;

                }
                if (i == 2){
                    onlyActive = false;
                    onlyInactive = false;

                }

                Query querer;

                querer = refCompanies.orderByChild("taxCompany");
                if (nameAtoZ) querer = refCompanies.orderByChild("companyName");
                Reader(querer);

                if (revOrder){
                    Collections.reverse(companyList);
                    Collections.reverse(companyValues);
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(CompanyDetails.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, companyList);
                    listofCompanies.setAdapter(adp);
                }




            }
        });

        adbFilter.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        adFilter = adbFilter.create();

        revOrder = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (companyListener!=null) {
            refCompanies.removeEventListener(companyListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        spin.setSelection(0);
        Query querer = refCompanies.orderByChild("TaxCompany");
        Reader(querer);
    }


    /**
     * Shows the filter AlertDialog.
     * <p>
     *
     * @param	view - the Button that was clicked on
     * @return	None
     */
    public void filterSelect(View view){
        adFilter.show();
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
     * Reads the Company tree and saves each company's full information in a global
     * ArrayList<Copmany> called companyValues. the information that is presented to the user is saved in
     * a global ArrayList<String> companyList.
     * <p>
     *
     * @param	quer - the Query that was selected.
     * @return	None
     */
    public void Reader(Query quer){
        companyList = new ArrayList<>();
        companyValues = new ArrayList<>();
        companyListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                companyList.clear();
                companyValues.clear();
                for(DataSnapshot data : dS.getChildren()) {

                    Company comptmp = data.getValue(Company.class);

                    int Active_num = comptmp.getCompanyActive();
                    if (onlyActive){
                        if (Active_num == 1){
                            active_st = "ACTIVE";
                            String Tax = (String) data.getKey();
                            String Phone = comptmp.getMainPhoneNumber();
                            String Name = comptmp.getCompanyName();
                            companyList.add(Tax+": "+ Name + ", " + Phone + ", " + active_st );
                            companyValues.add(comptmp);
                        }
                    }
                    else if (onlyInactive){
                        if (Active_num == 0){
                            active_st = "INACTIVE";
                            String Tax = (String) data.getKey();
                            String Phone = comptmp.getMainPhoneNumber();
                            String Name = comptmp.getCompanyName();
                            companyList.add(Tax+": "+ Name + ", " + Phone + ", " + active_st );
                            companyValues.add(comptmp);
                        }
                    }
                    else{
                        if (Active_num == 0) active_st = "INACTIVE";
                        else  active_st = "ACTIVE";
                        String Tax = (String) data.getKey();
                        String Phone = comptmp.getMainPhoneNumber();
                        String Name = comptmp.getCompanyName();
                        companyList.add(Tax+": "+ Name + ", " + Phone + ", " + active_st );
                        companyValues.add(comptmp);
                    }

                }
                if (revOrder){
                    Collections.reverse(companyList);
                    Collections.reverse(companyValues);
                }
                adp = new ArrayAdapter<String>(CompanyDetails.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, companyList);
                listofCompanies.setAdapter(adp);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        quer.addValueEventListener(companyListener);






    }

    /**
     * Starts the EditCompany activity with the full information of the company that the user selected.
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
        Company companyDetails;
        companyDetails = companyValues.get(i);


        Intent si = new Intent(this, CompanyEdit.class);
        si.putExtra("Name", companyDetails.getCompanyName());
        si.putExtra("TaxCompany", companyDetails.getTaxCompany());
        si.putExtra("MainPhone", companyDetails.getMainPhoneNumber());
        si.putExtra("SecondaryPhone", companyDetails.getSecondaryPhoneNumber());
        si.putExtra("ActiveNum", companyDetails.getCompanyActive());
        startActivity(si);

    }



    /**
     * Sorts the list of companies according to the user's choice:
     *  - By first added first.
     *  - By last added first.
     *  - By name A-Z first.
     *  - By name Z-A first.
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


        Query querer;
        if (i==0){
            nameAtoZ = true;
            taxUp = false;
            revOrder = false;
            querer = refCompanies.orderByChild("taxCompany");

        }

        else if (i==1){
            revOrder = true;
            querer = refCompanies.orderByChild("taxCompany");

        }

        else if (i==2){
            nameAtoZ = false;
            taxUp = true;
            revOrder = false;
            querer = refCompanies.orderByChild("companyName");

        }
        else{
            revOrder = true;
            querer = refCompanies.orderByChild("companyName");


        }
        Reader(querer);

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
     * or the AddCompany activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 0, "Add a New Company!");
        return true;
    }

    /**
     * Starts the MainActivity, CreditsScreen activity, or AddCompany activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("Add a New Company!")){
            Intent si = new Intent(this, AddCompany.class);
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
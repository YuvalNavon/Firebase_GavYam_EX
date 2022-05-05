
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 * This activity is used to show the main details of all workers, and allows the user to edit each
 * worker by clicking on them.
 */


package com.example.firebase_gavyam_ex;

import static com.example.firebase_gavyam_ex.FBref.refWorkers;

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
import java.util.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkerDetails extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {



String  active_st;

boolean revOrder, workerAtoZ, workerZtoA,  companyAtoZ, companyZtoA, onlyActive, onlyInactive;

Spinner spin;
String [] sorts, listofFilters;


AlertDialog.Builder adbFilter;
AlertDialog adFilter;


ListView listofWorkers;
ArrayList<String> workList;
ArrayList<Worker> workValues;
ArrayAdapter adp;

ValueEventListener workListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        workerAtoZ = true;
        workerZtoA = false;

        companyAtoZ = false;
        companyZtoA = false;

        onlyActive = false;
        onlyInactive = false;



        listofWorkers = (ListView) findViewById(R.id.listOfWorkers);
        listofWorkers.setOnItemClickListener(this);
        listofWorkers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        sorts = new String[]{"Last name: A-Z","Last name: Z-A", "Company: A-Z", "Company: Z-A"};
        spin = (Spinner) findViewById(R.id.sortSpinnerWorker);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,sorts);
        spin.setAdapter(adp);

        listofFilters = new String[]{"Active", "Inactive", "All workers"};

        adbFilter = new AlertDialog.Builder(this);
        adbFilter.setTitle("Filter Workers");
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

                querer = refWorkers.orderByChild("lastName");
                if (companyAtoZ) querer = refWorkers.orderByChild("workCompany");
                Reader(querer);

                if (revOrder){
                    Collections.reverse(workList);
                    Collections.reverse(workValues);
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(WorkerDetails.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, workList);
                    listofWorkers.setAdapter(adp);
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
        if (workListener!=null) {
            refWorkers.removeEventListener(workListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        spin.setSelection(0);
        Query querer = refWorkers.orderByChild("lastName");
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
     * Reads the Worker tree and saves each worker's full information in a global
     * ArrayList<Worker> called workerValues. the information that is presented to the user is saved in
     * a global ArrayList<String> workList.
     * <p>
     *
     * @param  quer - the Query that was selected.
     * @return	None
     */
    public void Reader(Query quer){
        workList = new ArrayList<>();
        workValues = new ArrayList<>();
        workListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                workList.clear();
                workValues.clear();
                for(DataSnapshot data : dS.getChildren()) {

                    Worker workTmp = data.getValue(Worker.class);

                    int Active_num = workTmp.getWorkerActive();
                    if (onlyActive){
                        if (Active_num == 1){
                            active_st = "ACTIVE";
                            String ID = (String) data.getKey();
                            String Last = workTmp.getLastName();
                            String Company = workTmp.getWorkCompany();
                            workList.add(ID+": "+ Last + ", " + Company + ", " + active_st );
                            workValues.add(workTmp);
                        }
                    }
                    else if (onlyInactive){
                        if (Active_num == 0){
                            active_st = "INACTIVE";
                            String ID = (String) data.getKey();
                            String Last = workTmp.getLastName();
                            String Company = workTmp.getWorkCompany();
                            workList.add(ID+": "+ Last + ", " + Company + ", " + active_st );
                            workValues.add(workTmp);
                        }
                    }
                    else{
                        if (Active_num == 0) active_st = "INACTIVE";
                        else  active_st = "ACTIVE";
                        String ID = (String) data.getKey();
                        String Last = workTmp.getLastName();
                        String Company = workTmp.getWorkCompany();
                        workList.add(ID+": "+ Last + ", " + Company + ", " + active_st );
                        workValues.add(workTmp);
                    }

                }
                if (revOrder){
                    Collections.reverse(workList);
                    Collections.reverse(workValues);
                }
                adp = new ArrayAdapter<String>(WorkerDetails.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, workList);
                listofWorkers.setAdapter(adp);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        quer.addValueEventListener(workListener);






    }



    /**
     * Starts the EditWorker activity with the full information of the worker that the user selected.
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
        Worker workerDetails;
        workerDetails = workValues.get(i);


        Intent si = new Intent(this, WorkerEdit.class);
        si.putExtra("First", workerDetails.getFirstName());
        si.putExtra("Last", workerDetails.getLastName());
        si.putExtra("ID", workerDetails.getIdNumber());
        si.putExtra("Company", workerDetails.getWorkCompany());
        si.putExtra("Phone", workerDetails.getPhoneNumber());
        si.putExtra("ActiveNum", workerDetails.getWorkerActive());
        startActivity(si);




    }


    /**
     * Sorts the list of workers according to the user's choice:
     *  - By first added first.
     *  - By last added first.
     *  - By last name A-Z first.
     *  - By last name Z-A first.
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
            workerAtoZ = true;
            companyAtoZ = false;
            revOrder = false;
            querer = refWorkers.orderByChild("lastName");

        }

        else if (i==1){
            revOrder = true;
            querer = refWorkers.orderByChild("lastName");

        }

        else if (i==2){
            companyAtoZ = true;
            workerAtoZ = false;
            revOrder = false;
            querer = refWorkers.orderByChild("workCompany");

        }
        else{
            revOrder = true;
            querer = refWorkers.orderByChild("workCompany");


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
     * or the AddWorker activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 0, "Add a New Worker!");
        return true;
    }

    /**
     * Starts the MainActivity, CreditsScreen activity, or AddWorker activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("Add a New Worker!")){
            Intent si = new Intent(this, AddWorker.class);
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
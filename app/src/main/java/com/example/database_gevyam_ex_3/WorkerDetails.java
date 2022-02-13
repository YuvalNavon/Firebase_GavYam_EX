package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkerDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {



String first_st, last_st, id_st, company_st, phone_st, active_st, first_st_new, last_st_new, id_st_new, company_st_new, phone_st_new, active_st_new;
int edit, active, key;
boolean pick, finito;

SQLiteDatabase db;
ContentValues cv;
HelperDB hlp;
Cursor crsr;

ListView listofWorkers;
ArrayList<String> tbl;
ArrayList<String[]> tblfull;
ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);



        hlp = new HelperDB(this);


        listofWorkers = (ListView) findViewById(R.id.listofWorkers);
        listofWorkers.setOnItemClickListener(this);
        listofWorkers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Reader();
    }

    public void Reader(){
        db=hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        tblfull = new ArrayList<>();
        crsr = db.query(Worker.TABLE_WORKER, null, null, null, null, null, null);
        int col_key = crsr.getColumnIndex(Worker.KEY_NUMBER);
        int col_first = crsr.getColumnIndex(Worker.FIRST_NAME);
        int col_last = crsr.getColumnIndex(Worker.LAST_NAME);
        int col_id = crsr.getColumnIndex(Worker.ID_NUMBER);
        int col_company = crsr.getColumnIndex(Worker.WORK_COMPANY);
        int col_phone = crsr.getColumnIndex(Worker.PHONE_NUMBER);
        int col_active = crsr.getColumnIndex(Worker.WORKER_ACTIVE);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()){
            key = crsr.getInt(col_key);
            first_st = crsr.getString(col_first);
            last_st = crsr.getString(col_last);
            id_st = crsr.getString(col_id);
            company_st = crsr.getString(col_company);
            phone_st = crsr.getString(col_phone);
            active = crsr.getInt(col_active);
            if (active==0) active_st = "INACTIVE";
            else active_st = "ACTIVE";
            String tmp = "" + key + ": " + first_st + ", " + last_st + ", " + company_st + ", " + active_st;
            String[] tmp2 = new String[]{String.valueOf(key), first_st, last_st, id_st, company_st, phone_st, active_st} ;
            tbl.add(tmp);
            tblfull.add(tmp2);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
        listofWorkers.setAdapter(adp);



    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String[] workerDetails = tblfull.get(i);
        Intent si = new Intent(this, WorkerEdit.class);
        si.putExtra("First", workerDetails[1]);
        si.putExtra("Last", workerDetails[2]);
        si.putExtra("ID", workerDetails[3]);
        si.putExtra("Company", workerDetails[4]);
        si.putExtra("Phone", workerDetails[5]);
        si.putExtra("Active", workerDetails[6]);
        startActivity(si);




    }





}
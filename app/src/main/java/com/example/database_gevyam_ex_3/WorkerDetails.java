package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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

Button cancel, editconfirm;
EditText first, last, id, company, phone;
Switch activeSwitch;

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

        first = (EditText) findViewById(R.id.FirstName2);
        last = (EditText) findViewById(R.id.LastName2);
        id = (EditText) findViewById(R.id.ID2);
        company = (EditText) findViewById(R.id.Company2);
        phone = (EditText) findViewById(R.id.PhoneNumber2);

        activeSwitch = (Switch) findViewById(R.id.activeWorker);


       etDisabler();


        hlp = new HelperDB(this);

        edit = 0;

        cancel = (Button) findViewById(R.id.Cancel);
        editconfirm = (Button) findViewById(R.id.EditConfirm);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setClickable(false);
        editconfirm.setText("EDIT");

        listofWorkers = (ListView) findViewById(R.id.listofWorkers);
        listofWorkers.setOnItemClickListener(this);
        listofWorkers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Reader();

        pick = false;
        finito = true;


    }


    public void EditConfirm(View view){
      if (pick){
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

              finito = false;
              edit = 1;

          }
          else if (edit == 1){
              first_st_new = first.getText().toString();
              last_st_new = last.getText().toString();
              id_st_new = id.getText().toString();
              company_st_new = company.getText().toString();
              phone_st_new = phone.getText().toString();


              if (!id_check(id_st)) {
                  Toast toast = Toast.makeText(getApplicationContext(), "Invalid ID number!", Toast.LENGTH_LONG);
                  toast.show();
              }
              else{
                  if (!first_st_new.isEmpty() && !last_st_new.isEmpty() && !id_st_new.isEmpty() && !company_st_new.isEmpty() && !phone_st_new.isEmpty() && id_check(id_st_new))
                  {
                      db = hlp.getWritableDatabase();
                      cv = new ContentValues();
                      cv.put(Worker.FIRST_NAME, first_st_new);
                      cv.put(Worker.LAST_NAME, last_st_new);
                      cv.put(Worker.ID_NUMBER, id_st_new);
                      cv.put(Worker.WORK_COMPANY, company_st_new);
                      cv.put(Worker.PHONE_NUMBER, phone_st_new);

                      if (activeSwitch.isChecked()) {
                          active = 1;
                          active_st_new = "ACTIVE";
                      }
                      else {
                          active = 0;
                          active_st_new = "INACTIVE";
                      }


                      cv.put(Worker.WORKER_ACTIVE, active);
                      db.update(Worker.TABLE_WORKER, cv, Worker.FIRST_NAME+"=?", new String[]{first_st});
                      Reader();


                      db.close();

                      cancel.setVisibility(View.INVISIBLE);
                      cancel.setClickable(false);
                      editconfirm.setText("EDIT");

                      etDisabler();
                      first.setVisibility(View.VISIBLE);
                      last.setVisibility(View.VISIBLE);
                      id.setVisibility(View.VISIBLE);
                      company.setVisibility(View.VISIBLE);
                      phone.setVisibility(View.VISIBLE);

                      finito = true;
                      edit = 0;
                  }
                  else{
                      Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields!", Toast.LENGTH_LONG);
                      toast.show();
                  }
              }


          }
      }
      else {
          Toast toast = Toast.makeText(getApplicationContext(), "Please choose a worker first!", Toast.LENGTH_LONG);
          toast.show();
      }

    }

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
        first.setVisibility(View.VISIBLE);
        last.setVisibility(View.VISIBLE);
        id.setVisibility(View.VISIBLE);
        company.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);



        finito = true;
        edit = 0;
    }

    public void Back(View view){
       finish();
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
            String tmp = "" + key + ": " + last_st + ", " + company_st + ", " + active_st;
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

       if (finito) {
           String[] workerDetails = tblfull.get(i);
           first.setText(workerDetails[1]);
           last.setText(workerDetails[2]);
           id.setText(workerDetails[3]);
           company.setText(workerDetails[4]);
           phone.setText(workerDetails[5]);
           first.setVisibility(View.VISIBLE);
           last.setVisibility(View.VISIBLE);
           id.setVisibility(View.VISIBLE);
           company.setVisibility(View.VISIBLE);
           phone.setVisibility(View.VISIBLE);
           if (workerDetails[6] == "INACTIVE") activeSwitch.setChecked(false);
           else activeSwitch.setChecked(true);

           pick = true;
       }

        else {
           Toast toast = Toast.makeText(getApplicationContext(), "Please finish editing first!", Toast.LENGTH_LONG);
           toast.show();
       }
    }

    public static boolean id_check(String ID){
        return true;
    }

    public void etDisabler(){
        first.setEnabled(false);
        last.setEnabled(false);
        id.setEnabled(false);
        company.setEnabled(false);
        phone.setEnabled(false);
        activeSwitch.setEnabled(false);
        first.setVisibility(View.INVISIBLE);
        last.setVisibility(View.INVISIBLE);
        id.setVisibility(View.INVISIBLE);
        company.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
    }

    public void etEnabler(){
        first.setEnabled(true);
        last.setEnabled(true);
        id.setEnabled(true);
        company.setEnabled(true);
        phone.setEnabled(true);
        activeSwitch.setEnabled(true);
        first.setVisibility(View.VISIBLE);
        last.setVisibility(View.VISIBLE);
        id.setVisibility(View.VISIBLE);
        company.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
    }

}
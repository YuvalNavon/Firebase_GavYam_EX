package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class WorkerEdit extends AppCompatActivity {
    Button cancel, editconfirm;
    EditText first, last, id, company, phone;
    Switch activeSwitch;

    String keyID_st, first_st, last_st, id_st, company_st, phone_st, active_st, first_st_new, last_st_new, id_st_new, company_st_new, phone_st_new, active_st_new, active_DB;
    int edit;


    SQLiteDatabase db;
    ContentValues cv;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_edit);

        hlp = new HelperDB(this);

        activeSwitch = (Switch) findViewById(R.id.activeWorker);


        Intent gi = getIntent();
        keyID_st = gi.getStringExtra("KEY");
        System.out.println(keyID_st);
        first_st = gi.getStringExtra("First");
        last_st = gi.getStringExtra("Last");
        id_st = gi.getStringExtra("ID");
        company_st = gi.getStringExtra("Company");
        phone_st = gi.getStringExtra("Phone");
        active_st = gi.getStringExtra("ActiveNum");

        if (active_st.equals("INACTIVE") ){
            active_DB = "0";
            activeSwitch.setChecked(false);
        }
        else if (active_st.equals("ACTIVE")) {
            active_DB = "1";
            activeSwitch.setChecked(true);

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
        first.setVisibility(View.VISIBLE);
        last.setVisibility(View.VISIBLE);
        id.setVisibility(View.VISIBLE);
        company.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
    }




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
                id_st_new = id.getText().toString();
                company_st_new = company.getText().toString();
                phone_st_new = phone.getText().toString();


                if (!idCheck(id_st_new)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid ID number!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (!phoneCheck(phone_st_new)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (!first_st_new.isEmpty() && !last_st_new.isEmpty() && !id_st_new.isEmpty() && !company_st_new.isEmpty() && !phone_st_new.isEmpty() &&
                        idCheck(id_st_new) && phoneCheck(phone_st_new))
                    {
                        db = hlp.getWritableDatabase();
                        cv = new ContentValues();
                        cv.put(Worker.FIRST_NAME, first_st_new);
                        cv.put(Worker.LAST_NAME, last_st_new);
                        cv.put(Worker.ID_NUMBER, id_st_new);
                        cv.put(Worker.WORK_COMPANY, company_st_new);
                        cv.put(Worker.PHONE_NUMBER, phone_st_new);

                        if (activeSwitch.isChecked()) {
                            active_DB = "1";
                            active_st_new = "ACTIVE";
                        }
                        else {
                            active_DB = "0";
                            active_st_new = "INACTIVE";
                        }


                        cv.put(Worker.WORKER_ACTIVE, active_DB);
                        db.update(Worker.TABLE_WORKER, cv, Worker.KEY_NUMBER+"=?", new String[]{keyID_st});



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



        edit = 0;
    }

    public void goBack(View view){
        finish();
    }


    public static boolean idCheck(String ID){
        if (ID.length()>0 && ID.length()<=9){


            if (ID.length()<9){
                int add = 9 - ID.length();
                String zeroAdder = "";
                for (int i = 0; i<add; i++){
                    zeroAdder = zeroAdder + "0";
                }
                ID = zeroAdder + ID;
            }

            Integer[] macphelot = new Integer[9];
            String checker = "121212121";
            int sum = 0;
            for (int i = 0; i<macphelot.length;i ++){
                macphelot[i] = Integer.parseInt(ID.substring(i, i+1)) * Integer.parseInt(checker.substring(i,i+1));
                if (macphelot[i]>9){
                    macphelot[i] = macphelot[i]/10 + macphelot[i]%10;
                }
                sum+= macphelot[i];
            }
            if (sum%10!=0){
                return false;
            }
            else return true;
        }
        else{
            return false;
        }




    }

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
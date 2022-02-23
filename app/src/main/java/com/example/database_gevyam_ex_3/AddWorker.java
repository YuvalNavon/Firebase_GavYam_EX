package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.*;

public class AddWorker extends AppCompatActivity {
EditText first, last, id, company, phone;
String first_st, last_st, id_st, company_st, phone_st;
SQLiteDatabase db;
ContentValues cv;
HelperDB hlp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);

        first = (EditText) findViewById(R.id.FirstName);
        last = (EditText) findViewById(R.id.LastName);
        id = (EditText) findViewById(R.id.ID);
        company = (EditText) findViewById(R.id.Company);
        phone = (EditText) findViewById(R.id.PhoneNumber);
        hlp = new HelperDB(this);


    }


    public void Add(View view)
    {
        first_st = first.getText().toString();
        last_st = last.getText().toString();
        id_st = id.getText().toString();
        company_st = company.getText().toString();
        phone_st = phone.getText().toString();
        if (!idCheck(id_st)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid ID number!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!phoneCheck(phone_st)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (!first_st.isEmpty() && !last_st.isEmpty() && !id_st.isEmpty() && !company_st.isEmpty() && !phone_st.isEmpty() && idCheck(id_st) && phoneCheck(phone_st))
        {
                cv = new ContentValues();
                cv.put(Worker.FIRST_NAME, first_st);
                cv.put(Worker.LAST_NAME, last_st);
                cv.put(Worker.WORK_COMPANY, company_st);
                cv.put(Worker.ID_NUMBER, id_st);
                cv.put(Worker.PHONE_NUMBER, phone_st);
                cv.put(Worker.WORKER_ACTIVE, "1");
                db = hlp.getWritableDatabase();
                db.insert(Worker.TABLE_WORKER, null, cv );
                db.close();
                first.setText("");
                last.setText("");
                id.setText("");
                company.setText("");
                phone.setText("");
                Toast toast = Toast.makeText(getApplicationContext(), "Worker added successfully!", Toast.LENGTH_LONG);
                toast.show();

        }
        else{
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields correctly!", Toast.LENGTH_SHORT);
                toast.show();
        }


    }

    public void clear(View view){
        first.setText("");
        last.setText("");
        id.setText("");
        company.setText("");
        phone.setText("");
        first_st = "";
        last_st = "";
        id_st = "";
        company_st = "";
        phone_st ="";
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

}
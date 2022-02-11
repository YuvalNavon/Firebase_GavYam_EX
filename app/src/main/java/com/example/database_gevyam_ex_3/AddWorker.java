package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        if (!id_check(id_st)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid ID number!", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            if (!first_st.isEmpty() && !last_st.isEmpty() && !id_st.isEmpty() && !company_st.isEmpty() && !phone_st.isEmpty() && id_check(id_st))
            {
                cv = new ContentValues();
                cv.put(Worker.FIRST_NAME, first_st);
                cv.put(Worker.LAST_NAME, last_st);
                cv.put(Worker.WORK_COMPANY, company_st);
                cv.put(Worker.ID_NUMBER, id_st);
                cv.put(Worker.PHONE_NUMBER, phone_st);
                cv.put(Worker.WORKER_ACTIVE, 1);
                db = hlp.getWritableDatabase();
                db.insert(Worker.TABLE_WORKER, null, cv );
                db.close();
                first.setText("");
                last.setText("");
                id.setText("");
                company.setText("");
                phone.setText("");

            }
            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Please fill ALL fields!", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    public static boolean id_check(String ID){
        return true;
    }

    public void Back(View view){
        finish();
    }
}
package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void AddWorker(View view){
        Intent si = new Intent(this,AddWorker.class);
        startActivity(si);
    }
    public void DetailsWorker(View view){
        Intent si = new Intent(this,WorkerDetails.class);
        startActivity(si);
    }


}
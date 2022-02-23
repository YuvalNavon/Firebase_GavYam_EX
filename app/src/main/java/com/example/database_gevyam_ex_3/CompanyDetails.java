package com.example.database_gevyam_ex_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class CompanyDetails extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    String name_st, tax_st, mainPhone_st, secondaryPhone_st, active_st, active_DB;
    int key;
    boolean revOrder, curr_reversed, AtoZ, ZtoA, onlyActive, onlyInactive;

    Spinner spin;
    String [] sorts, listofFilters,selectionArgs;


    AlertDialog.Builder adbFilter;
    AlertDialog adFilter;


    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;

    ListView listofCompanies;
    ArrayList<String> tbl, tbl2;
    ArrayList<String[]> tblfull;
    ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        AtoZ = false;
        ZtoA = false;
        onlyActive = false;
        onlyInactive = false;

        hlp = new HelperDB(this);


        listofCompanies = (ListView) findViewById(R.id.listOfCompanies);
        listofCompanies.setOnItemClickListener(this);
        listofCompanies.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        sorts = new String[]{"First added","Last added","Name: A-Z","Name: Z-A"};
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
                String selection = null;
                if (i == 0){
                    selection = Company.COMPANY_ACTIVE + "=?";
                    selectionArgs = new String[]{"1"};
                    onlyActive = true;
                    onlyInactive = false;

                }
                if (i == 1){
                    selection = Company.COMPANY_ACTIVE + "=?";
                    selectionArgs = new String[]{"0"};

                    onlyActive = false;
                    onlyInactive = true;

                }
                if (i == 2){
                    selection =null;
                    selectionArgs = null;

                    onlyActive = false;
                    onlyInactive = false;

                }
                db=hlp.getReadableDatabase();
                crsr = db.query(Company.TABLE_COMPANY, null, selection, selectionArgs, null,null, null, null);
                if (AtoZ)  crsr = db.query(Company.TABLE_COMPANY, null, selection, selectionArgs, null,null, Company.NAME, null);
                else if (ZtoA) crsr = db.query(Company.TABLE_COMPANY, null, selection, selectionArgs, null,null, Company.NAME + " DESC", null);
                else  if (curr_reversed) revOrder = true;
                Reader(crsr);
                crsr.close();
                db.close();



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
        curr_reversed = false;

    }


    @Override
    protected void onResume() {
        super.onResume();
        spin.setSelection(0);
        db=hlp.getReadableDatabase();
        crsr = db.query(Company.TABLE_COMPANY, null, null, null, null, null, null, null);
        Reader(crsr);
        crsr.close();
        db.close();
    }


    public ArrayList<String> reverse_tbl(){
        tbl2 = new ArrayList<>();
        for (int i = tbl.size()-1; i>=0; i--){
            tbl2.add(tbl.get(i));
        }
        return tbl2;
    }

    public void Reader(Cursor cursr){
        db=hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        tblfull = new ArrayList<>();
        int col_key = cursr.getColumnIndex(Company.COMPANY_ID);
        int col_name = cursr.getColumnIndex(Company.NAME);
        int col_tax = cursr.getColumnIndex(Company.TAX_COMPANY);
        int col_mainPhone = cursr.getColumnIndex(Company.MAIN_NUMBER);
        int col_secondaryPhone = cursr.getColumnIndex(Company.SECONDARY_NUMBER);
        int col_active = cursr.getColumnIndex(Company.COMPANY_ACTIVE);
        cursr.moveToFirst();
        while (!cursr.isAfterLast()){
            key = cursr.getInt(col_key);
            name_st = cursr.getString(col_name);
            tax_st = cursr.getString(col_tax);
            mainPhone_st = cursr.getString(col_mainPhone);
            secondaryPhone_st = cursr.getString(col_secondaryPhone);
            active_DB = cursr.getString(col_active);
            if (active_DB.equals("0")) active_st = "INACTIVE";
            else active_st = "ACTIVE";
            String tmp = "" + key + ": " + name_st + ", " + mainPhone_st + ", " + active_st;
            String[] tmp2 = new String[]{String.valueOf(key), name_st, tax_st, mainPhone_st, secondaryPhone_st, active_st} ;
            tbl.add(tmp);
            tblfull.add(tmp2);
            cursr.moveToNext();
        }
        cursr.close();
        db.close();
        if (revOrder) tbl = reverse_tbl();
        adp = new ArrayAdapter<String>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tbl);
        listofCompanies.setAdapter(adp);
        revOrder = false;



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] companyDetails = tblfull.get(i);
        Intent si = new Intent(this, CompanyEdit.class);
        si.putExtra("KEY", companyDetails[0]);
        si.putExtra("Name", companyDetails[1]);
        si.putExtra("Tax", companyDetails[2]);
        si.putExtra("MainPhone", companyDetails[3]);
        si.putExtra("SecondaryPhone", companyDetails[4]);
        si.putExtra("ActiveNum", companyDetails[5]);
        System.out.println(companyDetails[1]);
        System.out.println(companyDetails[2]);
        System.out.println(companyDetails[3]);
        System.out.println(companyDetails[4]);
        System.out.println(companyDetails[5]);
        startActivity(si);

    }


    public void filterSelect(View view){
        adFilter.show();
    }


    public void goBack(View view){
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String[] columns = null;
        String groupBy  = null;
        String having  = null;
        String orderBy = null;
        String limit  = null;

        if (i==0){
            AtoZ = false;
            ZtoA = false;
            curr_reversed = false;
        }

        else if (i==1){
            revOrder = true;
            curr_reversed = true;
            AtoZ = false;
            ZtoA = false;

        }

        else if (i==2){
            orderBy = Company.NAME;
            AtoZ = true;
            ZtoA = false;

        }
        else if (i==3){
            orderBy = Company.NAME + " DESC";
            ZtoA = true;
            AtoZ = false;
        }
        db=hlp.getReadableDatabase();
        if (onlyActive || onlyInactive) crsr = db.query(Company.TABLE_COMPANY, columns, Company.COMPANY_ACTIVE+ "=?", selectionArgs, groupBy, having, orderBy, limit);
        else crsr = db.query(Company.TABLE_COMPANY, columns, null, null, groupBy, having, orderBy, limit);
        Reader(crsr);
        crsr.close();
        db.close();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
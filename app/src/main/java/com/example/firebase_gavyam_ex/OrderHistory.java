
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 * This activity is used to show the full details of all orders.
 */


package com.example.firebase_gavyam_ex;

import static com.example.firebase_gavyam_ex.FBref.refOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class OrderHistory extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    boolean revOrder;

    Spinner spin;
    String [] sorts;

    TextView order, worker, appetizer, maincourse, extra, dessert, drink;



    ListView listofOrders;
    ArrayList<String> orderList;
    ArrayList<Hazmana> orderValues;
    ArrayAdapter adp;

    ValueEventListener orderListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);



        revOrder = false;




        listofOrders = (ListView) findViewById(R.id.listOfOrders);
        listofOrders.setOnItemClickListener(this);
        listofOrders.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        sorts = new String[]{"First added","Last added","Worker: A-Z", "Worker: Z-A", "Company: A-Z","Company: Z-A"};
        spin = (Spinner) findViewById(R.id.sortSpinnerOrder);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,sorts);
        spin.setAdapter(adp);

        order = findViewById(R.id.OrderNumber);
        worker = findViewById(R.id.WorkerIDOrder);
        appetizer = findViewById(R.id.AppetizerOrder);
        maincourse = findViewById(R.id.MainCourseOrder);
        extra = findViewById(R.id.ExtraOrder);
        dessert = findViewById(R.id.DessertOrder);
        drink = findViewById(R.id.DrinkOrder);

        order.setText("Order number - ");
        worker.setText("");
        appetizer.setText("");
        maincourse.setText("");
        extra.setText("");
        dessert.setText("");
        drink.setText("");



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (orderListener!=null) {
            refOrders.removeEventListener(orderListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        spin.setSelection(0);
        Query quer = refOrders.orderByChild("date");
        Reader(quer);
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
     *  Reads the Order tree and saves each order's full information in a global
     *  ArrayList<Order> called orderValues. the information that is presented to the user is saved in
     *  a global ArrayList<String> orderList.
     * <p>
     *
     * @param	quer - the Query that was selected.
     * @return	None
     */
    public void Reader(Query quer){
        orderList = new ArrayList<>();
        orderValues = new ArrayList<>();
        orderListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dS) {
                orderList.clear();
                orderValues.clear();
                for (DataSnapshot data : dS.getChildren()) {

                    Hazmana ordertemp = data.getValue(Hazmana.class);
                    String workerId = ordertemp.getWorkerID();
                    String companyName = ordertemp.getCompanyName();
                    String date = ordertemp.getDate();
                    String hour = ordertemp.getHour();

                    orderList.add(date + " " + hour +  ": " + workerId + ", " + companyName);
                    orderValues.add(ordertemp);

                    if (revOrder) {
                        Collections.reverse(orderList);
                        Collections.reverse(orderValues);
                    }

                    adp = new ArrayAdapter<String>(OrderHistory.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, orderList);
                    listofOrders.setAdapter(adp);


                }

            }
            @Override
                public void onCancelled (@NonNull DatabaseError error){
                }

        };
        quer.addValueEventListener(orderListener);



    }



    /**
     * Shows the full information of the order that the user selected.
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
        Hazmana orderDetails;
        orderDetails = orderValues.get(i);
        appetizer.setText(orderDetails.getMeal().getAppetizer());
        maincourse.setText(orderDetails.getMeal().getMainMeal());
        extra.setText(orderDetails.getMeal().getExtra());
        dessert.setText(orderDetails.getMeal().getDessert());
        drink.setText(orderDetails.getMeal().getDrink());
        worker.setText(orderDetails.getWorkerFirstName() + " " + orderDetails.getWorkerLastName());
        order.setText("Order number - " + String.valueOf(i+1));


    }


    /**
     * Sorts the list of companies according to the user's choice:
     *  - By first added first.
     *  - By last added first.
     *  - By worker last name A-Z first.
     *  - By worker last name Z-A first.
     *  - By company name A-Z first.
     *  - By company name Z-A first.
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
        Query quer = refOrders.orderByChild("date");
        if (i==0){
            revOrder = false;
        }

        else if (i==1){
            revOrder = true;


        }


        else if (i==2){

            quer = refOrders.orderByChild("workerLastName");
            revOrder = false;

        }

        else if (i==3){

            quer = refOrders.orderByChild("workerLastName");
            revOrder = true;
        }

        else if (i==4){
            quer = refOrders.orderByChild("companyName");
            revOrder = false;
        }

        else if (i==5){
            quer = refOrders.orderByChild("companyName");
            revOrder = true;

        }

        Reader(quer);
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
     * or the MakeOrder activity.
     * <p>
     *
     * @param	menu - the Menu that is created
     * @return	boolean true - mandatory
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, 0, 0, "Make a New Order!");
        return true;
    }

    /**
     * Starts the MainActivity, CreditsScreen activity, or MakeOrder activity according to the
     * user's choice.
     * <p>
     *
     * @param	item - the MenuItem that is clicked.
     * @return	boolean true - mandatory
     */
    public boolean onOptionsItemSelected(MenuItem item){
        String st = item.getTitle().toString();
        if (st.equals("Make a New Order!")){
            Intent si = new Intent(this, MakeOrder.class);
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
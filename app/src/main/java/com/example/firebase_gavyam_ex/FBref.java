
/**
 * @author		Yuval Navon <yuvalnavon8@gmail.com>
 * @version	    1
 * @since		5/5/2022
 * This activity is used to to make the refrence objects for the trees in the database.
 */

package com.example.firebase_gavyam_ex;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBref {

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();

    public static DatabaseReference refWorkers=FBDB.getReference("Workers");

    public static DatabaseReference refCompanies=FBDB.getReference("Companies");

    public static DatabaseReference refOrders=FBDB.getReference("Orders");
}

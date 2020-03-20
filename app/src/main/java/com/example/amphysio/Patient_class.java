package com.example.amphysio;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Patient_class extends AppCompatActivity {


    AHBottomNavigation bottomNavigation;
    Fragment frag;
    private String incomingID = "";
    DatabaseReference ref3;
    ValueEventListener listener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity_main);

        Bundle extras = getIntent().getExtras();
        incomingID = extras.getString("XYZ");
        ref3 = FirebaseDatabase.getInstance().getReference("Users/");
        checkNewMessage();

        int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.tabcolor);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.menu_one);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);

        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setCurrentItem(2);
        FragmentManager fragmentM = getFragmentManager();
        FragmentTransaction fragmentTrans = fragmentM.beginTransaction();
        frag = new P_one(incomingID);
        FragmentTransaction replace = fragmentTrans.replace(R.id.fragment, frag);
        replace.commit();
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Do something cool here...
                switch (position) {
                    case 0:

                        frag = new P_three(incomingID);
                        break;
                    case 1:
                        frag = new P_two(incomingID);
                        ref3.child(incomingID).child("Messages").child("new_message").removeValue();
                        break;
                    case 2:
                        frag = new P_one(incomingID);
                        break;
                }
                FragmentTransaction replace = fragmentTransaction.replace(R.id.fragment, frag);
                replace.commit();
                return true;

            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    private void checkNewMessage() {
     listener = ref3.child(incomingID).child("Messages").child("new_message").addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             long lc = dataSnapshot.getChildrenCount();

             if(lc > 0){
                 bottomNavigation.setNotification("1", 1);
             }else{
                 bottomNavigation.setNotification("", 1);
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
    }

    @Override
    public void onBackPressed() {
        Disconnect();
    }

    private void Disconnect(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_logout);
        builder.setTitle("Déconnexion");
        builder.setMessage("Voulez Vous vraiment Déconnecter de cette Application?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ref3 != null && listener != null ){
                    ref3.removeEventListener(listener);
                    finish();
                }
            }
        });
        builder.setNegativeButton("Non", null);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

    }
}

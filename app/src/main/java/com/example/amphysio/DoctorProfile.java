package com.example.amphysio;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfile extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;
    Fragment frag;
    DatabaseReference reference;
    DatabaseReference reference1;
    TextView t1, t2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        t1 = (TextView) findViewById(R.id.doc_name);
        t2 = (TextView) findViewById(R.id.doc_speciality);

        int[] tabColors = getApplicationContext().getResources().getIntArray(R.array.tabcolor);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation_doc);
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.menu_two);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));
        reference = FirebaseDatabase.getInstance().getReference("Doctor/Messages/new_message/");
        reference1 = FirebaseDatabase.getInstance().getReference("Doctor/");
        loadMyInfos(t1, t2, reference1);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long c = dataSnapshot.getChildrenCount();
                if (c > 0) {
                    bottomNavigation.setNotification(String.valueOf(c), 1);
                } else {
                    bottomNavigation.setNotification("", 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        bottomNavigation.setCurrentItem(0);
        FragmentManager fragmentM = getFragmentManager();
        FragmentTransaction fragmentTrans = fragmentM.beginTransaction();
        frag = new s_one();
        FragmentTransaction replace = fragmentTrans.replace(R.id.frag_doc, frag);
        replace.commit();
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Do something cool here...
                switch (position) {
                    case 0:
                        frag = new s_one();
                        break;
                    case 1:
                        frag = new s_three();
                        bottomNavigation.setNotification("", 1);
                        break;
                }
                FragmentTransaction replace = fragmentTransaction.replace(R.id.frag_doc, frag);
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

    private void loadMyInfos(final TextView edName, final TextView edJob, DatabaseReference ref) {
        ref.child("Infos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("DocName").getValue().toString();
                    String job = dataSnapshot.child("DocJob").getValue().toString();
                    edName.setText(name);
                    edJob.setText(job);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Disconnect() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_logout);
        builder.setTitle("Déconnexion");
        builder.setMessage("Voulez Vous vraiment Déconnecter de cette Application?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logOut();
            }
        });
        builder.setNegativeButton("Non", null);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

    }

    private void logOut() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Deconnexion");
        pd.setMessage("Deconnexion via le serveur...");
        pd.show();
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(DoctorProfile.this, ActivityStart.class));
                //finish();
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            Disconnect();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}

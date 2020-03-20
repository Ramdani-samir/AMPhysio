package com.example.amphysio;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


@SuppressLint("ValidFragment")
public class P_one extends Fragment {


    TextView emo_state;
    ImageButton show;
    ImageView imgAlert, imgState;
    Switch sw1;
    TextView t1, t2, t3, t4;
    private String inc_id;
    DatabaseReference Ref2;
    ValueEventListener listener2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View root = inflater.inflate(R.layout.p_one, container, false);
        // views
        t1 = (TextView) root.findViewById(R.id.t1);
        t2 = (TextView) root.findViewById(R.id.t2);
        t3 = (TextView) root.findViewById(R.id.t3);
        t4 = (TextView) root.findViewById(R.id.t4);
        emo_state = (TextView) root.findViewById(R.id.emo_state);
        imgState = (ImageView) root.findViewById(R.id.img_state);
        imgAlert = (ImageView) root.findViewById(R.id.imgAlert);
        sw1 = (Switch) root.findViewById(R.id.sw1);
        show = (ImageButton) root.findViewById(R.id.show);

        // load Data
        LoadData(inc_id);
        return root;
    }

    @SuppressLint("ValidFragment")
    public P_one(String id){
        inc_id = id;
    }
    public void LoadData(final String ID) {
        Ref2 = FirebaseDatabase.getInstance().getReference("Users");
        listener2 = Ref2.child(ID).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String str = "Values";
                    String p1 = dataSnapshot.child(str).child("Param1").getValue().toString();
                    String p2 = dataSnapshot.child(str).child("Param2").getValue().toString();
                    String p3 = dataSnapshot.child(str).child("Param3").getValue().toString();
                    //String p4 = ds.child(ID).child(str).child("Param4").getValue().toString();
                    t1.setText(p1);
                    t2.setText(p2);
                    t3.setText(p3);
                    if (Integer.parseInt(p3) > 100) {
                        emo_state.setText("Negatif");
                        imgState.setImageResource(R.drawable.ic_action_sad);
                    } else {
                        emo_state.setText("Positif");
                        imgState.setImageResource(R.drawable.ic_action_happy);
                    }
                }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onStop() {
        if (Ref2 != null && listener2 != null) {
            Ref2.removeEventListener(listener2);
        }
        super.onStop();
    }
}

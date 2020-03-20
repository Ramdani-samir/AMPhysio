package com.example.amphysio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ListPatients extends AppCompatActivity {
    private FirebaseAuth Auth;
    PatientsAdapter adapter;
    public ArrayList<DataModel> list = new ArrayList<>();
    ArrayList<DataModel> searchlist = new ArrayList<>();
    public GridView lv;
    EditText search;
    DatabaseReference Ref;
    ValueEventListener listener;
    ColorDrawable[] colors = {
            new ColorDrawable(Color.RED),
            new ColorDrawable(Color.BLUE)};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_patients);
        this.Auth = FirebaseAuth.getInstance();
        lv = (GridView) findViewById(R.id.grid);
        search = (EditText) findViewById(R.id.search);
        FetchPatients();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SendIdToNextActivity(((DataModel) parent.getItemAtPosition(position)).getPatientId());
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void SendIdToNextActivity(String id) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("P_ID", id);
        startActivity(intent);
    }

    private void FetchPatients() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        Ref = FirebaseDatabase.getInstance().getReference("Users");
        listener = Ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
                if (ListPatients.this.adapter != null) {
                    ListPatients.this.list.clear();
                    ListPatients.this.adapter.notifyDataSetChanged();
                }
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String p_id = data.child("Infos").child("Id").getValue().toString();
                    String p_name = data.child("Infos").child("Username").getValue().toString();
                    String p_url = data.child("Infos").child("Url").getValue().toString();
                    String p_temp = data.child("Values").child("Param1").getValue().toString();
                    String p_bat = data.child("Values").child("Param2").getValue().toString();
                    list.add(new DataModel(p_name, p_id, p_bat, p_temp, p_url));
                }

                adapter = new PatientsAdapter(list, ListPatients.this);
                lv.setAdapter(ListPatients.this.adapter);
                adapter.notifyDataSetChanged();
                lv.invalidateViews();
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // your code.
        if (Ref != null && listener != null) {
            Ref.removeEventListener(listener);
        }
        finish();
    }
    @Override
    protected void onStop() {
        if (Ref != null && listener != null) {
            Ref.removeEventListener(listener);
        }
        super.onStop();
    }
}
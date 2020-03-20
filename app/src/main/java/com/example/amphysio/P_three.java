package com.example.amphysio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

@SuppressLint("ValidFragment")
public class P_three extends Fragment implements View.OnClickListener {

    ImageButton btn1, btn2, btn3;
    Button save;
    ImageButton disco;
    EditText ed1, ed2, ed3;
    private String _ID;
    private DatabaseReference ref;
    private ValueEventListener listener;

    @SuppressLint("ValidFragment")
    public P_three(String id) {
        _ID = id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View root = inflater.inflate(R.layout.p_three, container, false);
        //Views
        btn1 = (ImageButton) root.findViewById(R.id.p_btn1);
        btn2 = (ImageButton) root.findViewById(R.id.p_btn2);
        btn3 = (ImageButton) root.findViewById(R.id.p_btn3);
        ed1 = (EditText) root.findViewById(R.id.p_editName);
        ed2 = (EditText) root.findViewById(R.id.p_editphone);
        ed3 = (EditText) root.findViewById(R.id.p_editAge);
        save = (Button) root.findViewById(R.id.p_saveAll);
        disco = (ImageButton) root.findViewById(R.id.p_disconnect);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        save.setOnClickListener(this);
        disco.setOnClickListener(this);

        LoadPatientInfos();
        return root;
    }
    // load Patient infos
    private void LoadPatientInfos(){
        ref = FirebaseDatabase.getInstance().getReference("Users");
        listener = ref.child(_ID).child("Infos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                String name = ds.child("Username").getValue().toString();
                String Num = ds.child("Number").getValue().toString();
                String Mail = ds.child("Email").getValue().toString();

                ed1.setText(name);
                ed2.setText(Num);
                ed3.setText(Mail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.p_btn1:
                ed1.setEnabled(true);
                btn1.setEnabled(true);
                save.setEnabled(true);
                break;
            case R.id.p_btn2:
                ed2.setEnabled(true);
                btn2.setEnabled(true);
                save.setEnabled(true);
                break;
            case R.id.p_btn3:
                ed3.setEnabled(true);
                btn3.setEnabled(true);
                save.setEnabled(true);
                break;
            case R.id.p_saveAll:
                Initialize();
                SaveChanges();
                save.setEnabled(false);
                break;
            case R.id.p_disconnect:
                Disconnect();
                break;
        }
    }

    private void Disconnect(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_logout);
        builder.setTitle("Déconnexion");
        builder.setMessage("Voulez Vous vraiment Déconnecter de cette Application?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ref != null && listener != null ){
                    ref.removeEventListener(listener);
                    startActivity(new Intent(getActivity(), ActivityStart.class));
                }
            }
        });
        builder.setNegativeButton("Non", null);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();

    }
    private void SaveChanges() {
        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("Mise à jour ...");
        d.show();
        String name = ed1.getText().toString();
        String phone = ed2.getText().toString();
        String mail = ed3.getText().toString();
        HashMap map = new HashMap<String, String>();
        map.put("Username", name);
        map.put("Number", phone);
        map.put("Email", mail);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(_ID).child("Infos").updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
            d.dismiss();
            }
        });
    }

    private void Initialize() {
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        ed1.setEnabled(false);
        ed2.setEnabled(false);
        ed3.setEnabled(false);
    }
}

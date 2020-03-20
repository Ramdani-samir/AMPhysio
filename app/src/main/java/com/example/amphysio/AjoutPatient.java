package com.example.amphysio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AjoutPatient extends AppCompatActivity {

    private FirebaseAuth Auth;
    private Button np_add;
    public EditText np_age;
    public EditText np_nphone;
    public EditText np_name;
    public EditText np_fmail;

    private TextView textlogin;
    private Count countClass;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);

        Auth = FirebaseAuth.getInstance();
        np_age = (EditText) findViewById(R.id.np_age);
        np_nphone = (EditText) findViewById(R.id.np_nphone);
        np_name = (EditText) findViewById(R.id.np_name);
        np_fmail = (EditText) findViewById(R.id.np_fmail);
        np_add = (Button) findViewById(R.id.np_add);

        countClass = new Count();
        getCounter();

        np_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCounter();
                String p1 = np_name.getText().toString();
                String p2 = np_fmail.getText().toString();
                String p3 = np_age.getText().toString();
                String p4 = np_nphone.getText().toString();
                if (TextUtils.isEmpty(p1)) {
                    np_name.setError("Champs Obligatoire!");
                } else {
                    if (TextUtils.isEmpty(p2)) {
                        np_fmail.setError("Champs Obligatoire!");
                    } else {
                        if (TextUtils.isEmpty(p3)) {
                            np_age.setError("Champs Obligatoire!");
                        } else {
                            if (TextUtils.isEmpty(p4)) {
                                np_nphone.setError("Champs Obligatoire!");
                            } else {
                                AddToDataBase(p1, p2, p3, p4);
                            }
                        }
                    }
                }
                np_name.getText().clear();
                np_age.getText().clear();
                np_nphone.getText().clear();
            }
        });
    }


    public void AddToDataBase(String name, String fmail, String age, String phone) {
        String str = "0";
        final ProgressDialog d = new ProgressDialog(this);
        d.setTitle("Ajouter un patient.");
        d.setMessage("En cours d'ajout...");
        d.show();
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Total_ID");
            final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("IDs");

            final String number = Random(11);
            Map map_values = new HashMap();
            map_values.put("Param1", str);
            map_values.put("Param2", str);
            map_values.put("Param3", str);
            map_values.put("Param4", str);
            Map map = new HashMap();
            map.put("Username", name);
            map.put("Email", fmail);
            map.put("Age", age);
            map.put("Id", number);
            map.put("Url", "default");
            map.put("Number", phone);
            ref.child(number).getRef().child("Infos").setValue(map);
            ref.child(number).getRef().child("Values").setValue(map_values).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //-----------------------------
                        int c = (countClass.getCount() + 1);
                        ref1.child("Num").setValue(c);
                        String id = "id" + c;
                        ref2.child(id).setValue("" + Long.parseLong(number));
                        d.dismiss();
                        ShowIdentify(number);
                        Show("Patient ajouté !");
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void Show(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private String Random(int Number) {
        long n = (long) ((new Random().nextFloat() * 8.9999999E10f) + 1.0E10f);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(n);
        return sb.toString();
    }

    private void ShowIdentify(final String id) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);
        TextView np_id = (TextView) findViewById(R.id.np_id);
        TextView np_sc = (TextView) findViewById(R.id.np_sc);
        Button env = (Button) findViewById(R.id.np_send);

        final String g_sc = getCodeOrId(id, 1);
        final String g_id = getCodeOrId(id, 2);

        np_id.setText(g_id);
        np_sc.setText(g_sc);

        env.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = g_id + "-" + g_sc;
                String to = np_fmail.getText().toString();
                sendEmail(to, code);

            }
        });
        layout.setVisibility(View.VISIBLE);
    }

    // send gmail
    private void sendEmail(String recipiant, String code) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + recipiant));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Vos Identifiants");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "ID + CODE : " + code);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(Intent.createChooser(emailIntent, "Envoyer par..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AjoutPatient.this, "Aucune application installée.", Toast.LENGTH_SHORT).show();
        }

    }

    // get code or id
    public String getCodeOrId(String input, int pos) {
        String lastFourDigits = "";     //substring containing last 4 characters
        if (pos == 1) {
            if (input.length() > 4) {
                lastFourDigits = input.substring(input.length() - 4);
            } else {
                lastFourDigits = input;
            }
            return lastFourDigits;
        } else {
            return input.length() < 7 ? input : input.substring(0, 7);
        }
    }

    @Override
    public void onBackPressed() {
        // your code.
        finish();
    }

    private void getCounter() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Total_ID");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.exists()) {
                    String num = ds.child("Num").getValue().toString();
                    countClass.setCount(Integer.parseInt(num));
                } else {
                    countClass.setCount(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

class Count {

    private int total;

    public void setCount(int i) {
        total = i;
    }

    public int getCount() {
        return total;
    }
}
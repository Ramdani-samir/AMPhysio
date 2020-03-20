package com.example.amphysio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    String IncID = "";
    LinearLayout alert_layout;
    Button alert_ok;
    TextView emo_state;
    ImageButton show, Call, Msg;
    ImageView imgAlert, imgState;
    Switch sw1;
    TextView t1, t2, t3, t4;
    DatabaseReference Ref1;
    ValueEventListener listener1;
    Number numberC;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.IncID = getIntent().getStringExtra("P_ID");
        this.t1 = (TextView) findViewById(R.id.t1);
        this.t2 = (TextView) findViewById(R.id.t2);
        this.t3 = (TextView) findViewById(R.id.t3);
        this.t4 = (TextView) findViewById(R.id.t4);
        this.emo_state = (TextView) findViewById(R.id.emo_state);
        this.imgState = (ImageView) findViewById(R.id.img_state);
        this.imgAlert = (ImageView) findViewById(R.id.imgAlert);
        this.sw1 = (Switch) findViewById(R.id.sw1);
        this.show = (ImageButton) findViewById(R.id.show);
        this.Call =  (ImageButton) findViewById(R.id.d_call);
        this.Msg =  (ImageButton) findViewById(R.id.d_sendMsg);

        this.alert_ok = (Button) findViewById(R.id.btn_alert_ok);
        this.alert_layout = (LinearLayout) findViewById(R.id.layout_alert);
        this.sw1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LoadData(IncID);
                    sw1.setText("ID : " + IncID);
                } else {
                    MainActivity.this.Initialize();
                }
            }
        });
        this.show.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.alert_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("Param4", "1");
                FirebaseDatabase.getInstance().getReference("Users").
                        child(MainActivity.this.IncID).child("Values").updateChildren(map1);
                MainActivity.this.alert_layout.setVisibility(View.GONE);
                MainActivity.this.imgAlert.setImageResource(R.mipmap.bon_etat);
            }
        });

        Call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Call(numberC.getNum());
            }
        });
        Msg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Doc_chat_with.class);
                i.putExtra("Xavi",IncID);
                startActivity(i);
                finish();
            }
        });
    }

    private void Call(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + Long.parseLong(number)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        startActivity(callIntent);
    }
    public void Initialize() {
        String str = "0";
        t1.setText(str);
        t2.setText(str);
        t3.setText(str);
    }

    /* access modifiers changed from: private */
    public void LoadData(final String ID) {
        Ref1 = FirebaseDatabase.getInstance().getReference("Users");
        listener1 = Ref1.child(ID).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String number = dataSnapshot.child("Infos").child("Number").getValue().toString();
                numberC = new Number(number);

                //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String str = "Values";
                    String p1 = dataSnapshot.child(str).child("Param1").getValue().toString();
                    String p2 = dataSnapshot.child(str).child("Param2").getValue().toString();
                    String p3 = dataSnapshot.child(str).child("Param3").getValue().toString();
                    String p4 = dataSnapshot.child(str).child("Param4").getValue().toString();
                    MainActivity.this.t1.setText(p1);
                    MainActivity.this.t2.setText(p2);
                    MainActivity.this.t3.setText(p3);
                    if (Integer.parseInt(p3) > 100) {
                        MainActivity.this.emo_state.setText("Negatif");
                        MainActivity.this.imgState.setImageResource(R.drawable.ic_action_sad);
                    } else {
                        MainActivity.this.emo_state.setText("Positif");
                        MainActivity.this.imgState.setImageResource(R.drawable.ic_action_happy);
                    }
                    if (Integer.parseInt(p4) > 10) {
                        MainActivity.this.alert_layout.setVisibility(View.VISIBLE);
                        MainActivity.this.imgAlert.setImageResource(R.mipmap.mauvais_etat);
                    }
               // }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        // your code.
        if (Ref1 != null && listener1 != null) {
            Ref1.removeEventListener(listener1);
        }
        finish();
    }

    @Override
    protected void onStop() {
        if (Ref1 != null && listener1 != null) {
            Ref1.removeEventListener(listener1);
        }
        super.onStop();
    }

    private class Number{
        String num;
        Number(String number){
            this.num = number;
        }
        private String getNum(){
            return num;
        }
    }
}

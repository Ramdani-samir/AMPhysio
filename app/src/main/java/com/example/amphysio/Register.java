package com.example.amphysio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private FirebaseAuth Auth;
    private EditText EmailView;
    private EditText PasswordView;
    private Button RegisterView;
    private TextView textlogin;
    private EditText usernameView;
    private Spinner spinner;

    ArrayAdapter<String> adapter;
    String[] arraySpinner = new String[]{
            "Cardiologue", "Gériatre", "Gynécologue", "Infermier-ère", "Médecin", "Spécialiste", "Généraliste"
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Auth = FirebaseAuth.getInstance();
        usernameView = (EditText) findViewById(R.id.User_name);
        PasswordView = (EditText) findViewById(R.id.password);
        spinner = (Spinner)findViewById(R.id.spinner);
        textlogin = (TextView) findViewById(R.id.textLogin);
        EmailView = (EditText) findViewById(R.id.email);
        RegisterView = (Button) findViewById(R.id.Register_to_fb);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        RegisterView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String email = EmailView.getText().toString().trim();
                String password = PasswordView.getText().toString().trim();
                CreateUser(email, password);
            }
        });
        textlogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Register register = Register.this;
                register.startActivity(new Intent(register, ActivityStart.class));
            }
        });
    }

    public void CreateUser(String Email, String Password) {
        if (!isValidEmaillId(Email)) {
            EmailView.setError("invalid Email Address !");
        }
        if (TextUtils.isEmpty(Email)) {
            EmailView.setError("Enter Email address!");
        } else if (TextUtils.isEmpty(Password)) {
            PasswordView.setError("Enter valid password!");
        } else if (Password.length() < 8) {
            PasswordView.setError("Password too short, enter minimum 8 characters!");
        } else {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Registring...");
            pd.show();
            Auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                public void onComplete(Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()) {
                        AddToDataBase(Register.this.usernameView.getText().toString(),spinner.getSelectedItem().toString());
                        startActivity(new Intent(Register.this, ActivityStart.class));
                        finish();
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(Register.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Register.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isValidEmaillId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    /* access modifiers changed from: private */
    public void AddToDataBase(String name,String occupation) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctor/");
            Map map = new HashMap();
            map.put("DocName", name);
            map.put("DocJob", occupation);
            ref.child("Infos").setValue(map);
            Toast.makeText(this, "Signing Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}

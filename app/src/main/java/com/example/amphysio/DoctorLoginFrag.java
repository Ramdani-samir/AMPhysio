package com.example.amphysio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorLoginFrag extends Fragment {
    EditText email;
    Button login;
    EditText password;
    TextView register_to_fb;
    TextView reset_Password;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View root = inflater.inflate(R.layout.doctor_login_frag, container, false);
        this.email = (EditText) root.findViewById(R.id.email);
        this.password = (EditText) root.findViewById(R.id.password);
        this.login = (Button) root.findViewById(R.id.doc_login);
        this.reset_Password = (TextView) root.findViewById(R.id.doc_reset_Password);
        this.register_to_fb = (TextView) root.findViewById(R.id.textRegister);
        this.register_to_fb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DoctorLoginFrag doctorLoginFrag = DoctorLoginFrag.this;
                doctorLoginFrag.startActivity(new Intent(doctorLoginFrag.getActivity(), Register.class));
            }
        });
        this.reset_Password.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DoctorLoginFrag doctorLoginFrag = DoctorLoginFrag.this;
                doctorLoginFrag.startActivity(new Intent(doctorLoginFrag.getActivity(), Reset.class));
            }
        });
        this.login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    String Email = DoctorLoginFrag.this.email.getText().toString();
                    String Pass = DoctorLoginFrag.this.password.getText().toString();
                    if (TextUtils.isEmpty(Email)) {
                        DoctorLoginFrag.this.email.setError("l'email ne peut pas être vide !");
                    } else if (!TextUtils.isEmpty(Pass)) {
                        ProgressDialog dialog = new ProgressDialog(DoctorLoginFrag.this.getActivity());
                        dialog.setTitle("Authentication");
                        dialog.setMessage("Connexion en cours...");
                        dialog.show();
                        DoctorLoginFrag.this.login(Email, Pass, dialog);
                    } else {
                        DoctorLoginFrag.this.password.setError("Mot de passe ne peut pas être vide !");
                    }
                } catch (Exception e) {
                    DoctorLoginFrag.this.DisplayMessage(e.toString());
                }
            }
        });
        return root;
    }

    public void login(String Email, String Password, final ProgressDialog dialog) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)) {
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(getActivity(),
                    new OnCompleteListener<AuthResult>() {
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        DoctorLoginFrag.this.goToNext();
                        email.getText().clear();
                        password.getText().clear();
                        dialog.dismiss();
                        return;
                    }
                    DoctorLoginFrag.this.DisplayMessage(task.getException().toString());
                    dialog.dismiss();
                }
            });
        }
    }

    public void goToNext() {
        startActivity(new Intent(getActivity(), DoctorProfile.class));
    }

    public void DisplayMessage(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}

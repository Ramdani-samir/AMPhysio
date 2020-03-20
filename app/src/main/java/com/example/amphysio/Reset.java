package com.example.amphysio;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset extends AppCompatActivity {
    public FirebaseAuth Auth;
    private Button back;
    public EditText email;
    private Button reset;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.reset);
        this.Auth = FirebaseAuth.getInstance();
        this.reset = (Button) findViewById(R.id.button_reset);
        this.back = (Button) findViewById(R.id.button_back);
        this.email = (EditText) findViewById(R.id.inputEmail);
        this.back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Reset.this.finish();
            }
        });
        this.reset.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String IE = Reset.this.email.getText().toString().trim();
                if (TextUtils.isEmpty(IE)) {
                    Reset.this.email.setError("Enter your registered Email");
                    return;
                }
                final ProgressDialog pd = new ProgressDialog(Reset.this);
                pd.setMessage("Plaise wait while checking Authentication...");
                pd.show();
                Reset.this.Auth.sendPasswordResetEmail(IE).addOnCompleteListener(Reset.this,
                        new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            Builder builder = new Builder(Reset.this);
                            builder.setTitle("Reset Password");
                            builder.setIcon(R.mipmap.ic_successful);
                            builder.setMessage("We sent to you a link, check your Email Account and follow instructions, Please!");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Reset.this.finish();
                                }
                            });
                            builder.show();
                            return;
                        }
                        Toast.makeText(Reset.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }
}
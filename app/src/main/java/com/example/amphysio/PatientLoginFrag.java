package com.example.amphysio;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientLoginFrag extends Fragment {
    EditText ID, CODE;
    Button connexion;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {

        View root = inflater.inflate(R.layout.patient_login_frag, container, false);
        CODE = (EditText) root.findViewById(R.id.key);
        ID = (EditText) root.findViewById(R.id.identifiant);
        connexion = (Button) root.findViewById(R.id.connexion);

        // button connexion on click
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _id = ID.getText().toString();
                String _code = CODE.getText().toString();
                if (!TextUtils.isEmpty(_id)) {
                    if (!TextUtils.isEmpty(_id)) {
                        String c = _id + _code;
                        checkUser(c);
                    } else {
                        CODE.setError("Entrer un Code valide !");
                    }

                } else {
                    ID.setError("Entrer un Identifiant valide !");
                }
            }
        });
        return root;
    }

    private void checkUser(final String code) {
        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("Connexion en cours...");
        d.show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
        rootRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!TextUtils.isEmpty(code)) {
                    if (snapshot.getValue() == null) {
                        // The child doesn't exist
                        ShowAlert("Aucun compte avec cette reférence : " + code
                                + " n'a été trouvé !, merci de vérifier votre identifiant ou votre code d'accès?");
                        d.dismiss();
                    } else {
                        Intent i = new Intent(getActivity(), Patient_class.class);
                        i.putExtra("XYZ", code);
                        startActivity(i);
                        d.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Notify(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public void ShowAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Échec de la connexion.")
                .setMessage(message)
                .setNeutralButton("Compris", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

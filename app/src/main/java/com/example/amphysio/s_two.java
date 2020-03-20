package com.example.amphysio;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class s_two extends Fragment {

    private EditText user, new_mail, new_pass;
    Spinner spinner;
    private DatabaseReference reference;
    Button save;
    ArrayAdapter<String> adapter;
    CheckBox options, ch1, ch2;
    LinearLayout param_lay;
    private FirebaseUser CurrentUser;
    private FirebaseAuth mAuth;
    String[] arraySpinner = new String[]{
            "Cardiologue", "Gériatre", "Gynécologue", "Infermier-ère", "Médecin", "Spécialiste", "Généraliste"
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View root = inflater.inflate(R.layout.doc_edit_profile, container, false);
        CurrentUser = mAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Doctor/");
        user = (EditText) root.findViewById(R.id.doc_edit);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        save = (Button) root.findViewById(R.id.save);
        options = (CheckBox) root.findViewById(R.id.options);
        ch1 = (CheckBox) root.findViewById(R.id.chng_mail);
        ch2 = (CheckBox) root.findViewById(R.id.chng_pass);
        param_lay = (LinearLayout) root.findViewById(R.id.lay_params);
        new_mail = (EditText) root.findViewById(R.id.newemail);
        new_pass = (EditText) root.findViewById(R.id.newpassword);

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        loadMyInfos(user, reference);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = user.getText().toString();
                String j = spinner.getSelectedItem().toString();
                Update(n, j);
            }
        });

        options.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    param_lay.setVisibility(View.VISIBLE);
                } else {
                    param_lay.setVisibility(View.GONE);
                }
            }
        });
        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    new_mail.setEnabled(isChecked);
            }
        });
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new_pass.setEnabled(isChecked);
            }
        });
        return root;
    }

    private void Update(String name, String job) {
        final ProgressDialog d = new ProgressDialog(getActivity());
        d.setMessage("Mise à jour d'informations...");
        d.show();
        Map<String, Object> map = new HashMap<>();
        map.put("DocName", name);
        map.put("DocJob", job);
        reference.child("Infos").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                d.dismiss();
            }
        });
        if (options.isChecked()) {

            if (ch1.isChecked()) {
                String mail = new_mail.getText().toString().toLowerCase();
                if (!(mail.length() > 0)) {
                    new_mail.setError("Vérifier votre adresse mail ?");
                }

                // change mail
                CurrentUser.updateEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "mail modifié corréctement !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            if (ch2.isChecked()) {
                String newp = new_pass.getText().toString();
                if (!(newp.length() > 8)) {
                    new_pass.setError("Mot de passe Incorrecte ! ?");
                }
                // change password
                CurrentUser.updatePassword(newp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "mot de passe modifié corréctement !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }
    }

    private void loadMyInfos(final EditText edName, DatabaseReference ref) {
        ref.child("Infos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("DocName").getValue().toString();
                    //String job = dataSnapshot.child("DocJob").getValue().toString();
                    edName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

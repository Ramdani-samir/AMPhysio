package com.example.amphysio;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class s_one extends Fragment implements View.OnClickListener{
    Button EditProfile,Exit,showLst,addPatient;
    Fragment frag;
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
            View root = inflater.inflate(R.layout.doc_params, container, false);
            showLst = (Button) root.findViewById(R.id.d_showList);
            this.EditProfile = (Button) root.findViewById(R.id.d_editProfile);
            this.Exit = (Button) root.findViewById(R.id.d_exit);
            this.addPatient = (Button)root.findViewById(R.id.d_addpatient);
            this.showLst.setOnClickListener(this);
            this.EditProfile.setOnClickListener(this);
            this.Exit.setOnClickListener(this);
            this.addPatient.setOnClickListener(this);
            return root;
        }


    private void Disconnect() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Deconnexion");
        pd.setMessage("Deconnexion via le serveur...");
        pd.show();
        AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(s_one.this.getActivity(), ActivityStart.class));
                //finish();
                pd.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.d_exit :
                Disconnect();
                break;
            case R.id.d_showList :
                startActivity(new Intent(s_one.this.getActivity(), ListPatients.class));
                break;
            case R.id.d_editProfile:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                frag = new s_two();
                FragmentTransaction replace = fragmentTransaction.replace(R.id.frag_doc, frag);
                replace.commit();
                break;
            case R.id.d_addpatient:
                startActivity(new Intent(s_one.this.getActivity(), AjoutPatient.class));
                break;

        }
    }
}

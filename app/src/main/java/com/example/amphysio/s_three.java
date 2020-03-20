package com.example.amphysio;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class s_three extends Fragment {

    ListView lv;
    MessagesAdapter adapter;
    ArrayList<DataModel> lstMsg = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View root = inflater.inflate(R.layout.doc_chat_list, container, false);
        lv = (ListView) root.findViewById(R.id.list_msg);
        FetchDocMessages();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Pid = ((DataModel) parent.getItemAtPosition(position)).getPatientId();
                Intent i = new Intent(getActivity(), Doc_chat_with.class);
                i.putExtra("Xavi", Pid);
                startActivity(i);
            }
        });

        return root;
    }

    public void showPup(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    private void FetchDocMessages() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctor/Messages/new_message/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lstMsg.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //String  _name = ds.child("Name").getValue().toString();
                    String _message = ds.child("Message").getValue().toString();
                    String _id = ds.child("Id").getValue().toString();
                    //String _url = ds.child("Url").getValue().toString();
                    lstMsg.add(new DataModel(_id, _id, _message, "default"));
                }

                adapter = new MessagesAdapter(getActivity(), lstMsg);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

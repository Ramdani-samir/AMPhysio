package com.example.amphysio;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class P_two extends Fragment {

    ImageButton like, post;
    EditText Ed_msg;
    ScrollView scrollView;
    LinearLayout layout;
    DatabaseReference ref3;
    ChildEventListener listener;
    private String MY_ID;

    // constructor
    @SuppressLint("ValidFragment")
    public P_two(String id) {
        MY_ID = id;
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle icicle) {
        View root = inflater.inflate(R.layout.p_two, container, false);
        layout = (LinearLayout) root.findViewById(R.id.layout1);
        like = (ImageButton) root.findViewById(R.id.p_like);
        post = (ImageButton) root.findViewById(R.id.p_post);
        Ed_msg = (EditText) root.findViewById(R.id.p_message);
        scrollView = (ScrollView) root.findViewById(R.id.scroller1);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref1, ref2;
                String messageText = Ed_msg.getText().toString();
                String currentDateandTime = java.text.DateFormat.getDateTimeInstance().
                        format(Calendar.getInstance().getTime());
                if (!messageText.equals("")) {
                    try {
                        ref1 = FirebaseDatabase.getInstance().getReference("Doctor/Messages/");
                        ref2 = FirebaseDatabase.getInstance().getReference("Users");

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Message", messageText);
                        map.put("Id", MY_ID);
                        map.put("Time", currentDateandTime);
                        ref1.child("Current_messages").child("Doc_" + MY_ID).push().setValue(map);
                        ref2.child(MY_ID).child("Messages").child("Current_messages").push().setValue(map).addOnCompleteListener(
                                new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        Ed_msg.setText("");
                                    }
                                });
                        //----------------------------------------------------------------------
                        HashMap map1 = new HashMap<String, String>();
                        map1.put("Message", messageText);
                        map1.put("Id", MY_ID);
                        ref1.child("new_message").child(MY_ID).updateChildren(map1);
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }
            }
        });
        ref3 = FirebaseDatabase.getInstance().getReference("Users");
        listener = ref3.child(MY_ID).child("Messages").child("Current_messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    GenericTypeIndicator<Map<String, String>> genericTypeIndicator =
                            new GenericTypeIndicator<Map<String, String>>() {
                            };
                    Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                    String message = map.get("Message");
                    String userID = map.get("Id");
                    String time = map.get("Time");

                    if (userID.equals("Doc")) {
                        addMessageBox(message, time, 2);
                        GoBottom();
                    } else {
                        addMessageBox(message, time, 1);
                        GoBottom();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }

    // add Messege
    public void addMessageBox(String message, String time, int type) {

        View lay;
        TextView msgbody, timeView;
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (type == 1) {
            lay = inflater.inflate(R.layout.item_message_sent, null);
            msgbody = (TextView) lay.findViewById(R.id.item_message_body_sent);
            msgbody.setText(message);
            timeView = (TextView) lay.findViewById(R.id.item_message_date_text_view);
            timeView.setText(time);


        } else {
            lay = inflater.inflate(R.layout.item_message_received, null);
            msgbody = (TextView) lay
                    .findViewById(R.id.item_message_body_recieve);
            timeView = (TextView) lay.findViewById(R.id.item_message_date_text_view);
            timeView.setText(time);
            msgbody.setText(message);
        }

        layout.addView(lay);
    }

    public void GoBottom() {
        scrollView.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 500);
    }

    @Override
    public void onStop(){
        if(ref3 != null && listener != null){
            ref3.removeEventListener(listener);
        }
        super.onStop();
        getActivity().invalidateOptionsMenu();
    }
    @Override
    public void onResume() {
        if(ref3 != null && listener != null){
            ref3.removeEventListener(listener);
        }
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }
}

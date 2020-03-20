package com.example.amphysio;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Doc_chat_with extends AppCompatActivity {


    private String P_id;
    LinearLayout layout;
    Button sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference ref1, ref2;
    TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_chat_with);
        Firebase.setAndroidContext(this);
        this.P_id = getIntent().getStringExtra("Xavi");

        layout = (LinearLayout) findViewById(R.id.d_layout);
        sendButton = (Button) findViewById(R.id.d_post);
        messageArea = (EditText) findViewById(R.id.d_message);
        scrollView = (ScrollView) findViewById(R.id.scroller);
        title = (TextView) findViewById(R.id.d_chat_with);
        title.setText(P_id);

        ref1 = FirebaseDatabase.getInstance().getReference("Doctor/Messages/Current_messages/");
        ref2 = FirebaseDatabase.getInstance().getReference("Users/");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                String currentDateandTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Message", messageText);
                    map.put("Id", "Doc");
                    map.put("Time", currentDateandTime);
                    ref1.child("Doc_" + P_id).push().setValue(map);
                    ref2.child(P_id).child("Messages").child("Current_messages").push().setValue(map);
                    ref2.child(P_id).child("Messages").child("new_message").push().setValue(map);

                    messageArea.setText("");
                }
            }
        });

        ref1.child("Doc_" + P_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator =
                        new GenericTypeIndicator<Map<String, String>>() {
                        };
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                String message = map.get("Message");
                String userID = map.get("Id");
                String time = map.get("Time");

                if (userID.equals("Doc")) {
                    addMessageBox(message, time, 1);
                    GoBottom();
                } else {
                    addMessageBox(message, time, 2);
                    GoBottom();
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
    }


    public void addMessageBox(String message, String time, int type) {

        View lay;
        TextView msgbody, timeView;
        LayoutInflater inflater = (LayoutInflater) this
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
        //scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void GoBottom() {
        scrollView.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 500);
    }
}
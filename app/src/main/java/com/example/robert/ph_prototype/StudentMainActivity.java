package com.example.robert.ph_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentMainActivity extends Activity {
    private static String REFERENCE = "Account";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference(REFERENCE);

    private String userFullName;
    private int userId;
    private String userEmail;
    private String activities;

    public static StudentMainActivity newInstance() {
        StudentMainActivity fragment = new StudentMainActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activities = dataSnapshot.child(safeEmail(userEmail))
                        .child("activities").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("lol", "Failed to read value.", error.toException());
            }
        });

        TextView studentTitle = findViewById(R.id.student_main_title);

        userFullName = getIntent().getStringExtra("full_name");
        userId = getIntent().getIntExtra("user_id", -1);
        userEmail = getIntent().getStringExtra("user_email");
        studentTitle.setText("Welcome, "+userFullName+"\nID: "+userId+"\nEmail: "+userEmail);

        initButtons();
    }

    private void initButtons() {
        Button courseButton = (Button) findViewById(R.id.course_btn);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Filter by course", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainActivity.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.COURSE_HELP.toString());
                intent.putExtra("user_id", userId);
                intent.putExtra("activities", activities);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });

        Button selfButton = (Button) findViewById(R.id.selfdirect_btn);
        selfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Filter by self directed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainActivity.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.SELF_GUIDED.toString());
                intent.putExtra("user_id", userId);
                intent.putExtra("activities", activities);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });

        Button clubButton = (Button) findViewById(R.id.club_btn);
        clubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Filter by club", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainActivity.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.CLUB.toString());
                intent.putExtra("user_id", userId);
                intent.putExtra("activities", activities);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });

        Button allActivitiesButton = (Button) findViewById(R.id.all_activities_btn);
        allActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "See My Activities", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainActivity.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.MINE.toString());
                intent.putExtra("user_id", userId);
                intent.putExtra("activities", activities);
                intent.putExtra("user_email", userEmail);
                intent.putExtra("signup_enabled", false);
                startActivity(intent);
            }
        });
    }

    private String safeEmail(String email) {
        return email.replace("@","(AT)").replace(".","(DOT)");
    }
}
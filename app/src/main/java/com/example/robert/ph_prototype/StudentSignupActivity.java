package com.example.robert.ph_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class StudentSignupActivity extends AppCompatActivity {
    private static String REFERENCE = "ActivityCollection";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference(REFERENCE);

    ScheduleItemCard currentItem;

    private String activityID;
    private int userId;

    TextView text;
    Button goBackButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        Intent i = getIntent();
        userId = i.getIntExtra("user_id", -1);
        currentItem = (ScheduleItemCard) i.getParcelableExtra("parcelable_item");
        activityID = i.getStringExtra("activity_id");

        text = findViewById(R.id.signup_tv);
        text.setText(currentItem.getName());

        StringBuffer stringBuffer = new StringBuffer("Details: \n");
        stringBuffer.append("Type: "+currentItem.getType()+"\n");
        stringBuffer.append("Room Number: "+currentItem.getRoom()+"\n");
        stringBuffer.append("Time: "+currentItem.getTime()+"\n");
        stringBuffer.append("Duration: "+currentItem.getDuration()+"\n");
        stringBuffer.append("Capacity: "+currentItem.getCapacity()+"\n");
        stringBuffer.append("Teacher: "+currentItem.getTeacher()+"\n");
        stringBuffer.append("Students: "+currentItem.getStudents());
        stringBuffer.append("\n\nActivity ID: "+activityID);

        text = findViewById(R.id.detail_tv);
        text.setText(stringBuffer.toString());

        initButtons();
    }

    private void initButtons() {
        goBackButton = (Button) findViewById(R.id.go_back_btn);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signUpButton = (Button) findViewById(R.id.signup_btn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Signing Up...", Toast.LENGTH_SHORT).show();
                int currentCapacity = Integer.valueOf(currentItem.getCapacity().split("/")[0]);
                int maxCapacity = Integer.valueOf(currentItem.getCapacity().split("/")[1]);

                if (currentCapacity+1 > maxCapacity) {
                    Toast.makeText(getApplicationContext(), "Sucks to suck", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    mRootReference.child(activityID)
                            .child("capacity").setValue((currentCapacity+1)+"/"+maxCapacity);
                    mRootReference.child(activityID)
                            .child("students").setValue(currentItem.getStudents()+" "+userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

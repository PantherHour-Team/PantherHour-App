package com.example.robert.ph_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentSignupActivity extends AppCompatActivity {
    private static String REFERENCE = "ActivityCollection";
    private static String REFERENCE_2 = "Account";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference(REFERENCE);
    DatabaseReference mRootReference2 = firebaseDatabase.getReference(REFERENCE_2);

    ActivityModel currentItem;

    private String activityID;
    private String activities;
    private String userEmail;
    private int userId;
    private boolean signupEnabled;

    TextView text;
    Button goBackButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        Intent i = getIntent();
        userId = i.getIntExtra("user_id", -1);
        currentItem = (ActivityModel) i.getParcelableExtra("parcelable_item");
        activityID = i.getStringExtra("activity_id");
        activities = i.getStringExtra("activities");
        userEmail = i.getStringExtra("user_email");
        signupEnabled = i.getBooleanExtra("signup_enabled", true);
        text = findViewById(R.id.signup_tv);
        text.setText(currentItem.getName());

        StringBuffer stringBuffer = new StringBuffer("Details: \n");
        stringBuffer.append("Type: "+currentItem.getType()+"\n");
        stringBuffer.append("Room Number: "+currentItem.getRoom()+"\n");
        stringBuffer.append("Time Frame: "+currentItem.getTimeFrame()+"\n");
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
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        signUpButton = (Button) findViewById(R.id.signup_btn);
        if (signupEnabled) {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentCapacity = Integer.valueOf(currentItem.getCapacity().split("/")[0]);
                    int maxCapacity = Integer.valueOf(currentItem.getCapacity().split("/")[1]);

                    if (currentCapacity+1 > maxCapacity) {
                        Toast.makeText(getApplicationContext(),
                                "Activity has reached max capacity",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Log.d("lol", activityID);
                    if (!activities.equals("") && activities.contains(activityID)) {
                        Toast.makeText(getApplicationContext(),
                                "Already signed up for this activity (dummy)",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        mRootReference.child(activityID)
                                .child("capacity").setValue((currentCapacity+1)+"/"+maxCapacity);
                        mRootReference.child(activityID)
                                .child("students")
                                .setValue((currentItem.getStudents().equals(""))
                                        ? String.valueOf(userId) : currentItem.getStudents()+" "+userId);
                        mRootReference2.child(safeEmail(userEmail))
                                .child("activities")
                                .setValue((activities.equals(""))
                                        ? activityID : activities+" "+activityID);
                        Toast.makeText(getApplicationContext(), "Activity Added!", Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            signUpButton.setVisibility(View.INVISIBLE);
        }
    }

    private String safeEmail(String email) {
        return email.replace("@","(AT)").replace(".","(DOT)");
    }
}

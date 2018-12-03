package com.example.robert.ph_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentSignupActivity extends AppCompatActivity {
    private static String REFERENCE = "ActivityCollection";
    private static String REFERENCE_2 = "Account";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference(REFERENCE);
    DatabaseReference mRootReference2 = firebaseDatabase.getReference(REFERENCE_2);

    HashMap<String, HashMap<String, String>> allActivities;

    ActivityModel currentItem;

    private String activityID;
    private String activities;
    private String userEmail;
    private int userId;
    private boolean signupEnabled;

    TextView title;
    TextView description;
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

        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allActivities =
                        (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                Log.d("lol", "List of activities: " + allActivities);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("lol", "Failed to read value.", error.toException());
            }
        });

        title = findViewById(R.id.card_title);
        title.setText(currentItem.getName());

        // Coloring scheme
        FrameLayout card = findViewById(R.id.title_card_holder);
        switch (ActivityModel.Type.valueOf(currentItem.getType())) {
            case COURSE_HELP:
                card.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.rounded_corners_dkgray));
                break;
            case SELF_GUIDED:
                card.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.rounded_corners_blue));
                break;
            case CLUB:
                card.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.rounded_corners_magenta));
                break;
        }

        StringBuffer stringBuffer = new StringBuffer("Details\n\n");
        stringBuffer.append("Type: "+currentItem.getType()+"\n");
        stringBuffer.append("Room Number: "+currentItem.getRoom()+"\n");
        stringBuffer.append("Time Frame: "+currentItem.getTimeFrame()+"\n");
        stringBuffer.append("Capacity: "+currentItem.getCapacity()+"\n");
        stringBuffer.append("Teacher: "+currentItem.getTeacher()+"\n");
        stringBuffer.append("Students: "+currentItem.getStudents());
        stringBuffer.append("\n\nActivity ID: "+activityID);

        description = findViewById(R.id.description);
        description.setText(stringBuffer.toString());

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

                    if (allActivities == null) {
                        Toast.makeText(getApplicationContext(),
                                "Database Error",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

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

                    // Comment this check out if needed
                    if (checkUserScheduleConflict()) {
                        Toast.makeText(getApplicationContext(),
                                "This activity conflicts with another activity in your schedule",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        mRootReference.child(activityID)
                                .child("capacity").setValue((currentCapacity+1)+"/"+maxCapacity);
                        mRootReference.child(activityID)
                                .child("students")
                                .setValue((currentItem.getStudents() == null || currentItem.getStudents().equals(""))
                                        ? String.valueOf(userId) : currentItem.getStudents()+" "+userId);
                        mRootReference2.child(safeEmail(userEmail))
                                .child("activities")
                                .setValue((activities == null || activities.equals(""))
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

    private boolean checkUserScheduleConflict() {
        if (activities == null || activities.equals(""))
            return false;

        String currTimeFrame = allActivities.get(activityID).get("time_frame");
        for (String userActivityId : activities.split(" ")) {
            String userTimeFrame = allActivities.get(userActivityId).get("time_frame");
            if (isOverlap(userTimeFrame, currTimeFrame)) {
                return true;
            }
        }
        return false;
    }

    private boolean isOverlap(String timeFrameA, String timeFrameB) {
        // Logic: start1.before(end2) && start2.before(end1);

        String[] frameA = timeFrameA.split(" ");
        String start1 = frameA[0]+frameA[1];
        String end1 = frameA[2]+frameA[3];

        String[] frameB = timeFrameA.split(" ");
        String start2 = frameB[0]+frameB[1];
        String end2 = frameB[2]+frameB[3];

        Log.d("lol", "Comparing: "+timeFrameA+" and "+timeFrameB);
        return start1.compareTo(end2) < 0 && start2.compareTo(end1) < 0;
    }

    private String safeEmail(String email) {
        return email.replace("@","(AT)").replace(".","(DOT)");
    }
}

package com.example.robert.ph_prototype;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;


public class TeacherEditActivity extends AppCompatActivity {

    private EditText capacity;
    private EditText duration;
    private EditText name;
    private EditText room;
    private EditText students;
    private EditText teacher;
    private EditText time;
    private RadioGroup type;

    private Button cancelButton;
    private Button submitButton;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_edit_activity);

        final ActivityObject act = new ActivityObject();

        capacity = findViewById(R.id.capacityBox);
        duration = findViewById(R.id.durationBox);
        name = findViewById(R.id.activityNameText);
        room = findViewById(R.id.roomNumberBox);
//        students = findViewById(R.id.);
        teacher = findViewById(R.id.teacherNameText);
        time = findViewById(R.id.timeSlotText);
        type = findViewById(R.id.radioGroup);


        cancelButton = findViewById(R.id.cancelButton);
        submitButton = findViewById(R.id.updateButton);


        submitButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                act.setCapacity(capacity.getText().toString());
//                act.setDuration(teacherName.getText().toString());
                act.setName(name.getText().toString());
                act.setRoom(room.getText().toString());
//                act.setStudents(students.getText().toString());
                act.setTeacher(teacher.getText().toString());

                if(type.getCheckedRadioButtonId() == R.id.clubRadio )
                    act.setType("CLUB");
                 else if(type.getCheckedRadioButtonId() == R.id.selfRadio)
                    act.setType("SELF_GUIDED");
                else
                    act.setType("COURSE_HELP");

                act.updateRoom();
                Toast.makeText(getApplicationContext(),"Updated room successfully",Toast.LENGTH_LONG).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(room.getText() != null || room.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(),"Reloaded room " + room.getText(),Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(),"Please input a room number",Toast.LENGTH_LONG).show();
                }
            }
        });

    }




}

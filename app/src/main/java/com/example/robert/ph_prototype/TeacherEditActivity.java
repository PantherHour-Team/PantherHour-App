package com.example.robert.ph_prototype;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import java.util.Calendar;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;


import com.google.firebase.database.FirebaseDatabase;


public class TeacherEditActivity extends AppCompatActivity {

    private EditText capacity;
    private EditText name;
    private EditText room;
    private EditText teacher;
    private EditText startTime;
    private EditText endTime;
    private RadioGroup type;
    private RadioButton clubRadio;
    private RadioButton selfRadio;
    private RadioButton courseRadio;

    private Button cancelButton;
    private Button submitButton;


    private String act_id;
    private String students;
    private ActivityModel act;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_edit_activity);
        boolean isAdd = getIntent().getBooleanExtra("add_activity", false);

        // Get the parameters from intent
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            act_id = bundle.getString("id");
            act = loadAct(act_id);
        } else {
            act = new ActivityModel();
            students = "";
        }


        capacity = findViewById(R.id.capacityBox);
        name = findViewById(R.id.activityNameText);
        room = findViewById(R.id.roomNumberBox);
        teacher = findViewById(R.id.teacherNameText);
        startTime = findViewById(R.id.startText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startTime.setShowSoftInputOnFocus(false);
        }
        endTime = findViewById(R.id.endText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            endTime.setShowSoftInputOnFocus(false);
        }
        // Add the end time here....

        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TeacherEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.append(" " + selectedHour + ":" + selectedMinute + ":" + "00");
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        startTime.setText("");
                        Toast.makeText(TeacherEditActivity.this,"Input cancelled by user",Toast.LENGTH_LONG).show();
                    }
                });

                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(TeacherEditActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        startTime.setText("" + selectedmonth + "-" + selectedday + "-" + selectedyear);
                        mTimePicker.show();
                    }


                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");

                mDatePicker.show();
            }
        });


        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                final TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TeacherEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.append(" " + selectedHour + ":" + selectedMinute + ":" + "00");
                    }

                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");

                mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        endTime.setText("");
                        Toast.makeText(TeacherEditActivity.this,"Input cancelled by user",Toast.LENGTH_LONG).show();
                    }
                });

                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(TeacherEditActivity.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        endTime.setText("" + selectedmonth + "-" + selectedday + "-" + selectedyear);
                        mTimePicker.show();
                    }


                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");

                mDatePicker.show();
            }
        });


        type = findViewById(R.id.radioGroup);

        cancelButton = findViewById(R.id.cancelButton);
        submitButton = findViewById(R.id.updateButton);

        // Pre-populate if the teacher selected a card
        if (isAdd) {
            ((TextView) findViewById(R.id.title)).setText("Add New Activity");
        } else {
            ActivityModel currentItem =
                    (ActivityModel) getIntent().getParcelableExtra("parcelable_item");

            name.setText(currentItem.getName());
            room.setText(currentItem.getRoom());
            teacher.setText(currentItem.getTeacher());
            capacity.setText(currentItem.getCapacity());
            time.setText(currentItem.getTimeFrame());
            switch (ActivityModel.Type.valueOf(currentItem.getType())) {
                case CLUB:
                    type.check(R.id.clubRadio);
                    break;
                case SELF_GUIDED:
                    type.check(R.id.selfRadio);
                    break;
                case COURSE_HELP:
                    type.check(R.id.courseRadio);
                    break;
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!room.getText().toString().equals("") && !room.getText().toString().isEmpty()) {

                    String timeSlot = startTime.getText().toString() + " " + endTime.getText().toString();

                    act = new ActivityModel(name.getText().toString(), "NULL", room.getText().toString(), teacher.getText().toString(), timeSlot, "", capacity.getText().toString());

                    if (type.getCheckedRadioButtonId() == R.id.clubRadio)
                        act.setType("CLUB");
                    else if (type.getCheckedRadioButtonId() == R.id.selfRadio)
                        act.setType("SELF_GUIDED");
                    else
                        act.setType("COURSE_HELP");

                    act.setStudents(students);

                    act.submitActivity();
                    Toast.makeText(getApplicationContext(), "Updated activity successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill out the form before submitting", Toast.LENGTH_LONG).show();
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Changes were not saved", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }


    public ActivityModel loadAct(String id) {

        capacity = findViewById(R.id.capacityBox);
        name = findViewById(R.id.activityNameText);
        room = findViewById(R.id.roomNumberBox);
        teacher = findViewById(R.id.teacherNameText);
        startTime = findViewById(R.id.startText);
        endTime = findViewById(R.id.endText);

        clubRadio = findViewById(R.id.clubRadio);
        courseRadio = findViewById(R.id.courseRadio);
        selfRadio = findViewById(R.id.selfRadio);

        ActivityModel actTemp = new ActivityModel();
        actTemp.fetchActivity(id);

        if (actTemp.getRoom() != null) {
            capacity.setText(actTemp.getCapacity());
            name.setText(actTemp.getName());
            room.setText(actTemp.getRoom());
            teacher.setText(actTemp.getTeacher());

            // Need to parse here
            String start = actTemp.getTimeFrame().substring(0,actTemp.getTimeFrame().length()/2);
            String end = actTemp.getTimeFrame().substring(actTemp.getTimeFrame().length()/2);

            startTime.setText(start);
            endTime.setText(end);

            if (actTemp.getType().equals("CLUB"))
                clubRadio.setChecked(true);
            else if (actTemp.getType().equals("SELF_GUIDED"))
                selfRadio.setChecked(true);
            else
                courseRadio.setChecked(true);

            students = actTemp.getStudents();
        }

        return actTemp;
    }


}

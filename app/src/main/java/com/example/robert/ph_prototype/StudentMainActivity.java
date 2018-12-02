package com.example.robert.ph_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class StudentMainTabFragment extends Activity {
    private String userFullName;
    private int userId;

    public static StudentMainTabFragment newInstance() {
        StudentMainTabFragment fragment = new StudentMainTabFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(LayoutInflater inflater, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.student_main_tab, container, false);
        TextView studentTitle = view.findViewById(R.id.student_main_title);

        userFullName = getIntent().getStringExtra("full_name");
        userId = getIntent().getIntExtra("user_id", -1);
        studentTitle.setText("Welcome, "+userFullName+ " ID: "+userId);
        initButtons(view);
        return view;
    }

    private void initButtons(View v) {
        Button courseButton = (Button) v.findViewById(R.id.course_btn);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Filter by course", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainTabFragment.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.COURSE_HELP.toString());
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        Button selfButton = (Button) v.findViewById(R.id.selfdirect_btn);
        selfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Filter by self directed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainTabFragment.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.SELF_GUIDED.toString());
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        Button clubButton = (Button) v.findViewById(R.id.club_btn);
        clubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Filter by club", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainTabFragment.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.CLUB.toString());
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        Button allActivitiesButton = (Button) v.findViewById(R.id.all_activities_btn);
        allActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "See All Activities", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StudentMainTabFragment.this, StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.ALL.toString());
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });
    }
}
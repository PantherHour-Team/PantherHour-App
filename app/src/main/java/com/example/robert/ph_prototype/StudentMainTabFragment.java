package com.example.robert.ph_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class StudentMainTabFragment extends Fragment {

    public static StudentMainTabFragment newInstance() {
        StudentMainTabFragment fragment = new StudentMainTabFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_main_tab, container, false);
        initButtons(view);
        return view;
    }

    private void initButtons(View v) {
        Button courseButton = (Button) v.findViewById(R.id.course_btn);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter by course", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.COURSE_HELP.toString());
                startActivity(intent);
            }
        });

        Button selfButton = (Button) v.findViewById(R.id.selfdirect_btn);
        selfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter by self directed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.SELF_GUIDED.toString());
                startActivity(intent);
            }
        });

        Button clubButton = (Button) v.findViewById(R.id.club_btn);
        clubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter by club", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.CLUB.toString());
                startActivity(intent);
            }
        });

        Button allActivitiesButton = (Button) v.findViewById(R.id.all_activities_btn);
        allActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "See All Activities", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), StudentSchedulerActivity.class);
                intent.putExtra("FILTER", ScheduleItemCard.Type.ALL.toString());
                startActivity(intent);
            }
        });
    }
}
package com.example.robert.ph_prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TabOneFragment extends Fragment {

    public static TabOneFragment newInstance() {
        TabOneFragment fragment = new TabOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_1, container, false);
        initializeButtons(view);
        return view;
    }

    private void initializeButtons(View v) {
        Button courseButton = (Button) v.findViewById(R.id.course_btn);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter by course", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), SchedulerActivity.class);
                startActivity(intent);
            }
        });

        Button selfButton = (Button) v.findViewById(R.id.selfdirect_btn);
        selfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter by self directed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), SchedulerActivity.class);
                startActivity(intent);
            }
        });

        Button clubButton = (Button) v.findViewById(R.id.club_btn);
        clubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Filter by club", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), SchedulerActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.robert.ph_prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


public class ManageRoomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView className;
    private TextView teacherName;
    private EditText roomNumber;
    private TextView timeSlot;
    private TextView clubName;
    private TextView activitysName;
    private ToggleButton availableBtn;
    private Button cancelButton;
    private Button submitButton;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();



    public ManageRoomFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ManageRoomFragment newInstance() {
        ManageRoomFragment fragment = new ManageRoomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tempView = inflater.inflate(R.layout.fragment_manage_room, container, false);

        final RoomClass room = new RoomClass();

        className = tempView.findViewById(R.id.classNameText);
        teacherName = tempView.findViewById(R.id.teacherNameText);
        roomNumber = tempView.findViewById(R.id.roomNumberBox);
        timeSlot = tempView.findViewById(R.id.timeSlotText);
        clubName = tempView.findViewById(R.id.clubNameText);
        activitysName = tempView.findViewById(R.id.activityNameText);
        availableBtn = tempView.findViewById(R.id.availableButton);
        submitButton = tempView.findViewById(R.id.updateButton);
        cancelButton = tempView.findViewById(R.id.cancelButton);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room.setclass_name(className.getText().toString());
                room.setteacher_name(teacherName.getText().toString());
                room.setroom_number(roomNumber.getText().toString());
                room.settime_slot(timeSlot.getText().toString());
                room.setclub_name(clubName.getText().toString());
                room.setactivity_name(activitysName.getText().toString());

                if (availableBtn.isChecked())
                    room.setcurrently_available("true");
                else
                    room.setcurrently_available("false");

                room.updateRoom();



                Toast.makeText(getContext(),"Updated room successfully",Toast.LENGTH_LONG).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(roomNumber.getText() != null || roomNumber.getText().toString().equals("")) {
                    room.setroom_number(roomNumber.getText().toString());
                    room.getRoom();

                    if(room == null) {
                        Toast.makeText(getContext(),"Failed to load room",Toast.LENGTH_LONG).show();
                        return;
                    }

                    className.setText(room.getclass_name());
                    teacherName.setText(room.getteacher_name());
                    roomNumber.setText(room.getroom_number());
                    timeSlot.setText(room.gettime_slot());
                    clubName.setText(room.getclub_name());
                    activitysName.setText(room.getactivity_name());
                    availableBtn.setChecked(new Boolean(room.getcurrently_available()));

                    Toast.makeText(getContext(),"Reloaded room " + room.getroom_number(),Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(),"Please input a room number",Toast.LENGTH_LONG).show();
                }
            }
        });






        return tempView;
    }


}

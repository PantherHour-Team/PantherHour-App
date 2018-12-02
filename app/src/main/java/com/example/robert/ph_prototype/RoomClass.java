package com.example.robert.ph_prototype;

import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RoomClass {


    private String activity_name;
    private String currently_available;
    private String class_name;
    private String club_name;
    private String room_number;
    private String teacher_name;
    private String time_slot;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();






    public RoomClass() {
        this.activity_name = null;
        this.currently_available = null;
        this.class_name = null;
        this.club_name = null;
        this.room_number = null;
        this.teacher_name = null;
        this.time_slot = null;
    }


    // Fire base Methods


    // Retrieves a room from the database
    public RoomClass getRoom() {
        if(room_number == null)
            return null;

        DatabaseReference roomObject = firebaseDatabase.getReference("Room").child(room_number);



        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (!dataSnapshot.exists())
                    return;

                RoomClass post = dataSnapshot.getValue(RoomClass.class);

                // [START_EXCLUDE]
                activity_name = post.activity_name;
                currently_available = post.currently_available;
                class_name = post.class_name;
                club_name = post.club_name;
                room_number = post.room_number;
                teacher_name = post.teacher_name;
                time_slot = post.time_slot;

                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        roomObject.addListenerForSingleValueEvent(postListener);


        return this;
    }


    // Updates and also adds a room if it does not exist.
    public void updateRoom() {
        Log.i("RoomClass", "updateRoom: fuction called");



        //Overwriting a room has the same effect as updating it...
        firebaseDatabase.getReference("Room").child(room_number).setValue(this);
    }


    // Setter methods
    public void setactivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public void setclass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setroom_number(String room_number) {
        this.room_number = room_number;
    }

    public void setteacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public void settime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public void setcurrently_available(String currently_available) {
        this.currently_available = currently_available;
    }

    public void setclub_name(String club_name) {
        this.club_name = club_name;
    }

    // Getter methods

    public String getactivity_name() {
        return activity_name;
    }

    public String getclass_name() {
        return class_name;
    }

    public String getclub_name() {
        return club_name;
    }

    public String getcurrently_available() {
        return currently_available;
    }

    public String getroom_number() {
        return room_number;
    }

    public String getteacher_name() {
        return teacher_name;
    }

    public String gettime_slot() {
        return time_slot;
    }


}

package com.example.robert.ph_prototype;

import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ActivityObject {


    private String capacity;
    private String name;
    private String room;
    private String students;
    private String teacher;
    private String time_frame;
    //    private String date_time;
    private String type;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public ActivityObject() {
        capacity = null;
        time_frame = null;
        name = null;
        room = null;
        students = null;
        teacher = null;
        type = null;
    }


    // Fire base Methods


    // Retrieves a room from the database
    public ActivityObject getRoom() {

        if (room == null)
            return null;

        DatabaseReference roomObject = firebaseDatabase.getReference("Rooms").child(room);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI
                ActivityObject post = dataSnapshot.getValue(ActivityObject.class);

                // [START_EXCLUDE]
                capacity = post.capacity;
                time_frame = post.time_frame;
                name = post.name;
                room = post.room;
                students = post.students;
                teacher = post.teacher;
//                time = post.time;
                type = post.type;

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
        Log.i("ActivityObject", "updateRoom: fuction called");

        //Overwriting a room has the same effect as updating it...
        firebaseDatabase.getReference("Rooms").child(room).setValue(this);
    }


    // Getters

    public String getCapacity() {
        return capacity;
    }

    public String getTime_frame() {
        return time_frame;
    }

    public String getName() {
        return name;
    }

    public String getStudents() {
        return students;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getType() {
        return type;
    }


    // Setters:

    public void setName(String name) {
        this.name = name;
    }

    public void setTime_frame(String time_frame) {
        this.time_frame = time_frame;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setType(String type) {
        this.type = type;
    }

}

package com.example.robert.ph_prototype;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityModel implements Parcelable {

    enum Type
    {
        COURSE_HELP, SELF_GUIDED, CLUB, MINE
    }

    private String name;
    private String type;
    private String room;
    private String teacher;
    private String timeFrame;
    private String students;
    private String capacity;

    //Added for editActivity
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    public ActivityModel() {
        this.name = null;
        this.type = null;
        this.room = null;
        this.teacher = null;
        this.timeFrame = null;
        this.students = null;
        this.capacity = null;
    }

    public ActivityModel(String name, String type, String room, String teacher, String timeFrame,
                         String students, String capacity) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.teacher = teacher;
        this.timeFrame = timeFrame;
        this.students = students;
        this.capacity = capacity;
    }

    // example constructor that takes a Parcel and gives the object
    private ActivityModel(Parcel in) {
        this.name = in.readString();
        this.type =  in.readString();
        this.room =  in.readString();
        this.teacher =  in.readString();
        this.timeFrame =  in.readString();
        this.students =  in.readString();
        this.capacity = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public String getStudents() {
        return students;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getStartDate() {
        return timeFrame.split(" ")[0];
    }

    public String getStartTime() {
        return timeFrame.split(" ")[1];
    }

    public String getEndDate(){
        return timeFrame.split(" ")[2];
    }

    public String getEndTime(){
        return timeFrame.split(" ")[3];
    }

    /* everything below here is for implementing Parcelable */

    @Override
    public int describeContents() {
        return 0;
    }

    // write the object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.name);
        out.writeString(this.type);
        out.writeString(this.room);
        out.writeString(this.teacher);
        out.writeString(this.timeFrame);
        out.writeString(this.students);
        out.writeString(this.capacity);
    }

    // this is used to regenerate the schedule item object
    public static final Parcelable.Creator<ActivityModel> CREATOR = new Parcelable.Creator<ActivityModel>() {
        public ActivityModel createFromParcel(Parcel in) {
            return new ActivityModel(in);
        }

        public ActivityModel[] newArray(int size) {
            return new ActivityModel[size];
        }
    };

    // Firebase Methods


    public void setType(String type) {
        this.type = type;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public void fetchActivity(String id) {

        if(id == null || id.equals(""))
            return;

        // Check if it exists first?

        DatabaseReference activityRef = firebaseDatabase.getReference("ActivityCollection").child(id);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI
                ActivityModel post = dataSnapshot.getValue(ActivityModel.class);

                // [START_EXCLUDE]
                setName(post.name);
                setType(post.type);
                setRoom(post.room);
                setTeacher(post.teacher);
                setTimeFrame(post.timeFrame);
                setStudents(post.students);
                setCapacity(post.capacity);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        activityRef.addListenerForSingleValueEvent(postListener);
    }

    public void submitActivity() {
        String id = (this.getName() + this.getRoom()).replace(" ", "_");
        DatabaseReference ref = firebaseDatabase.getReference("ActivityCollection").child(id);

        ref.child("name").setValue(this.getName());
        ref.child("capacity").setValue("0/"+this.getCapacity());
        ref.child("room").setValue(this.getRoom());
        ref.child("students").setValue(this.getStudents());
        ref.child("teacher").setValue(this.getTeacher());
        ref.child("time_frame").setValue(this.getTimeFrame());
        ref.child("type").setValue(this.getType());

        //update room
        DatabaseReference ref2 = firebaseDatabase.getReference("Rooms");
        ref2.child(this.getRoom()).setValue(1);
    }

}


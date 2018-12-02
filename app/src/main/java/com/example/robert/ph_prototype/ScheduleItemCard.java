package com.example.robert.ph_prototype;

import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleItemCard implements Parcelable {

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

    public ScheduleItemCard(String name, String type, String room, String teacher, String timeFrame,
                            String duration, String students, String capacity) {
        this.name = name;
        this.type = type;
        this.room = room;
        this.teacher = teacher;
        this.timeFrame = timeFrame;
        this.students = students;
        this.capacity = capacity;
    }

    // example constructor that takes a Parcel and gives the object
    private ScheduleItemCard(Parcel in) {
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
    public static final Parcelable.Creator<ScheduleItemCard> CREATOR = new Parcelable.Creator<ScheduleItemCard>() {
        public ScheduleItemCard createFromParcel(Parcel in) {
            return new ScheduleItemCard(in);
        }

        public ScheduleItemCard[] newArray(int size) {
            return new ScheduleItemCard[size];
        }
    };

}
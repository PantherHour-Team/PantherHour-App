package com.example.robert.ph_prototype;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountModel implements Parcelable {

    private String name;
    private String email;
    private String id;
    private String activities;
    private String rooms;

    private boolean isTeacher;

    public AccountModel(String name, String email, String id, String activities, boolean isTeacher,
                        String rooms) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.activities = activities;
        this.isTeacher = isTeacher;
        this.rooms = rooms;
    }

    // example constructor that takes a Parcel and gives the object
    private AccountModel(Parcel in) {
        this.name = in.readString();
        this.email =  in.readString();
        this.id =  in.readString();
        this.activities =  in.readString();
        this.isTeacher = Boolean.valueOf(in.readString());
        this.rooms = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getActivities() {
        return activities;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public boolean isActive() {
        return activities != null && !activities.isEmpty();
    }

    public String getRooms() {
        return rooms;
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
        out.writeString(this.email);
        out.writeString(this.id);
        out.writeString(this.activities);
        out.writeString(String.valueOf(this.isTeacher));
        out.writeString(this.rooms);
    }

    // this is used to regenerate the schedule item object
    public static final Creator<AccountModel> CREATOR = new Creator<AccountModel>() {
        public AccountModel createFromParcel(Parcel in) {
            return new AccountModel(in);
        }

        public AccountModel[] newArray(int size) {
            return new AccountModel[size];
        }
    };

}
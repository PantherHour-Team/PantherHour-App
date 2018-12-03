package com.example.robert.ph_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeacherManageActivity extends AppCompatActivity {

    private static final String TAG = "lol";

    private static String REFERENCE = "ActivityCollection";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference(REFERENCE);

    HashMap<String, HashMap<String, String>> allActivities;
    HashMap<String, String> activityIds = new HashMap<>();

    private ActivityModelArrayAdapter activityModelArrayAdapter;
    private ListView listView;

    private String currRoomFilter = "All Rooms";
    private String currTimeFilter = "All Time Slots";
    private String currTypeFilter = "All Activity Types";

    private Set<String> allRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_manage_activity);
        Intent i = getIntent();

        allRooms = new HashSet<>();
        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allActivities =
                        (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                parseActivityData(activityModelArrayAdapter);
                Log.d(TAG, "List of activities: " + allActivities);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        initScheduleListView();
        initDropdownFilters();
        initFAB();
    }

    private void initScheduleListView() {

        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityModel selectedItem = (ActivityModel) parent.getItemAtPosition(position);
                Intent intent = new Intent(TeacherManageActivity.this, TeacherEditActivity.class);
                intent.putExtra("parcelable_item", (Parcelable) selectedItem);
                intent.putExtra("id", activityIds.get(selectedItem.getName()));
                startActivityForResult(intent, 1);
            }
        });

        activityModelArrayAdapter = new ActivityModelArrayAdapter(getApplicationContext(), R.layout.schedule_item_card);
        listView.setAdapter(activityModelArrayAdapter);
    }

    //  {Activity1={Type=Club, Students=<Nick Shinn, Austin Le>, Capacity=100, Teacher=Mr. Bboy, Time Frame="", Room=222, Name=BboyClub}}
    private void parseActivityData(ActivityModelArrayAdapter activityModelArrayAdapter) {
        Log.d(TAG, "Parsing Activity Data");
        if (allActivities == null || activityModelArrayAdapter == null) {
            Log.d(TAG, "null activities or adapter");
            return;
        } else {
            activityModelArrayAdapter.reset();
            activityIds.clear();
        }

        // Display all activities for teacher
        for (String activity : allActivities.keySet()) {
            HashMap<String, String> fields = (HashMap<String, String>) allActivities.get(activity);
            Log.d(TAG, fields.toString());

            String name = fields.get("name");
            String type = fields.get("type");
            String room = fields.get("room");
            String teacher = fields.get("teacher");
            String timeFrame = fields.get("time_frame");
            String students = fields.get("students");
            String capacity = fields.get("capacity");

            ActivityModel newActivity =
                    new ActivityModel(name, type, room, teacher, timeFrame, students, capacity);
            activityModelArrayAdapter.add(newActivity);
            allRooms.add(room);
            activityIds.put(name, activity);
        }
        // Reinitialize room filter to populate with all rooms
        initRoomFilter();
    }

    private void initFAB() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), TeacherEditActivity.class);
                newIntent.putExtra("add_activity", true);
                startActivityForResult(newIntent, 1);
            }
        });
    }

    private void initDropdownFilters() {
        initTimeFilter();
        initRoomFilter();
        initTypeFilter();
    }

    private void initRoomFilter() {
        // Set up room filter
        Spinner roomFilter = findViewById(R.id.room_filter);
        roomFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                currRoomFilter = selectedItem;
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Populate room filter options with available choices
        List<String> rooms = new ArrayList<>();
        rooms.add("All Rooms");
        rooms.addAll(allRooms);
        ArrayAdapter<String> roomFilterAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rooms);
        roomFilter.setAdapter(roomFilterAdapter);
    }

    private void initTimeFilter() {
        // Set up time filter
        Spinner timeFilter = findViewById(R.id.time_filter);
        timeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                currTimeFilter = selectedItem;
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        String[] timeSlots = new String[]{"10:25", "10:40", "10:55", "11:10", "11:25"};
        ArrayList<String> times = new ArrayList<>();
        times.add("All Time Slots");
        times.addAll(Arrays.asList(timeSlots));
        ArrayAdapter<String> timeFilterAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, times);
        timeFilter.setAdapter(timeFilterAdapter);
    }

    private void initTypeFilter() {
        // Set up type filter
        Spinner typeFilter = findViewById(R.id.type_filter);
        typeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                currTypeFilter = selectedItem;
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        String[] timeSlots = new String[]{"Course Help", "Self Guided", "Clubs"};
        ArrayList<String> types = new ArrayList<>();
        types.add("All Activity Types");
        types.addAll(Arrays.asList(timeSlots));
        ArrayAdapter<String> typeFilterAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        typeFilter.setAdapter(typeFilterAdapter);
    }

    private void filterList() {
        if (currRoomFilter.equals("All Rooms") && currTimeFilter.equals("All Time Slots")
                && currTypeFilter.equals("All Activity Types")) {
            listView.setAdapter(activityModelArrayAdapter);
            return;
        }

        boolean allRooms = currRoomFilter.equals("All Rooms");
        boolean allTimes = currTimeFilter.equals("All Time Slots");
        boolean allActivityTypes = currTypeFilter.equals("All Activity Types");

        ArrayList<ActivityModel> filteredList = new ArrayList<>();
        for (int i = 0; i < activityModelArrayAdapter.getCount(); i++) {
            ActivityModel current = activityModelArrayAdapter.getItem(i);
            if (current == null) continue;
            if (allRooms) {
                if (allTimes) {
                    if (allActivityTypes)
                        filteredList.add(current);
                    else if (current.getType().equals(typeToEnumString(currTypeFilter)))
                        filteredList.add(current);
                } else if (current.getStartTime().contains(currTimeFilter)) {
                    if (allActivityTypes)
                        filteredList.add(current);
                    else if (current.getType().equals(typeToEnumString(currTypeFilter)))
                        filteredList.add(current);
                }
            } else if (current.getRoom().equals(currRoomFilter)) {
                if (allTimes) {
                    if (allActivityTypes)
                        filteredList.add(current);
                    else if (current.getType().equals(typeToEnumString(currTypeFilter)))
                        filteredList.add(current);
                } else if (current.getStartTime().contains(currTimeFilter)) {
                    if (allActivityTypes)
                        filteredList.add(current);
                    else if (current.getType().equals(typeToEnumString(currTypeFilter)))
                        filteredList.add(current);
                }
            }
        }

        ActivityModelArrayAdapter filteredAdapter =
                new ActivityModelArrayAdapter(getApplicationContext(),
                        R.layout.schedule_item_card, filteredList);
        filteredAdapter.sort(ActivityModelArrayAdapter.getComparator());
        listView.setAdapter(filteredAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(getApplicationContext(),
                        "Activity successfully added!",
                        Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public String typeToEnumString(String str) {
        switch (str) {
            case "Course Help":
                return "COURSE_HELP";
            case "Self Guided":
                return "SELF_GUIDED";
            case "Clubs":
                return "CLUB";
        }
        return null;
    }
}

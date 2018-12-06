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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

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


public class TeacherAdminActivity extends AppCompatActivity {

    private static final String TAG = "lol";

    private static String REFERENCE = "Account";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference(REFERENCE);

    private static String REFERENCE2 = "Rooms";
    DatabaseReference mRootReference2 = firebaseDatabase.getReference(REFERENCE2);

    private static String REFERENCE3 = "ActivityCollection";
    DatabaseReference mRootReference3 = firebaseDatabase.getReference(REFERENCE3);

    HashMap<String, HashMap<String, String>> allAccounts;
    HashMap<String, HashMap<String, String>> allActivities;

    private AccountModelArrayAdapter accountModelArrayAdapter;
    private ListView listView;

    private String currRoomFilter = "All Rooms";
    private boolean toggleActive = true;
    private boolean toggleInactive = true;

    private HashMap<String, String> allRooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_admin_activity);
        Intent i = getIntent();


        mRootReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allActivities =
                        (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                Log.d(TAG, "List of activities: " + allActivities);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mRootReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allRooms = (HashMap<String, String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mRootReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allAccounts =
                        (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                parseAccountData(accountModelArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        initScheduleListView();
        initDropdownFilters();
    }

    private void initScheduleListView() {

        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AccountModel selectedItem = (AccountModel) parent.getItemAtPosition(position);
//                Intent intent = new Intent(TeacherAdminActivity.this, StudentEditActivity.class);
//                intent.putExtra("parcelable_item", (Parcelable) selectedItem);
//                startActivityForResult(intent, 1);
            }
        });

        accountModelArrayAdapter = new AccountModelArrayAdapter(getApplicationContext(), R.layout.account_item_card);
        listView.setAdapter(accountModelArrayAdapter);
    }

    private void parseAccountData(AccountModelArrayAdapter accountModelArrayAdapter) {
        Log.d(TAG, "Parsing Activity Data");
        if (allAccounts == null || accountModelArrayAdapter == null) {
            Log.d(TAG, "null account or adapter");
            return;
        } else {
            accountModelArrayAdapter.reset();
        }

        // Display all activities for teacher
        for (String email : allAccounts.keySet()) {
            if (email.equals("key")) continue;
            HashMap<String, String> fields = (HashMap<String, String>) allAccounts.get(email);
            Log.d(TAG, fields.toString());

            String name = fields.get("full_name");
            String user_id = String.valueOf(fields.get("user_id"));
            String user_email = getEmail(email);
            String activities = fields.get("activities");
            boolean isTeacher = String.valueOf(fields.get("account_type")).equals("1");

            String rooms = getRooms(activities);

            AccountModel newAccount =
                    new AccountModel(name, user_email, user_id, activities, isTeacher, rooms);
            accountModelArrayAdapter.add(newAccount);
        }
        // Reinitialize room filter to populate with all rooms
        initRoomFilter();
        initToggleFilters();
    }

    private void initDropdownFilters() {
        initRoomFilter();
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
        if (allRooms != null)
            rooms.addAll(allRooms.keySet());
        ArrayAdapter<String> roomFilterAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rooms);
        roomFilter.setAdapter(roomFilterAdapter);
    }

    private void initToggleFilters() {
        Switch activeToggle = (Switch) findViewById(R.id.toggle_active);
        Switch inactiveToggle = (Switch) findViewById(R.id.toggle_inactive);

        activeToggle.setChecked(true);
        activeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleActive = isChecked;
                filterList();
            }
        });

        inactiveToggle.setChecked(true);
        inactiveToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleInactive = isChecked;
                filterList();
            }
        });
    }

    private void filterList() {
        if (currRoomFilter.equals("All Rooms") && toggleActive && toggleInactive) {
            listView.setAdapter(accountModelArrayAdapter);
            return;
        }

        boolean allRooms = currRoomFilter.equals("All Rooms");

        ArrayList<AccountModel> filteredList = new ArrayList<>();
        for (int i = 0; i < accountModelArrayAdapter.getCount(); i++) {
            AccountModel current = accountModelArrayAdapter.getItem(i);
            if (allRooms) {
                if (toggleActive && toggleInactive) {
                    filteredList.add(current);
                } else if (toggleActive) {
                    if (current.isActive())
                        filteredList.add(current);
                } else if (toggleInactive){
                    if (!current.isActive())
                        filteredList.add(current);
                }
            } else {
                if (current.getRooms() != null && current.getRooms().contains(currRoomFilter)) {
                    if (toggleActive && toggleInactive) {
                        filteredList.add(current);
                    } else if (toggleActive) {
                        if (current.isActive())
                            filteredList.add(current);
                    } else if (toggleInactive){
                        if (!current.isActive())
                            filteredList.add(current);
                    }
                }
            }
        }

        AccountModelArrayAdapter filteredAdapter =
                new AccountModelArrayAdapter(getApplicationContext(),
                        R.layout.account_item_card, filteredList);
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

    private String getEmail(String email) {
        return email.replace("(AT)","@").replace("(DOT)",".");
    }

    private String getRooms(String activities) {
        StringBuffer rooms = new StringBuffer();
        if (activities == null || activities.isEmpty()) {
            return null;
        }
        for (String activityId : activities.split(" ")) {
            String room = allActivities.get(activityId).get("room");
            rooms.append(room+" ");
        }
        Log.d("lol", "found "+rooms);
        return rooms.toString();
    }
}

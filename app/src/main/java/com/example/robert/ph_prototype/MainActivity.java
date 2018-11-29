package com.example.robert.ph_prototype;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    Button buttonAddAccount;
    EditText editTextName;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRootReference = firebaseDatabase.getReference("message");
//
    //DatabaseReference mAccountReference = mRootReference.child("account");

    //DatabaseReference databaseAccounts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName = (EditText) findViewById(R.id.edittext);
        buttonAddAccount = (Button) findViewById(R.id.simpleButton);

        buttonAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("My app", "hi");
                submitAccount(v);
            }
        });
    }

    public void submitAccount(View v2) {
        editTextName = (EditText) findViewById(R.id.edittext);
        String account =  editTextName.getText().toString();
        Log.i("My app", account);
        mRootReference.setValue(account);
        editTextName.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String value = dataSnapshot.getValue(String.class);
        Log.d("value is ", value);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.i("failed to read", databaseError.toException().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        mRootReference.addValueEventListener(this);
    }
}

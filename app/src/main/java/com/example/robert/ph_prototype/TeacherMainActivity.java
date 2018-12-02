package com.example.robert.ph_prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;


public class TeacherMainActivity extends AppCompatActivity {


    private Button manageButton;
    private Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);


        manageButton = (Button) findViewById(R.id.manage_activites_btn);
        manageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), TeacherManageActivity.class);
                startActivity(newIntent);
            }
        });


        adminButton = findViewById(R.id.admin_btn);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), TeacherAdminActivity.class);
                startActivity(newIntent);
            }
        });



    }
}

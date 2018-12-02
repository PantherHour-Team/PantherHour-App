package com.example.robert.ph_prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;


public class TeacherMainActivity extends AppCompatActivity {


    private Button studentButton;
    private Button manageButton;
    private Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        studentButton = (Button) findViewById(R.id.student_request_btn);
        studentButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), TeacherActivity.class);
                startActivity(newIntent);
            }
        });

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
                Intent newIntent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(newIntent);
            }
        });



    }
}

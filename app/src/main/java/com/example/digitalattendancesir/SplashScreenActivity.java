package com.example.digitalattendancesir;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Database.MySharedPreferences;
import com.example.digitalattendancesir.Model.Teacher;

public class SplashScreenActivity extends AppCompatActivity {

    private DatabaseReference TEACHER_TABLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        TEACHER_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.TEACHER_TABLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoStart();
            }
        }, 2000);
    }

    private void autoStart() {
        String uid = MySharedPreferences.readUid(SplashScreenActivity.this);
        if (uid == null) {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        } else {
            readInfo(uid);
        }
    }

    private void readInfo(String uid) {
        TEACHER_TABLE.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Common.currentTeacher = dataSnapshot.getValue(Teacher.class);
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SplashScreenActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

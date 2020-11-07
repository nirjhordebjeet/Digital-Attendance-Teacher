package com.example.digitalattendancesir;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.digitalattendancesir.Adapter.StudentAdapter;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Fragment.AttendStudentFragment;
import com.example.digitalattendancesir.Fragment.ExamAttendanceFragment;
import com.example.digitalattendancesir.Fragment.TakeAttendanceFragment;
import com.example.digitalattendancesir.Model.DataMessage;
import com.example.digitalattendancesir.Model.FCMResponse;
import com.example.digitalattendancesir.Model.Ids;
import com.example.digitalattendancesir.Model.Student;
import com.example.digitalattendancesir.Model.Token;
import com.example.digitalattendancesir.Remote.IFCMService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.result) {
                    fragment = new TakeAttendanceFragment();
                    setFragment(fragment);
                    setTitle("Take Attendance");
                }
                if (menuItem.getItemId() == R.id.routine) {
                    fragment = new AttendStudentFragment();
                    setFragment(fragment);
                    setTitle("Today Attend Students");
                } if (menuItem.getItemId() == R.id.exam_attendance) {
                    fragment = new ExamAttendanceFragment();
                    setFragment(fragment);
                    setTitle("Exam Attendance");
                }
                return true;
            }
        });

        setFragment(new TakeAttendanceFragment());
        setTitle("Take Attendance");
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }




}

//take attendance page working process
package com.example.digitalattendancesir;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.example.digitalattendancesir.Adapter.ViewPageAdapter;
import com.example.digitalattendancesir.Fragment.AttendanceStatusFragment;
import com.example.digitalattendancesir.Fragment.ExamAttendanceFragment;
import com.example.digitalattendancesir.Fragment.PaymentStatusFragment;
import com.example.digitalattendancesir.Fragment.StudentsDetailsFragment;

public class ViewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        getSupportActionBar().setElevation(0);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new PaymentStatusFragment(), "Payment Status");
        adapter.addFragment(new AttendanceStatusFragment(), "Attendance Status");
        adapter.addFragment(new ExamAttendanceFragment(), "Exam Attendance");
        adapter.addFragment(new StudentsDetailsFragment(), "Students Deatils");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            startActivity(new Intent(ViewActivity.this, MainActivity.class));
            finish();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - viewPager.getCurrentItem());
        }
    }
}

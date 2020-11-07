package com.example.digitalattendancesir.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Model.AttendanceStatus;
import com.example.digitalattendancesir.R;

public class AttendanceStatusFragment extends Fragment {
    private View view;
    private Context context;
    private TextView totalClassTV, absentClassTV, attentClassTV;
    private DatabaseReference attentStatusDb;
    private AttendanceStatus attendanceStatus;

    public AttendanceStatusFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_layout, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        totalClassTV = (TextView) view.findViewById(R.id.totalClassTV);
        absentClassTV = (TextView) view.findViewById(R.id.absentClassTV);
        attentClassTV = (TextView) view.findViewById(R.id.attentClassTV);

        attentStatusDb = FirebaseDatabase.getInstance().getReference("ClassAttendanceStatus");
        attentStatusDb.orderByChild("student_id").equalTo(Common.currentStudentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        attendanceStatus = snapshot.getValue(AttendanceStatus.class);

                        if (attendanceStatus != null) {
                            if (attendanceStatus.getTotal_class() != null) {
                                totalClassTV.setText(attendanceStatus.getTotal_class());
                            }
                            if (attendanceStatus.getAbsent_class() != null) {
                                absentClassTV.setText(attendanceStatus.getAbsent_class());
                            }
                            if (attendanceStatus.getAttent_class() != null) {
                                attentClassTV.setText(attendanceStatus.getAttent_class());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

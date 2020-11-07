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
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Model.Student;
import com.example.digitalattendancesir.R;

public class StudentsDetailsFragment extends Fragment {
    private View view;
    private Context context;
    private TextView name_tv, student_id_tv, batch_tv, section_tv;
    private DatabaseReference studentDb;
    private Student studentDetails;

    public StudentsDetailsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_details_layout, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        name_tv = (TextView) view.findViewById(R.id.name_tv);
        student_id_tv = (TextView) view.findViewById(R.id.student_id_tv);
        batch_tv = (TextView) view.findViewById(R.id.batch_tv);
        section_tv = (TextView) view.findViewById(R.id.section_tv);

        studentDb = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);
        studentDb.child(Common.currentStudentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    studentDetails = dataSnapshot.getValue(Student.class);

                    if (studentDetails != null) {
                        if (studentDetails.getStudent_id() != null) {
                            student_id_tv.setText(studentDetails.getStudent_id());
                        }
                        if (studentDetails.getName() != null) {
                            name_tv.setText(studentDetails.getName());
                        }
                        if (studentDetails.getBatch() != null) {
                            batch_tv.setText(studentDetails.getBatch());
                        }
                        if (studentDetails.getSection() != null) {
                            section_tv.setText(studentDetails.getSection());
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

package com.example.digitalattendancesir.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Model.LUEntryStudentData;
import com.example.digitalattendancesir.Model.LURoom;
import com.example.digitalattendancesir.Model.Student;
import com.example.digitalattendancesir.R;
import com.example.digitalattendancesir.ViewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ExamAttendanceFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private Spinner roomSpinner;
    private ZXingScannerView zXingScannerView;

    private DatabaseReference roomDb, examAttendanceDb, studentDb;
    private List<String> luRoomList;

    private ArrayAdapter<String> roomAdapter;

    private String roomNumber;

    private ProgressDialog progressDialog;

    private static final int REQUEST_CAMERA_PERMISSION = 101;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanCode();
                }
            }
            break;
        }
    }

    private View view;
    private Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exam_attendance_fragment, container, false);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        luRoomList = new ArrayList<>();

        initFirebaseDB();

        roomDb.addValueEventListener(eventListener);

        roomAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, luRoomList);
        roomSpinner.setAdapter(roomAdapter);
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roomNumber = roomAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void initFirebaseDB() {
        roomDb = FirebaseDatabase.getInstance().getReference("RoomNumber");
        examAttendanceDb = FirebaseDatabase.getInstance().getReference(FirebaseTable.ExamAttendance_TABLE);
        studentDb = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);

        initView();
    }

    private void initView() {
        roomSpinner = (Spinner) view.findViewById(R.id.roomSpinner);
        zXingScannerView = (ZXingScannerView) view.findViewById(R.id.zXingScannerView);
        zXingScannerView.setResultHandler(this);

        scanCode();
    }

    private void scanCode() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        } else {
            zXingScannerView.setAutoFocus(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                zXingScannerView.setDefaultFocusHighlightEnabled(true);
                zXingScannerView.setFocusedByDefault(true);
            }
            zXingScannerView.setFocusableInTouchMode(true);
            zXingScannerView.startCamera();
        }
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            luRoomList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                luRoomList.add(snapshot.getValue(LURoom.class).getRoom_number());
                progressDialog.dismiss();
            }
            roomAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void handleResult(Result result) {
        //zXingScannerView.resumeCameraPreview(MainActivity.this);
        valueSet(result.getText());
    }

    private void valueSet(String id) {
        studentDb.child("1712020087").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Student studentDetails = dataSnapshot.getValue(Student.class);
                    uploadInfo(studentDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadInfo(final Student studentDetails) {

        //Date
        Calendar calendarToDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = currentDateFormat.format(calendarToDate.getTime());

        //Time
        Calendar calendarToTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = currentTimeFormat.format(calendarToTime.getTime());

        LUEntryStudentData data = new LUEntryStudentData();
        data.setSubject("Web Technology");
        data.setSobject_code("CSE-4311");
        data.setDepartment("CSE");
        data.setRoom_number(roomNumber);
        data.setDate(currentDate);
        data.setTime(currentTime);
        data.setStudent_id(String.valueOf(studentDetails.getStudent_id()));
        examAttendanceDb.child(String.valueOf(studentDetails.getStudent_id())).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Common.currentStudentId = String.valueOf(studentDetails.getStudent_id());
                    startActivity(new Intent(context, ViewActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });
    }

}

package com.example.digitalattendancesir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Model.Attendance;
import com.example.digitalattendancesir.Model.Ids;
import com.example.digitalattendancesir.Model.Student;
import com.example.digitalattendancesir.Remote.IFCMService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportCsv extends AppCompatActivity {

    Button csvread;
    private List<Attendance> studentList;
    private List<Ids> list;
    StringBuilder data;
    private DatabaseReference STUDENT_TABLE, TOKEN_TABLE, STUDENT_LIST_TABLE;
    private IFCMService mFCMService;
    String batchString, sectionString;
    private DatabaseReference STUDENT_ATTENDANCE_TABLE;
    private List<Attendance> attendanceList;

    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_csv);

        csvread = findViewById(R.id.readCsv);

        studentList = new ArrayList<>();
        list = new ArrayList<>();
        attendanceList = new ArrayList<>();
        data = new StringBuilder();
        mFCMService = Common.getFCMService();
        batchString = getIntent().getExtras().getString("batchid");
        sectionString = getIntent().getExtras().getString("sectionid");
//        Spinner batchSpinner, sectionSpinner;
//
//        batchSpinner = (Spinner) update_layout.findViewById(R.id.batchSpinner);
//        sectionSpinner = (Spinner) update_layout.findViewById(R.id.sectionSpinner);

        STUDENT_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);
        TOKEN_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.TOKEN_TABLE);
        STUDENT_LIST_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_LIST_TABLE);
        STUDENT_ATTENDANCE_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_ATTENDANCE_TABLE);

        csvread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                data.append("serial, Date & Time, Student Id, Name");

                STUDENT_ATTENDANCE_TABLE.child(batchString)
                        .child(sectionString)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                attendanceList.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        attendanceList.add(snapshot.getValue(Attendance.class));


                                    }

                                    for(int i = 0; i<attendanceList.size(); i++){
                                        data.append("\n" + String.valueOf(i+1) +"....."+Common.getDate(Long.parseLong(attendanceList.get(i).getTime()), "dd/MMM/yyyy hh:mm a")+"...." +attendanceList.get(i).getStudent_id()+" ..... "  +attendanceList.get(i).getStudent_name());

                                    }


                                }

                                try {
                                    //saving the file into device
                                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                                    out.write((data.toString()).getBytes());
                                    out.close();

                                    //exporting
                                    Context context = getApplicationContext();
                                    File filelocation = new File(getFilesDir(), "data.csv");
                                    Uri path = FileProvider.getUriForFile(context, "com.example.digitalattendancesir.fileprovider", filelocation);
                                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                                    fileIntent.setType("text/csv");
                                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//
//                data.append("serial, Date & Time, Student Id");
//
//                STUDENT_LIST_TABLE.child(batchString).child(sectionString)
//                        .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                list.clear();
//                                if (dataSnapshot.exists()) {
//                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                        list.add(snapshot.getValue(Ids.class));
//
//                                        for (int i = 0; i < list.size(); i++) {
//                                            studentList.clear();
//                                            query = STUDENT_TABLE.orderByChild("student_id").equalTo(list.get(i).getId());
//                                        }
//                                        query.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                                    studentList.add(snapshot.getValue(Attendance.class));
//                                                    for (int i = 0; i< studentList.size(); i++) {
//                                                        data.append("\n" + String.valueOf(i+1)+"\t"+studentList.get(i).getTime()+"\t"+studentList.get(i).getStudent_id());
//
//
//                                                    }
//
//                                                }
//                                                try {
//                                                    //saving the file into device
//                                                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
//                                                    out.write((data.toString()).getBytes());
//                                                    out.close();
//
//                                                    //exporting
//                                                    Context context = getApplicationContext();
//                                                    File filelocation = new File(getFilesDir(), "data.csv");
//                                                    Uri path = FileProvider.getUriForFile(context, "com.example.digitalattendancesir.fileprovider", filelocation);
//                                                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
//                                                    fileIntent.setType("text/csv");
//                                                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
//                                                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//                                                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                // progressDialog.dismiss();
//                                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                for(int i = 0; i<5; i++){
//                    //data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
//                }


                Toast.makeText(ExportCsv.this, batchString+" "+sectionString, Toast.LENGTH_LONG).show();
                Toast.makeText(ExportCsv.this, "done", Toast.LENGTH_LONG).show();
//                try {
//                    //saving the file into device
//                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
//                    out.write((data.toString()).getBytes());
//                    out.close();
//
//                    //exporting
//                    Context context = getApplicationContext();
//                    File filelocation = new File(getFilesDir(), "data.csv");
//                    Uri path = FileProvider.getUriForFile(context, "com.example.digitalattendancesir.fileprovider", filelocation);
//                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
//                    fileIntent.setType("text/csv");
//                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
//                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });
    }
}

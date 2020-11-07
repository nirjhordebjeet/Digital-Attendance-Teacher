package com.example.digitalattendancesir.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalattendancesir.ExportCsv;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.digitalattendancesir.Adapter.AttendStudentsAdapter;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Model.Attendance;
import com.example.digitalattendancesir.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendStudentFragment extends Fragment {

    private View view;
    private Context context;

    private TextView total_tv;
    private RecyclerView recyclerView;

    private DatabaseReference STUDENT_ATTENDANCE_TABLE;
    private List<Attendance> attendanceList;
    private AttendStudentsAdapter adapter;

    private boolean isFirstSelect = true;
    private boolean isSecondSelect = true;

    private String batchString, sectionString;
    private Button exportBtn;

    private String currentDate, timeFormat = "dd/MMM/yyyy";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.attend_student_fragment, container, false);

        showInputDialog();

        STUDENT_ATTENDANCE_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_ATTENDANCE_TABLE);

        Calendar calendarToDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(timeFormat);
        currentDate = currentDateFormat.format(calendarToDate.getTime());

        total_tv = (TextView) view.findViewById(R.id.total_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        attendanceList = new ArrayList<>();

        exportBtn = view.findViewById(R.id.exploreButton);

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), ExportCsv.class);
                intent.putExtra("batchid", batchString);
                intent.putExtra("sectionid", sectionString);
                startActivity(intent);
//                StringBuilder data = new StringBuilder();
//                data.append("Time,Distance");
//                for(int i = 0; i<5; i++){
//                    data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
//                }
//
//                try{
//                    //saving the file into device
//                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
//                    out.write((data.toString()).getBytes());
//                    out.close();
//
//                    //exporting
//                    //Context context = getApplicationContext();
//                    File filelocation = new File(getFilesDir(), "data.csv");
//                    Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
//                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
//                    fileIntent.setType("text/csv");
//                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
//                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
//                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }

            }
        });

        return view;
    }

    private void showInputDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View update_layout = inflater.inflate(R.layout.dialog_input, null);
        dialog.setView(update_layout);

        Spinner batchSpinner, sectionSpinner;

        batchSpinner = (Spinner) update_layout.findViewById(R.id.batchSpinner);
        sectionSpinner = (Spinner) update_layout.findViewById(R.id.sectionSpinner);

        batchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirstSelect) {
                    isFirstSelect = false;
                } else {
                    batchString = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSecondSelect) {
                    isSecondSelect = false;
                } else {
                    sectionString = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (batchString == null) {
                    Toast.makeText(context, "Select Batch", Toast.LENGTH_SHORT).show();
                    return;
                } else if (sectionString == null) {
                    Toast.makeText(context, "Select Section", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    loadStudent(batchString, sectionString);
                }
            }
        });
        dialog.show();
    }

    private void loadStudent(String batchString, String sectionString) {
        STUDENT_ATTENDANCE_TABLE.child(batchString)
                .child(sectionString)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        attendanceList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (currentDate.equals(Common.getDate(Long.parseLong(snapshot.getValue(Attendance.class).getTime()), timeFormat))) {
                                    attendanceList.add(snapshot.getValue(Attendance.class));
                                    total_tv.setText("Total Student : " + attendanceList.size());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        adapter = new AttendStudentsAdapter(context, attendanceList);
        recyclerView.setAdapter(adapter);
    }
}

package com.example.digitalattendancesir.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalattendancesir.ExportCsv;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.digitalattendancesir.Adapter.StudentAdapter;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Database.FirebaseTable;
import com.example.digitalattendancesir.Model.DataMessage;
import com.example.digitalattendancesir.Model.FCMResponse;
import com.example.digitalattendancesir.Model.Ids;
import com.example.digitalattendancesir.Model.Student;
import com.example.digitalattendancesir.Model.Token;
import com.example.digitalattendancesir.R;
import com.example.digitalattendancesir.Remote.IFCMService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeAttendanceFragment extends Fragment {

    private View view;
    private Context context;

    private boolean isFirstSelect = true;
    private boolean isSecondSelect = true;

    private String batchString, sectionString;

    private Button sendBtn, exportBtn;

    private DatabaseReference STUDENT_TABLE, TOKEN_TABLE, STUDENT_LIST_TABLE;

    private List<Student> studentList;
    private List<Ids> list;
    private List<Token> tokenList;

    private ProgressDialog progressDialog;

    private StudentAdapter adapter;

    private RecyclerView recyclerView;

    private IFCMService mFCMService;


    private Query query, queryTO;

    public TakeAttendanceFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.take_attendance_fragment, container, false);

        showInputDialog();

        mFCMService = Common.getFCMService();

        studentList = new ArrayList<>();
        list = new ArrayList<>();
        tokenList = new ArrayList<>();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        STUDENT_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_TABLE);
        TOKEN_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.TOKEN_TABLE);
        STUDENT_LIST_TABLE = FirebaseDatabase.getInstance().getReference(FirebaseTable.STUDENT_LIST_TABLE);

        sendBtn = (Button) view.findViewById(R.id.sendBtn);
//        exportBtn = view.findViewById(R.id.exploreButton);
//
//        exportBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent intent = new Intent(getContext(), ExportCsv.class);
//                intent.putExtra("batchid", batchString);
//                intent.putExtra("sectionid", sectionString);
//                startActivity(intent);
////                StringBuilder data = new StringBuilder();
////                data.append("Time,Distance");
////                for(int i = 0; i<5; i++){
////                    data.append("\n"+String.valueOf(i)+","+String.valueOf(i*i));
////                }
////
////                try{
////                    //saving the file into device
////                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
////                    out.write((data.toString()).getBytes());
////                    out.close();
////
////                    //exporting
////                    //Context context = getApplicationContext();
////                    File filelocation = new File(getFilesDir(), "data.csv");
////                    Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
////                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
////                    fileIntent.setType("text/csv");
////                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
////                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
////                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
////                }
////                catch(Exception e){
////                    e.printStackTrace();
////                }
//
//            }
//        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tokenList.size() > 0) {

                    String uuid = UUID.randomUUID().toString().substring(0, 6);

                    showTokenDialog(uuid);
                    for (int i = 0; i < tokenList.size(); i++) {
                        Map<String, String> content = new HashMap<>();
                        content.put("title", "attendance");
                        content.put("message", uuid);
                        content.put("code", uuid);
                        content.put("teacher_id", Common.currentTeacher.getPhone());
                        content.put("teacher_name", Common.currentTeacher.getName());

                        DataMessage dataMessage = new DataMessage(tokenList.get(i).getToken(), content);

                        // Firebase_cloud_messaging service
                        mFCMService.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (response.body().getSuccess() == 1) {

                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {
                                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Toast.makeText(context, "Send", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void showTokenDialog(String uuis) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(uuis);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
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
       // dialog.setCancelable(false);
        dialog.show();
    }

    private void loadStudent(String batchString, String sectionString) {
        progressDialog.show();
        STUDENT_LIST_TABLE.child(batchString).child(sectionString)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                list.add(snapshot.getValue(Ids.class));

                                loadStudentInfo(list);

                                loadToken(list);
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadToken(List<Ids> list) {
        tokenList.clear();
        for (int i = 0; i < list.size(); i++) {
            queryTO = TOKEN_TABLE.child(list.get(i).getId());
        }

        queryTO.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Token token = dataSnapshot.getValue(Token.class);
                    tokenList.add(token);
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void loadStudentInfo(final List<Ids> list) {
        for (int i = 0; i < list.size(); i++) {
            studentList.clear();
            query = STUDENT_TABLE.orderByChild("student_id").equalTo(list.get(i).getId());
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    studentList.add(snapshot.getValue(Student.class));
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        adapter = new StudentAdapter(context, studentList);
        recyclerView.setAdapter(adapter);
    }
}

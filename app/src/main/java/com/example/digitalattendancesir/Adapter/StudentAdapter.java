package com.example.digitalattendancesir.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalattendancesir.Model.Student;
import com.example.digitalattendancesir.R;
import com.example.digitalattendancesir.ViewHolder.StudentViewHolder;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {

    private Context context;
    private List<Student> studentList;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        if (studentList.get(position).getName() != null) {
            holder.name_tv.setText(studentList.get(position).getName());
        }

        if (studentList.get(position).getStudent_id() != null) {
            holder.student_id_tv.setText(studentList.get(position).getStudent_id());
        }

        if (studentList.get(position).getBatch() != null) {
            holder.batch_tv.setText(studentList.get(position).getBatch());
        }

        if (studentList.get(position).getSection() != null) {
            holder.section_tv.setText(studentList.get(position).getSection());
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}

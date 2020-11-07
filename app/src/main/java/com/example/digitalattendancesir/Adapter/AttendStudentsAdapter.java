package com.example.digitalattendancesir.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Model.Attendance;
import com.example.digitalattendancesir.R;
import com.example.digitalattendancesir.ViewHolder.AttendStudentViewHolder;

import java.util.List;

public class AttendStudentsAdapter extends RecyclerView.Adapter<AttendStudentViewHolder> {

    private Context context;
    private List<Attendance> attendanceList;

    public AttendStudentsAdapter(Context context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.attend_students_items, parent, false);
        return new AttendStudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendStudentViewHolder holder, int position) {
        holder.student_name.setText(attendanceList.get(position).getStudent_name());
        holder.student_id.setText(attendanceList.get(position).getStudent_id());
        holder.time.setText(Common.getDate(Long.parseLong(attendanceList.get(position).getTime()), "dd/MMM/yyyy hh:mm a"));
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
}

package com.example.digitalattendancesir.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalattendancesir.R;

public class AttendStudentViewHolder extends RecyclerView.ViewHolder {

    public TextView student_name, student_id, time;

    public AttendStudentViewHolder(@NonNull View itemView) {
        super(itemView);

        student_name = (TextView) itemView.findViewById(R.id.student_name);
        student_id = (TextView) itemView.findViewById(R.id.student_id);
        time = (TextView) itemView.findViewById(R.id.time);
    }
}

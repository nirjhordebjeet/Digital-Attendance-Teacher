package com.example.digitalattendancesir.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalattendancesir.R;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    public TextView name_tv, student_id_tv, batch_tv, section_tv;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);

        name_tv = (TextView) itemView.findViewById(R.id.name_tv);
        student_id_tv = (TextView) itemView.findViewById(R.id.student_id_tv);
        batch_tv = (TextView) itemView.findViewById(R.id.batch_tv);
        section_tv = (TextView) itemView.findViewById(R.id.section_tv);
    }
}

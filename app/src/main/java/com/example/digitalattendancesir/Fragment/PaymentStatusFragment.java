package com.example.digitalattendancesir.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.digitalattendancesir.Common.Common;
import com.example.digitalattendancesir.Model.PaymentStatus;
import com.example.digitalattendancesir.R;

public class PaymentStatusFragment extends Fragment {
    private View view;
    private Context context;
    private TextView totalPaymentTV, paymentTV, dueTV;
    private DatabaseReference paymentDb;
    private PaymentStatus paymentStatus;

    public PaymentStatusFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payment_status_layout, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        totalPaymentTV = (TextView) view.findViewById(R.id.totalPaymentTV);
        paymentTV = (TextView) view.findViewById(R.id.paymentTV);
        dueTV = (TextView) view.findViewById(R.id.dueTV);

        paymentDb = FirebaseDatabase.getInstance().getReference("PaymentStatus");
        paymentDb.orderByChild("student_id").equalTo(Common.currentStudentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        paymentStatus = snapshot.getValue(PaymentStatus.class);

                        if (paymentStatus != null) {
                            if (!paymentStatus.isClear_payment()) {
                                dueTV.setTextColor(Color.RED);
                            }
                            if (paymentStatus.getPayment() != null) {
                                totalPaymentTV.setText(paymentStatus.getTotal_payment());
                            }
                            if (paymentStatus.getPayment() != null) {
                                paymentTV.setText(paymentStatus.getPayment());
                            }
                            if (paymentStatus.getDue() != null) {
                                dueTV.setText(paymentStatus.getDue());
                            }
                        } else {
                            Toast.makeText(context, "paymentStatus null", Toast.LENGTH_SHORT).show();
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

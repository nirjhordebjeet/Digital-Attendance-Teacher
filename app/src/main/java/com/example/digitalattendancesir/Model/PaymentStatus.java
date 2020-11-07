package com.example.digitalattendancesir.Model;

public class PaymentStatus {
    private boolean clear_payment;
    private String due;
    private String payment;
    private String total_payment;
    private String student_id;

    public PaymentStatus() {
    }

    public boolean isClear_payment() {
        return clear_payment;
    }

    public void setClear_payment(boolean clear_payment) {
        this.clear_payment = clear_payment;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(String total_payment) {
        this.total_payment = total_payment;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}

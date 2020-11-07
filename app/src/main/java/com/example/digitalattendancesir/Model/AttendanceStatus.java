package com.example.digitalattendancesir.Model;

public class AttendanceStatus {
    private String total_class;
    private String absent_class;
    private String attent_class;
    private String student_id;

    public AttendanceStatus() {
    }

    public String getTotal_class() {
        return total_class;
    }

    public void setTotal_class(String total_class) {
        this.total_class = total_class;
    }

    public String getAbsent_class() {
        return absent_class;
    }

    public void setAbsent_class(String absent_class) {
        this.absent_class = absent_class;
    }

    public String getAttent_class() {
        return attent_class;
    }

    public void setAttent_class(String attent_class) {
        this.attent_class = attent_class;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}

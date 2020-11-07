package com.example.digitalattendancesir.Model;

public class LUEntryStudentData {
    private String room_number;
    private String student_id;
    private String date;
    private String time;
    private String subject;
    private String sobject_code;
    private String department;

    public LUEntryStudentData() {
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSobject_code() {
        return sobject_code;
    }

    public void setSobject_code(String sobject_code) {
        this.sobject_code = sobject_code;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

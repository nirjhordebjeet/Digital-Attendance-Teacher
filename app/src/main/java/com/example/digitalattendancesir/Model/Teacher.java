package com.example.digitalattendancesir.Model;

public class Teacher {
    private String name;
    private String phone;
    private String department;
    private String email;
    private String rule;

    public Teacher() {
    }

    public Teacher(String name, String phone, String department, String email, String rule) {
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.email = email;
        this.rule = rule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}

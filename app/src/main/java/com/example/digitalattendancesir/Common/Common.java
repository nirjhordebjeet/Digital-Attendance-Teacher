package com.example.digitalattendancesir.Common;

import com.example.digitalattendancesir.Model.Teacher;
import com.example.digitalattendancesir.Remote.FCMClient;
import com.example.digitalattendancesir.Remote.IFCMService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static Teacher currentTeacher;

    public static String currentStudentId;

    public static final String fcmURl = "https://fcm.googleapis.com/";

    public static IFCMService getFCMService() {
        return FCMClient.getClient(fcmURl).create(IFCMService.class);
    }

    //"dd/MMM/yyyy hh:mm a"
    public static String getDate(long milliSeconds, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}

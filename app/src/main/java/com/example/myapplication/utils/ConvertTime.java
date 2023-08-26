package com.example.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertTime {
    private static final String TIME_ZONE="Asia/Ho_Chi_Minh";
    public static String convertTimestampToVietnameseTime(String timestamp) {
        try {

            // Tạo đối tượng Date từ timestamp
            Date date = new Date(Long.parseLong(timestamp));

            // Tạo đối tượng SimpleDateFormat với định dạng muốn hiển thị
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            // Thiết lập múi giờ cho đối tượng SimpleDateFormat
            sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

            // Chuyển đổi và trả về giờ Việt Nam
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

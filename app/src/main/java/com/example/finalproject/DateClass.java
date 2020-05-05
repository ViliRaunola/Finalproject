package com.example.finalproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateClass {

    //makes DateClass singleton
    private static DateClass dateClass = new DateClass();

    public DateClass() {
    }

    public static DateClass getInstance() {
        return dateClass;
    }


    //https://stackoverflow.com/questions/5175728/how-to-get-the-current-date-time-in-java
    // returns current date with weekday as String
    public String getCurrentDateWithWeekDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\ndd.MM.yyyy");
        Date currentDate = new Date();
        return simpleDateFormat.format(currentDate);
    }

    //returns current date as String
    public String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date currentDate = new Date();
        return simpleDateFormat.format(currentDate);
    }

    //returns current day as integer
    public int getTodayInt() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        Date currentDate = new Date();
        return Integer.parseInt(simpleDateFormat.format(currentDate));
    }


    //https://beginnersbook.com/2017/10/java-add-days-to-date/
    //returns next or previous date (+1 or -1) or any other date by adding numberOfDays to given parameter date
    public static Date changeDate(Date date, int numberOfDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, numberOfDays);
        return cal.getTime();
    }

    //https://www.tutorialspoint.com/how-to-compare-two-dates-in-java
    //compares if given date is later than current date
    public Boolean compareDates(String selectedDateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE\ndd.MM.yyyy");
        Boolean bol = true;
        try {
            Date today = simpleDateFormat.parse(getCurrentDateWithWeekDay());
            Date selectedDate = simpleDateFormat.parse(selectedDateString);

            if (selectedDate.compareTo(today) > 0) {
                bol = false;
            } else {
                bol = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bol;
    }
}

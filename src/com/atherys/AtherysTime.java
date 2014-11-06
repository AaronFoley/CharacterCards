package com.atherys;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AtherysTime {

    public static String CurrentTime(){

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        int year = cal.get(Calendar.YEAR);
        // Year first, that's easy.
        year -= 1755;

        // Convert Gregorian days to total hours, less 1 day.
        int month = cal.get(Calendar.MONTH) + 1;
        double day = cal.get(Calendar.DAY_OF_MONTH);

        if (month == 1) {
            day = (day - 1) * 24;
        }
        // February
        else if (month == 2) {
            day = (day + 30) * 24;
        }
        // March
        else if (month == 3) {
            day = (day + 58) * 24;
        }
        //April
        else if (month == 4) {
            day = (day + 89) * 24;
        }
        // May
        else if (month == 5) {
            day = (day + 119) * 24;
        }
        // June
        else if (month == 6) {
            day = (day + 150) * 24;
        }
        // July
        else if (month == 7) {
            day = (day + 180) * 24;
        }
        // August
        else if (month == 8) {
            day = (day + 211) * 24;
        }
        // September
        else if (month == 9) {
            day = (day + 242) * 24;
        }
        // October
        else if (month == 10) {
            day = (day + 272) * 24;
        }
        // November
        else if (month == 11) {
            day = (day + 303) * 24;
        }
        // December
        else if (month == 12) {
            day = (day + 333) * 24;
        }

        // Now to catch spare days through the real day.

        double hours = cal.get(Calendar.HOUR_OF_DAY);
        double minutes = (double) cal.get(Calendar.MINUTE) / 60;
        double seconds = (double) cal.get(Calendar.SECOND) / 3600;

        // Add the current elapsed real hours.
        day += (double) (hours + minutes + seconds);

        // A quick aside to pull out the leftover time for later.
        double Ahour = (double) day / 8.76;

        // Now to convert total hours into A'therian days, rounding down.
        day = Math.floor(day / 8.76);

        // Now to figure out the seasons and date from it.

        String season = "";

        if (day < 101) {
            season = "Ascension";
        }
        else if (day > 100 && day < 201) {
            season = "Wind's Renewal";
            day -= 100;
        }
        else if (day > 200 && day < 301) {
            season = "Forgefire";
            day -= 200;
        }
        else if (day > 300 && day < 401) {
            season = "Sun's Might";
            day -= 300;
        }
        else if (day > 400 && day < 501) {
            season = "Sand's Song";
            day -= 400;
        }
        else if (day > 500 && day < 601) {
            season = "Tide's Flux";
            day -= 500;
        }
        else if (day > 600 && day < 701) {
            season = "Stormshift";
            day -= 600;
        }
        else if (day > 700 && day < 801) {
            season = "Urth's Heart";
            day -= 700;
        }
        else if (day > 800 && day < 901) {
            season = "Frostshroud";
            day -= 800;
        }
        else if (day > 900) {
            season = "Reckoning";
            day -= 900;
        }

        // Now to get the ordinal form of the day, and day of the week.

        String sDay = String.valueOf((int)day);
        char endNum = sDay.charAt(sDay.length() - 1);
        String ordinal = "";
        String weekDay = "";

        if (endNum == '1') {
            ordinal = "st";
            weekDay = "Ulvei";
        }
        else if (endNum == '2') {
            ordinal = "nd";
            weekDay = "Antwei";
        }
        else if (endNum == '3') {
            ordinal = "rd";
            weekDay = "Toldei";
        }
        else if (endNum == '4') {
            ordinal = "th";
            weekDay = "Mirnei";
        }
        else if (endNum == '5') {
            ordinal = "th";
            weekDay = "Kultei";
        }
        else if (endNum == '6') {
            ordinal = "th";
            weekDay = "Yavhei";
        }
        else if (endNum == '7') {
            ordinal = "th";
            weekDay = "Fenyei";
        }
        else if (endNum == '8') {
            ordinal = "th";
            weekDay = "Rosnei";
        }
        else if (endNum == '9') {
            ordinal = "th";
            weekDay = "Ulmhei";
        }
        else if (endNum == '0') {
            ordinal = "th";
            weekDay = "Paartesh";
        }

        // Now let's use Ahour from earlier to get the time.
        double hourFrag = Double.parseDouble("." + String.valueOf(Ahour).split("\\.")[1]);
        // Grab the minutes out before moving on.
        double Amin = hourFrag * 24;
        int thours = (int) Math.floor(hourFrag * 24);

        double minFrag = Double.parseDouble("." + String.valueOf(Amin).split("\\.")[1]);
        int tmins = (int) Math.floor(minFrag * 60);

        String time = "";
        // Add a zero in front if it's before 10:00 to format it nicely.
        if(thours < 10)
            time += '0';
        time += String.valueOf(thours) + ':';
        //Add a zero in front if it's before 10 minutes past the hour.
        if(tmins < 10)
            time += '0';
        time += String.valueOf(tmins);

        // Now spit it all out.
        return "The date is " + weekDay + ", " + (int)day + ordinal + " of " + season + ", AU" + year + " and the time is " + time;
    }

}
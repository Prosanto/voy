package eu.coach_yourself.app.utils;

import static eu.coach_yourself.app.utils.Tools.YYYY_MM_DD_HH;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtility {

    /**
     * Parse string date to formatted date object
     *
     * @param dateString
     * @param dateFormat
     * @return parseDate - Date object or null
     */
    public static Date parseDate(String dateString, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date parsedDate = sdf.parse(dateString);
            return parsedDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Format simple date formatted object to string
     *
     * @param date
     * @param dateFormat
     * @return formatDate - Formatted date string
     */
    public static String formatSDF(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatDate = sdf.format(date).trim();
        return formatDate;
    }

    /**
     * Format date object to string
     *
     * @param date
     * @param dateFormat
     * @return formatDate - formatted date string
     */
    public static String formatDate(Date date, String dateFormat) {
        Format formatter = new SimpleDateFormat(dateFormat);
        String formatDate = formatter.format(date).trim();
        return formatDate;
    }

    public static String dateToTime(Date date) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Tools.DD_MM_);
        String dateToTime = DATE_FORMAT.format(date);
        return dateToTime;
    }


    public static long dateToMillisecond(String duration) {
        long currentMillisec = 0;
        try {

            String dateFormat = "yyyy-MM-dd HH:mm:ss";
            //  SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date currentDate = parseDate(duration, dateFormat);
            currentMillisec = currentDate.getTime();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentMillisec;

    }


    public static String dateFromDayDifference(String currentDate, long oneday) {
        SimpleDateFormat sdf;
        Date date = null;
        String finalDateString;
        sdf = new SimpleDateFormat(Tools.YYYY_MM_DD_HH_MM_SS);
        try {
            date = sdf.parse(currentDate);
            long currentMillisec = date.getTime();
            Date resultExpectDate = new Date((currentMillisec + oneday));
            String finalDate = sdf.format(resultExpectDate).toString().trim();
            date = sdf.parse(finalDate);
            finalDateString = formatDate(date, Tools.YYYY_MM_DD_HH_MM_SS);
            return finalDateString;
        } catch (Exception e) {
            return "NULL";
        }

    }

//    public static String getCurrentTime() {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String date = df.format(Calendar.getInstance().getTime());
//
//        Date currentDate = parseDate(duration, dateFormat);
//       long currentMillisec = currentDate.getTime();
//
//        return date;
//    }

    public static long getCurrentTimeHHmm() {
        DateFormat df = new SimpleDateFormat(Tools.YYYY_MM_DD_HH_MM_SS);
        String date = df.format(Calendar.getInstance().getTime());
        String dateFormat = Tools.YYYY_MM_DD_HH_MM_SS;
        Date currentDate = parseDate(date, dateFormat);
        long currentMillisec = currentDate.getTime();
        return currentMillisec;
    }

    public static String getCurrentNumberMonthj() {
        Calendar mCalendar = Calendar.getInstance();
        int number = mCalendar.get(Calendar.MONTH) + 1;
        return "" + number;
    }

    public static String getTodaysDate() {
        DateFormat df = new SimpleDateFormat(Tools.YYYY_MM_DD_HH_MM_SS);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getYearDate() {
        DateFormat df = new SimpleDateFormat(Tools.YYYY);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getDayofWeek() {
        DateFormat df = new SimpleDateFormat(Tools.MMMM_yyyy);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getDayofWeek(String input) {
        //2019-06-14
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputformat = new SimpleDateFormat(Tools.MMMM_yyyy);
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;

    }

    public static String getCurrentTimeForsend() {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date mDate = new Date();
        TimeZone tz = TimeZone.getTimeZone("GMT");
        sdf.setTimeZone(tz);
        String date = sdf.format(mDate);
//        Date currentDate = parseDate(date, dateFormat);


//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        s;
//        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getDateFortop(String input) {
        //2019-06-14
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputformat = new SimpleDateFormat(Tools.EEE_D_MMM_YYYY_HH_MM);
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;

    }

    public static String getCurrentday() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String converTdate(String input) {
        return input;
//        DateFormat df = new SimpleDateFormat("HH:mm");
//        DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
//        Date date = null;
//        String output = null;
//        try{
//            date= df.parse(input);
//            output = outputformat.format(date);
//            System.out.println(output);
//        }catch(ParseException pe){
//            pe.printStackTrace();
//        }
//        return output;
    }

    public static String getCurrentdayFor() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getCurrentLinkUp(String input) {
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat outputformat = new SimpleDateFormat("EEEE, MMM dd");
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getCovention(String input) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        DateFormat outputformat = new SimpleDateFormat("hh:mm a");
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getCurrentConvert(String input) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputformat = new SimpleDateFormat("EEEE, MMM dd");
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getCurrentConvertYear(String input) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputformat = new SimpleDateFormat("yyyy");
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getCurrentConvertShow(String input) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputformat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getCurrentConvertFromMMDDYYY(String input) {
        DateFormat df = new SimpleDateFormat(Tools.YYYY_MM_DD_HH_MM_SS);
        DateFormat outputformat = new SimpleDateFormat(YYYY_MM_DD_HH);
        Date date = null;
        String output = null;
        try {
            date = df.parse(input);
            output = outputformat.format(date);
            System.out.println(output);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return output;
    }

    public static String getAge(String dateString) {
        String[] parts = dateString.split("/");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        int mYear = Integer.parseInt(part3);
        int mMonth = Integer.parseInt(part2);
        int mDay = Integer.parseInt(part1);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(mYear, mMonth, mDay);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}

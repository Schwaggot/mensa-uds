package de.mensa_uds.android.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateValidater {

    private static final String TAG = "DateValidater";

    private static final Calendar today = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));

    private DateValidater() {

    }

    public static boolean isTimeStampValid(long timestamp) {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        cal.setTimeInMillis(timestamp * 1000);

        Log.i(TAG, "today.before(cal)=" + today.before(cal));

        if (today.before(cal) || isTodayOrTomorrow(timestamp)) {
            Log.i(TAG, "valid: " + dateToString(cal));
            return true;
        } else {
            Log.i(TAG, "not valid: " + dateToString(cal));
            return false;
        }
    }

    public static String getTimestampRepresentation(long timestamp) {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        int today_day = cal.get(Calendar.DAY_OF_MONTH);
        int today_month = cal.get(Calendar.MONTH);
        int today_year = cal.get(Calendar.YEAR);

        cal.setTimeInMillis(timestamp * 1000);
        int date_day = cal.get(Calendar.DAY_OF_MONTH);
        int date_month = cal.get(Calendar.MONTH);
        int date_year = cal.get(Calendar.YEAR);

        if (date_day == today_day && date_month == today_month && date_year == today_year) {
            return "Heute - " + dateToString(cal);
        } else if (date_day == today_day + 1 && date_month == today_month && date_year == today_year) {
            return "Morgen - " + dateToString(cal);
        } else {
            return dateToString(cal);
        }
    }

    private static boolean isTodayOrTomorrow(long timestamp) {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        int today_day = cal.get(Calendar.DAY_OF_MONTH);
        int today_month = cal.get(Calendar.MONTH);
        int today_year = cal.get(Calendar.YEAR);

        cal.setTimeInMillis(timestamp * 1000);
        int date_day = cal.get(Calendar.DAY_OF_MONTH);
        int date_month = cal.get(Calendar.MONTH);
        int date_year = cal.get(Calendar.YEAR);

        return date_day == today_day && date_month == today_month && date_year == today_year || date_day == today_day + 1 && date_month == today_month && date_year == today_year;
    }

    private static String dateToString(Calendar c) {
        DateFormat formatter = new SimpleDateFormat("EEEE', 'dd. MMMM", Locale.GERMANY);
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return formatter.format(c.getTime());
    }
}

package kg.kloop.android.openbudgetapp.utils;

import android.content.Context;
import android.text.format.DateUtils;

public class DateConverter {
    public static String getRelativeDateTimeString(Context context, long time) {
        return DateUtils.getRelativeDateTimeString(context, time, (long)1000, (long)(1000 * 60 * 60 * 24 * 2), 0).toString();
    }

}

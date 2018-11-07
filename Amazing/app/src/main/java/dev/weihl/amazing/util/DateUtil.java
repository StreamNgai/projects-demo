package dev.weihl.amazing.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Ngai
 * @since 2018/5/25
 * Des:
 */
public class DateUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

    public static String formatDate(Date date, String pattern) {
        dateFormat.applyPattern(pattern);
        return dateFormat.format(date);
    }
}

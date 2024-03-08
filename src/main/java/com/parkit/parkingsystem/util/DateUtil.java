package com.parkit.parkingsystem.util;

import java.util.Date;

public class DateUtil {
    /**
     * Get the difference between 2 dates in second
     * @param a A Date
     * @param b B Date
     * @return Difference in second
     */
    public static long getDatesDiff(Date a, Date b) {
        if(a == null || b == null) return 0;

        long time = a.getTime() - b.getTime();
        return (long)Math.ceil(time / 1000.0D);
    }

    public static long getDatesDiffInMinutes(Date a, Date b) {
        return (long)Math.ceil(getDatesDiff(a, b) / 60.0D);
    }
}

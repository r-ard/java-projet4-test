package com.parkit.parkingsystem;

import com.parkit.parkingsystem.util.DateUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class DateUtilTest {
    @Test
    public void dateDiffForOneHourInSecondsTest() {
        Date a = new Date();
        Date b = new Date(System.currentTimeMillis() - 60 * 60 * 1000);

        long result = DateUtil.getDatesDiff(a, b);

        assertEquals(result, 3600L);
    }

    @Test
    public void dateDiffForOneHourInMinutesTest() {
        Date a = new Date();
        Date b = new Date(System.currentTimeMillis() - 60 * 60 * 1000);

        long result = DateUtil.getDatesDiffInMinutes(a, b);

        assertEquals(result, 60L);
    }

    @Test
    public void dateDiffWithFirstDateNullTest() {
        Date b = new Date();

        long result = DateUtil.getDatesDiff(null, b);

        assertEquals(result, 0L);
    }

    @Test
    public void dateDiffWithSecondDateNullTest() {
        Date a = new Date();

        long result = DateUtil.getDatesDiff(a, null);

        assertEquals(result, 0L);
    }
}

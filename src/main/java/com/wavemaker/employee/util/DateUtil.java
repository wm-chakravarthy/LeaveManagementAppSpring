package com.wavemaker.employee.util;

import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.service.HolidayService;
import com.wavemaker.employee.service.impl.HolidayServiceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    private static final HolidayService holidayService = new HolidayServiceImpl();

    public static int calculateTotalDaysExcludingWeekendsAndHolidays(Date fromDate, Date toDate) throws ServerUnavilableException {
        LocalDate start = convertToLocalDate(fromDate);
        LocalDate end = convertToLocalDate(toDate);

        int totalDays = 0;
        LocalDate currentDate = start;

        while (!currentDate.isAfter(end)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                totalDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return totalDays - holidayService.getUpcomingHolidayDatesList().size();
    }

    private static LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
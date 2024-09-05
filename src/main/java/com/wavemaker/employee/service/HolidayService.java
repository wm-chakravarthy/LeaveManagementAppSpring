package com.wavemaker.employee.service;

import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.Holiday;

import java.util.Date;
import java.util.List;

public interface HolidayService {
    public List<Holiday> getHolidayList() throws ServerUnavilableException;

    public Holiday getHolidayById(int holidayId) throws ServerUnavilableException;

    public Holiday addHoliday(Holiday holiday) throws ServerUnavilableException;

    public List<Date> getListOfHolidayDates() throws ServerUnavilableException;

    public List<Date> getUpcomingHolidayDatesList() throws ServerUnavilableException;

    public List<Holiday> getUpcommingHolidayList() throws ServerUnavilableException;

}

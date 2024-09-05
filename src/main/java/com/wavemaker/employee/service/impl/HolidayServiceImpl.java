package com.wavemaker.employee.service.impl;

import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.factory.HolidayRepositoryInstanceHandler;
import com.wavemaker.employee.pojo.Holiday;
import com.wavemaker.employee.repository.HolidayRepository;
import com.wavemaker.employee.service.HolidayService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class HolidayServiceImpl implements HolidayService {
    private static HolidayRepository holidayRepository;

    public HolidayServiceImpl()  {
       try {
           holidayRepository = HolidayRepositoryInstanceHandler.getHolidayRepositoryInstance();
       } catch (SQLException e) {

       }
    }

    @Override
    public List<Holiday> getHolidayList() throws ServerUnavilableException {
        return holidayRepository.getHolidayList();
    }

    @Override
    public Holiday getHolidayById(int holidayId) throws ServerUnavilableException {
        return holidayRepository.getHolidayById(holidayId);
    }

    @Override
    public Holiday addHoliday(Holiday holiday) throws ServerUnavilableException {
        return holidayRepository.addHoliday(holiday);
    }

    @Override
    public List<Date> getListOfHolidayDates() throws ServerUnavilableException {
        return holidayRepository.getListOfHolidayDates();
    }

    @Override
    public List<Date> getUpcomingHolidayDatesList() throws ServerUnavilableException {
        return holidayRepository.getUpcomingHolidayDatesList();
    }

    @Override
    public List<Holiday> getUpcommingHolidayList() throws ServerUnavilableException {
        return holidayRepository.getUpcommingHolidayList();
    }
}

package com.wavemaker.employee.factory;

import com.wavemaker.employee.repository.HolidayRepository;
import com.wavemaker.employee.repository.impl.HolidayRepositoryImpl;

import java.sql.SQLException;

public class HolidayRepositoryInstanceHandler {
    private static volatile HolidayRepository holidayRepository;

    public static HolidayRepository getHolidayRepositoryInstance() throws SQLException {
        if (holidayRepository == null) {
            synchronized (HolidayRepositoryInstanceHandler.class) {
                if (holidayRepository == null) {
                    holidayRepository = new HolidayRepositoryImpl();
                }
            }
        }
        return holidayRepository;
    }
}

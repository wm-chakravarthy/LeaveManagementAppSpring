package com.wavemaker.employee.repository.impl;

import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.Holiday;
import com.wavemaker.employee.repository.HolidayRepository;
import com.wavemaker.employee.util.DBConnector;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HolidayRepositoryImpl implements HolidayRepository {
    private Connection connection;

    private static final String INSERT_HOLIDAY_QUERY = "INSERT INTO HOLIDAYS " +
            "(NAME, HOLIDAY_DATE, DESCRIPTION) VALUES (?, ?, ?)";

    private static final String SELECT_ALL_HOLIDAYS_QUERY =
            "SELECT HOLIDAY_ID, NAME, HOLIDAY_DATE, DESCRIPTION FROM HOLIDAYS";

    private static final String SELECT_HOLIDAY_BY_ID_QUERY =
            "SELECT HOLIDAY_ID, NAME, HOLIDAY_DATE, DESCRIPTION FROM HOLIDAYS WHERE HOLIDAY_ID = ?";

    private static final String DATE_FROM_HOLIDAYS_QUERY = "SELECT HOLIDAY_DATE FROM HOLIDAYS";

    private static final String SELECT_UPCOMING_HOLIDAY_LIST = "SELECT HOLIDAY_ID, NAME, HOLIDAY_DATE, DESCRIPTION " +
            "FROM holidays WHERE HOLIDAY_DATE >= CURDATE()";


    public HolidayRepositoryImpl() throws SQLException {
        connection = DBConnector.getConnectionInstance();
    }

    public Holiday addHoliday(Holiday holiday) throws ServerUnavilableException {
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_HOLIDAY_QUERY, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, holiday.getHolidayName());
            java.sql.Date sqlHolidayDate = new java.sql.Date(holiday.getHolidayDate().getTime());
            statement.setDate(2, sqlHolidayDate);
            statement.setString(3, holiday.getDescription());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        holiday.setHolidayId((int) generatedKeys.getLong(1));
                    }
                }
            }
            return holiday;

        } catch (SQLException e) {
            throw new ServerUnavilableException("Error adding holiday", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Date> getListOfHolidayDates() throws ServerUnavilableException {
        List<Date> holidayDates = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(DATE_FROM_HOLIDAYS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                holidayDates.add(resultSet.getDate("HOLIDAY_DATE"));
            }
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error retrieving holiday dates", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return holidayDates;
    }

    public List<Date> getUpcomingHolidayDatesList() throws ServerUnavilableException {
        List<Date> upcomingHolidays = new ArrayList<>();
        String query = "SELECT HOLIDAY_DATE FROM holidays WHERE HOLIDAY_DATE >= CURDATE()";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Date holidayDate = rs.getDate("HOLIDAY_DATE");
                upcomingHolidays.add(holidayDate);
            }
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error retrieving upcoming holidays", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return upcomingHolidays;
    }

    @Override
    public List<Holiday> getUpcommingHolidayList() throws ServerUnavilableException {
        List<Holiday> upcomingHolidays = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_UPCOMING_HOLIDAY_LIST);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Holiday holiday = new Holiday();
                holiday.setHolidayId(rs.getInt("HOLIDAY_ID"));
                holiday.setHolidayName(rs.getString("NAME"));
                holiday.setHolidayDate(rs.getDate("HOLIDAY_DATE"));
                holiday.setDescription(rs.getString("DESCRIPTION"));

                upcomingHolidays.add(holiday);
            }

        } catch (SQLException e) {
            throw new ServerUnavilableException("Error retrieving upcoming holidays", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return upcomingHolidays;
    }


    @Override
    public List<Holiday> getHolidayList() throws ServerUnavilableException {
        List<Holiday> holidays = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_HOLIDAYS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Holiday holiday = new Holiday();
                holiday.setHolidayId(resultSet.getInt("HOLIDAY_ID"));
                holiday.setHolidayName(resultSet.getString("NAME"));
                holiday.setHolidayDate(resultSet.getDate("HOLIDAY_DATE"));
                holiday.setDescription(resultSet.getString("DESCRIPTION"));
                holidays.add(holiday);
            }
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error retrieving holiday list", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return holidays;
    }

    @Override
    public Holiday getHolidayById(int holidayId) throws ServerUnavilableException {
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_HOLIDAY_BY_ID_QUERY);
            statement.setInt(1, holidayId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Holiday holiday = new Holiday();
                    holiday.setHolidayId(resultSet.getInt("HOLIDAY_ID"));
                    holiday.setHolidayName(resultSet.getString("NAME"));
                    holiday.setHolidayDate(resultSet.getDate("HOLIDAY_DATE"));
                    holiday.setDescription(resultSet.getString("DESCRIPTION"));
                    return holiday;
                }
            }
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error retrieving holiday by ID", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}

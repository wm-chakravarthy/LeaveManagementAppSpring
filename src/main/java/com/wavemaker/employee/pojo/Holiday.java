package com.wavemaker.employee.pojo;

import java.util.Date;
import java.util.Objects;

public class Holiday {
    private int holidayId;
    private String holidayName;
    private Date holidayDate;
    private String description;

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Holiday holiday = (Holiday) object;
        return holidayId == holiday.holidayId && Objects.equals(holidayName, holiday.holidayName) && Objects.equals(holidayDate, holiday.holidayDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holidayId, holidayName, holidayDate);
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "holidayId=" + holidayId +
                ", holidayName='" + holidayName + '\'' +
                ", holidayDate=" + holidayDate +
                ", description='" + description + '\'' +
                '}';
    }
}

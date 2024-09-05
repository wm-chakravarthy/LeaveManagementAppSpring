package com.wavemaker.employee.service;

import com.wavemaker.employee.exception.LeaveDaysExceededException;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.EmployeeLeaveSummary;
import com.wavemaker.employee.pojo.dto.EmployeeIdNameVO;

import java.util.List;
import java.util.Map;

public interface EmployeeLeaveSummaryService {
    public boolean updateEmployeeLeaveSummary(int empId, int leaveTypeId, int totalDays) throws ServerUnavilableException;

    public List<EmployeeLeaveSummary> getEmployeeLeaveSummariesById(int empId) throws ServerUnavilableException;

    public Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> getAllEmployeesLeaveSummary(int empId) throws ServerUnavilableException;

    public boolean isLeaveTypeWithinRange(int empId, int leaveTypeId, int totalDays) throws LeaveDaysExceededException, ServerUnavilableException;

}

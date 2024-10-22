package com.wavemaker.employee.service.impl;

import com.wavemaker.employee.exception.LeaveDaysExceededException;
import com.wavemaker.employee.exception.ServerUnavailableException;
import com.wavemaker.employee.pojo.EmployeeLeaveSummary;
import com.wavemaker.employee.pojo.dto.EmployeeIdNameVO;
import com.wavemaker.employee.repository.EmployeeLeaveSummaryRepository;
import com.wavemaker.employee.service.EmployeeLeaveSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeLeaveSummaryServiceImpl implements EmployeeLeaveSummaryService {

    @Autowired
    @Qualifier("employeeLeaveSummaryRepositoryInDB")
    private EmployeeLeaveSummaryRepository employeeLeaveSummaryRepository;

    @Override
    public boolean updateEmployeeLeaveSummary(int empId, int leaveTypeId, int totalDays) throws ServerUnavailableException {
        return employeeLeaveSummaryRepository.updateEmployeeLeaveSummary(empId, leaveTypeId, totalDays);
    }

    @Override
    public List<EmployeeLeaveSummary> getEmployeeLeaveSummariesById(int empId) throws ServerUnavailableException {
        return employeeLeaveSummaryRepository.getEmployeeLeaveSummariesById(empId);
    }

    @Override
    public Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> getAllEmployeesLeaveSummary(int empId) throws ServerUnavailableException {
        return employeeLeaveSummaryRepository.getAllEmployeesLeaveSummary(empId);
    }

    @Override
    public boolean isLeaveTypeWithinRange(int empId, int leaveTypeId, int totalDays) throws LeaveDaysExceededException, ServerUnavailableException {
        return employeeLeaveSummaryRepository.isLeaveTypeWithinRange(empId, leaveTypeId, totalDays);
    }
}

package com.wavemaker.employee.service.impl;

import com.wavemaker.employee.exception.LeaveDaysExceededException;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.LeaveType;
import com.wavemaker.employee.repository.LeaveTypeRepository;
import com.wavemaker.employee.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    @Autowired
    @Qualifier("leaveTypeRepositoryInDB")
    private LeaveTypeRepository leaveTypeRepository;

    @Override
    public List<LeaveType> getAllLeaveTypes(String gender) throws ServerUnavilableException {
        return leaveTypeRepository.getAllLeaveTypes(gender);
    }

    @Override
    public boolean isLeaveTypeWithInRange(int leaveTypeId, int totalDays) throws ServerUnavilableException, LeaveDaysExceededException {
        return leaveTypeRepository.isLeaveTypeWithInRange(leaveTypeId, totalDays);
    }
}

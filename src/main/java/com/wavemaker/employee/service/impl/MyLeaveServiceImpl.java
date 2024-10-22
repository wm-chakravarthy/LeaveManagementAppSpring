package com.wavemaker.employee.service.impl;

import com.wavemaker.employee.exception.LeaveDaysExceededException;
import com.wavemaker.employee.exception.ServerUnavailableException;
import com.wavemaker.employee.pojo.LeaveRequest;
import com.wavemaker.employee.pojo.dto.EmployeeLeaveRequestVO;
import com.wavemaker.employee.repository.MyLeaveRepository;
import com.wavemaker.employee.service.EmployeeLeaveSummaryService;
import com.wavemaker.employee.service.MyLeaveService;
import com.wavemaker.employee.util.DateUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyLeaveServiceImpl implements MyLeaveService {

    @Autowired
    @Qualifier("myLeaveRepositoryInDB")
    private MyLeaveRepository myLeaveRepository;

    @Autowired
    private EmployeeLeaveSummaryService employeeLeaveSummaryService;

    @Override
    public LeaveRequest applyForLeave(LeaveRequest leaveRequest) throws ServerUnavailableException, LeaveDaysExceededException {
        int totalDays = DateUtil.calculateTotalDaysExcludingWeekendsAndHolidays(leaveRequest.getFromDate(), leaveRequest.getToDate());
        int leaveTypeId = leaveRequest.getLeaveTypeId();
        boolean isSuccess = employeeLeaveSummaryService.isLeaveTypeWithinRange(leaveRequest.getEmpId(), leaveTypeId, totalDays);
        if (!isSuccess) {
            throw new LeaveDaysExceededException("Total days exceed the maximum allowed for this leave type.", HttpServletResponse.SC_BAD_REQUEST);
        }
        leaveRequest.setTotalNoOfDays(totalDays);
        return myLeaveRepository.applyForLeave(leaveRequest);
    }

    @Override
    public boolean cancelMyLeaveRequest(int leaveRequestId, int approvingEmpId) throws ServerUnavailableException {
        return myLeaveRepository.cancelMyLeaveRequest(leaveRequestId, approvingEmpId);
    }

    @Override
    public List<EmployeeLeaveRequestVO> getMyLeaveRequests(int empId, List<String> statusList) throws ServerUnavailableException {
        return myLeaveRepository.getMyLeaveRequests(empId, statusList);
    }

    @Override
    public boolean updateMyLeaveRequest(LeaveRequest leaveRequest) throws ServerUnavailableException {
        return myLeaveRepository.updateMyLeaveRequest(leaveRequest);
    }

    @Override
    public int getEmployeeIdByLeaveRequestId(int leaveRequestId) throws ServerUnavailableException {
        return myLeaveRepository.getEmployeeIdByLeaveRequestId(leaveRequestId);
    }

    @Override
    public List<Integer> getLeaveTypeIdAndTotalDaysByLeaveRequestId(int leaveRequestId) throws ServerUnavailableException {
        return myLeaveRepository.getLeaveTypeIdAndTotalDaysByLeaveRequestId(leaveRequestId);
    }


}

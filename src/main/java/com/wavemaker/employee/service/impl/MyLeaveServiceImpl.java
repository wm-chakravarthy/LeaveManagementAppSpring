package com.wavemaker.employee.service.impl;

import com.wavemaker.employee.exception.LeaveDaysExceededException;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.factory.MyLeaveRepositoryInstanceHandler;
import com.wavemaker.employee.pojo.LeaveRequest;
import com.wavemaker.employee.pojo.dto.EmployeeLeaveRequestVO;
import com.wavemaker.employee.repository.MyLeaveRepository;
import com.wavemaker.employee.service.EmployeeLeaveSummaryService;
import com.wavemaker.employee.service.MyLeaveService;
import com.wavemaker.employee.util.DateUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

public class MyLeaveServiceImpl implements MyLeaveService {
    private static MyLeaveRepository myLeaveRepository;
    private static EmployeeLeaveSummaryService employeeLeaveSummaryService;

    public MyLeaveServiceImpl() throws SQLException {
        employeeLeaveSummaryService = new EmployeeLeaveSummaryServiceImpl();
        myLeaveRepository = MyLeaveRepositoryInstanceHandler.getEmployeeLeaveRepositoryInstance();
    }

    @Override
    public LeaveRequest applyForLeave(LeaveRequest leaveRequest) throws ServerUnavilableException, LeaveDaysExceededException {
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
    public boolean cancelMyLeaveRequest(int leaveRequestId, int approvingEmpId) throws ServerUnavilableException {
        return myLeaveRepository.cancelMyLeaveRequest(leaveRequestId, approvingEmpId);
    }

    @Override
    public List<EmployeeLeaveRequestVO> getMyLeaveRequests(int empId, List<String> statusList) throws ServerUnavilableException {
        return myLeaveRepository.getMyLeaveRequests(empId, statusList);
    }

    @Override
    public boolean updateMyLeaveRequest(LeaveRequest leaveRequest) throws ServerUnavilableException {
        return myLeaveRepository.updateMyLeaveRequest(leaveRequest);
    }

    @Override
    public int getEmployeeIdByLeaveRequestId(int leaveRequestId) throws ServerUnavilableException {
        return myLeaveRepository.getEmployeeIdByLeaveRequestId(leaveRequestId);
    }

    @Override
    public List<Integer> getLeaveTypeIdAndTotalDaysByLeaveRequestId(int leaveRequestId) throws ServerUnavilableException {
        return myLeaveRepository.getLeaveTypeIdAndTotalDaysByLeaveRequestId(leaveRequestId);
    }


}

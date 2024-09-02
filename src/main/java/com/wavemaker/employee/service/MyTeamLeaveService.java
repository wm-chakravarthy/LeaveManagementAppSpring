package com.wavemaker.employee.service;

import com.wavemaker.employee.constants.LeaveRequestStatus;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.dto.LeaveRequestVO;

import java.util.List;

public interface MyTeamLeaveService {
    public List<LeaveRequestVO> getMyTeamLeaveRequests(int managerEmpId, String status) throws ServerUnavilableException;

    public boolean approveOrRejectTeamLeaveRequest(int leaveRequestId, int approvingEmpId, LeaveRequestStatus approveOrRejectOrCancel) throws ServerUnavilableException;

}
package com.wavemaker.employee.controller;

import com.google.gson.Gson;
import com.wavemaker.employee.constants.LeaveRequestStatus;
import com.wavemaker.employee.exception.ErrorResponse;
import com.wavemaker.employee.exception.LeaveDaysExceededException;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.UserEntity;
import com.wavemaker.employee.pojo.dto.LeaveRequestVO;
import com.wavemaker.employee.service.MyTeamLeaveService;
import com.wavemaker.employee.service.impl.MyTeamLeaveServiceImpl;
import com.wavemaker.employee.util.ClientResponseHandler;
import com.wavemaker.employee.util.UserSessionHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/employee/my-team-leave")
public class MyTeamLeavesServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MyLeavesServlet.class);

    private Gson gson;

    @Autowired
    private MyTeamLeaveService myTeamLeaveService;

    @Override
    public void init(ServletConfig config) {
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String statusParam = request.getParameter("status");
        List<String> statusList = new ArrayList<>();
        if (statusParam != null && !statusParam.isEmpty()) {
            statusList = Arrays.asList(statusParam.split(","));
        } else {
            statusList.add("APPROVED");
            statusList.add("REJECTED");
            statusList.add("PENDING");
            statusList.add("CANCELLED");
        }
        List<LeaveRequestVO> leaveRequestList = null;
        UserEntity userEntity = null;
        String jsonResponse = null;
        try {
            userEntity = UserSessionHandler.handleUserSessionAndReturnUserEntity(request, response, logger);


            leaveRequestList = myTeamLeaveService.getMyTeamLeaveRequests(userEntity.getEmpId(), statusList);
            jsonResponse = gson.toJson(leaveRequestList);


        } catch (ServerUnavilableException e) {
            logger.error("Error fetching Leave details for user ID: {}", userEntity != null ? userEntity.getUserId() : "Unknown", e);
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500);
            jsonResponse = gson.toJson(errorResponse);
        } catch (Exception e) {
            logger.error("Server error occurred while processing GET request", e);
            ErrorResponse errorResponse = new ErrorResponse("Server Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(500);
            jsonResponse = gson.toJson(errorResponse);
        } finally {
            statusList = null;
            leaveRequestList = null;
            userEntity = null;
            ClientResponseHandler.sendResponseToClient(response, jsonResponse, logger);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String leaveRequestId = request.getParameter("leaveRequestId");
        String approveOrReject = request.getParameter("approveOrReject");
        String jsonResponse = null;
        UserEntity userEntity = null;
        try {
            userEntity = UserSessionHandler.handleUserSessionAndReturnUserEntity(request, response, logger);

            if (leaveRequestId != null && approveOrReject != null) {
                boolean isSuccess = myTeamLeaveService.approveOrRejectTeamLeaveRequest(Integer.parseInt(leaveRequestId), userEntity.getEmpId(), LeaveRequestStatus.valueOf(approveOrReject));
                jsonResponse = gson.toJson(isSuccess);
            }
        } catch (LeaveDaysExceededException e) {
            logger.error("Error in approving or rejecting leave request ID: {}", leaveRequestId, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse = gson.toJson(errorResponse);
        } catch (ServerUnavilableException e) {
            logger.error("Error fetching Leave details for user ID: {}", userEntity != null ? userEntity.getUserId() : "Unknown", e);
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), 500);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse = gson.toJson(errorResponse);
        } catch (Exception e) {
            logger.error("Server error occurred while processing GET request", e);
            ErrorResponse errorResponse = new ErrorResponse("Server Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setStatus(500);
            jsonResponse = gson.toJson(errorResponse);
        } finally {
            userEntity = null;
            approveOrReject = null;
            leaveRequestId = null;
            ClientResponseHandler.sendResponseToClient(response, jsonResponse, logger);
        }
    }
}

package com.wavemaker.employee.controller;

import com.google.gson.Gson;
import com.wavemaker.employee.exception.ErrorResponse;
import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.EmployeeLeaveSummary;
import com.wavemaker.employee.pojo.UserEntity;
import com.wavemaker.employee.pojo.dto.EmployeeIdNameVO;
import com.wavemaker.employee.service.EmployeeLeaveSummaryService;
import com.wavemaker.employee.service.impl.EmployeeLeaveSummaryServiceImpl;
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

import java.util.List;
import java.util.Map;

@WebServlet("/employee/team/leave/summary")
public class TeamLeaveSummaryServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TeamLeaveSummaryServlet.class);
    private Gson gson = null;

    @Autowired
    private EmployeeLeaveSummaryService employeeLeaveSummaryService;

    @Override
    public void init(ServletConfig config) {
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String empId = request.getParameter("empId");
        UserEntity userEntity = null;
        String jsonResponse = null;
        List<EmployeeLeaveSummary> employeeLeaveSummaryList = null;
        Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> leaveSummaryMap = null;
        try {
            userEntity = UserSessionHandler.handleUserSessionAndReturnUserEntity(request, response, logger);
            if (empId != null) {
                logger.info("Fetching leave summary for employee ID: {}", empId);
                employeeLeaveSummaryList = employeeLeaveSummaryService.getEmployeeLeaveSummariesById(Integer.parseInt(empId));
                jsonResponse = gson.toJson(employeeLeaveSummaryList);
                employeeLeaveSummaryList = null;
                ClientResponseHandler.sendResponseToClient(response, jsonResponse, logger);
                return;
            }
            leaveSummaryMap = employeeLeaveSummaryService.getAllEmployeesLeaveSummary(userEntity.getEmpId());
            jsonResponse = gson.toJson(leaveSummaryMap);
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
            userEntity = null;
            leaveSummaryMap = null;
            ClientResponseHandler.sendResponseToClient(response, jsonResponse, logger);
        }
    }
}

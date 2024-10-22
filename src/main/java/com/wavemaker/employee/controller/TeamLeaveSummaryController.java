package com.wavemaker.employee.controller;

import com.wavemaker.employee.exception.ServerUnavailableException;
import com.wavemaker.employee.pojo.EmployeeLeaveSummary;
import com.wavemaker.employee.pojo.UserEntity;
import com.wavemaker.employee.pojo.dto.EmployeeIdNameVO;
import com.wavemaker.employee.service.EmployeeLeaveSummaryService;
import com.wavemaker.employee.util.UserSessionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee/team/leave/summary")
public class TeamLeaveSummaryController {

    private static final Logger logger = LoggerFactory.getLogger(TeamLeaveSummaryController.class);

    @Autowired
    private EmployeeLeaveSummaryService employeeLeaveSummaryService;

    @GetMapping
    public List<EmployeeLeaveSummary> getEmployeeLeaveSummariesById(
            @RequestParam(value = "empId", required = false) String empId,
            HttpServletRequest request, HttpServletResponse response) throws ServerUnavailableException {

        List<EmployeeLeaveSummary> employeeLeaveSummaryList = null;
        logger.info("Fetching leave summary for employee ID: {}", empId);
        employeeLeaveSummaryList = employeeLeaveSummaryService.getEmployeeLeaveSummariesById(Integer.parseInt(empId));
        logger.info("Leave summary fetched: {}", employeeLeaveSummaryList);
        return employeeLeaveSummaryList;
    }

    @GetMapping("/list")
    public Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> getAllEmployeesLeaveSummary(HttpServletRequest request, HttpServletResponse response) throws ServerUnavailableException {
        Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> leaveSummaryMap = null;
        UserEntity userEntity = null;

        userEntity = UserSessionHandler.handleUserSessionAndReturnUserEntity(request, response, logger);
        logger.info("Fetching leave summary for employee : {}", userEntity);
        leaveSummaryMap = employeeLeaveSummaryService.getAllEmployeesLeaveSummary(userEntity.getEmpId());
        logger.info("Leave summary fetched: {}", leaveSummaryMap);
        return leaveSummaryMap;
    }
}

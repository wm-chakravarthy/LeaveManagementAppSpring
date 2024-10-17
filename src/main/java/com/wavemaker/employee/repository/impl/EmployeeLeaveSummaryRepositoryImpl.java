package com.wavemaker.employee.repository.impl;

import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.EmployeeLeaveSummary;
import com.wavemaker.employee.pojo.dto.EmployeeIdNameVO;
import com.wavemaker.employee.repository.EmployeeLeaveSummaryRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("employeeLeaveSummaryRepositoryInDB")
public class EmployeeLeaveSummaryRepositoryImpl implements EmployeeLeaveSummaryRepository {

    private static final String SQL_SELECT_EMPLOYEE_LEAVE_SUMMARY =
            "SELECT SUMMARY_ID, EMP_ID, LEAVE_TYPE_ID, LEAVE_TYPE, PENDING_LEAVES, TOTAL_LEAVES_TAKEN, LAST_UPDATED " +
                    "FROM employee_leave_summary " +
                    "WHERE EMP_ID = ?";

    private static final String EMPLOYEES_QUERY = "SELECT `EMP_ID`, `NAME` " +
            "FROM `employee` " +
            "WHERE `MANAGER_ID` = ?";

    private static final String LEAVE_SUMMARIES_QUERY = "SELECT `SUMMARY_ID`, `EMP_ID`, `LEAVE_TYPE_ID`, `LEAVE_TYPE`, `PENDING_LEAVES`, `TOTAL_LEAVES_TAKEN`, `LAST_UPDATED` " +
            "FROM `employee_leave_summary` " +
            "WHERE `EMP_ID` = ?";

    private static final String QUERY_PENDING_LEAVES =
            "SELECT PENDING_LEAVES FROM employee_leave_summary WHERE EMP_ID = ? AND LEAVE_TYPE_ID = ?";

    private static final String CHECK_QUERY = "SELECT PENDING_LEAVES, TOTAL_LEAVES_TAKEN " +
            "FROM EMPLOYEE_LEAVE_SUMMARY " +
            "WHERE EMP_ID = ? AND LEAVE_TYPE_ID = ?";

    private static final String UPDATE_QUERY = "UPDATE EMPLOYEE_LEAVE_SUMMARY " +
            "SET PENDING_LEAVES = PENDING_LEAVES - ?, " +
            "TOTAL_LEAVES_TAKEN = TOTAL_LEAVES_TAKEN + ?, " +
            "LAST_UPDATED = CURRENT_TIMESTAMP " +
            "WHERE EMP_ID = ? AND LEAVE_TYPE_ID = ?";

    @Autowired
    private Connection connection;

    @Override
    public boolean updateEmployeeLeaveSummary(int empId, int leaveTypeId, int totalDays) throws ServerUnavilableException {
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try {
            checkStmt = connection.prepareStatement(CHECK_QUERY);
            checkStmt.setInt(1, empId);
            checkStmt.setInt(2, leaveTypeId);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                updateStmt = connection.prepareStatement(UPDATE_QUERY);
                updateStmt.setInt(1, totalDays);
                updateStmt.setInt(2, totalDays);
                updateStmt.setInt(3, empId);
                updateStmt.setInt(4, leaveTypeId);

                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new ServerUnavilableException("Could not update employee leave summary", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public List<EmployeeLeaveSummary> getEmployeeLeaveSummariesById(int empId) throws ServerUnavilableException {
        List<EmployeeLeaveSummary> summaries = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_EMPLOYEE_LEAVE_SUMMARY);
            preparedStatement.setInt(1, empId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EmployeeLeaveSummary summary = new EmployeeLeaveSummary();
                summary.summaryId = resultSet.getInt("SUMMARY_ID");
                summary.empId = resultSet.getInt("EMP_ID");
                summary.leaveTypeId = resultSet.getInt("LEAVE_TYPE_ID");
                summary.leaveType = resultSet.getString("LEAVE_TYPE");
                summary.pendingLeaves = resultSet.getInt("PENDING_LEAVES");
                summary.totalLeavesTaken = resultSet.getInt("TOTAL_LEAVES_TAKEN");
                summary.lastUpdated = resultSet.getTimestamp("LAST_UPDATED");

                summaries.add(summary); // Add each summary to the list
            }
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error fetching employee leave summary", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return summaries;
    }

    @Override
    public Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> getAllEmployeesLeaveSummary(int managerId) throws ServerUnavilableException {
        try {
            PreparedStatement employeesStatement = connection.prepareStatement(EMPLOYEES_QUERY);
            employeesStatement.setInt(1, managerId);
            ResultSet employeesResultSet = employeesStatement.executeQuery();
            Map<EmployeeIdNameVO, List<EmployeeLeaveSummary>> resultMap = new HashMap<>();

            while (employeesResultSet.next()) {
                int empId = employeesResultSet.getInt("EMP_ID");
                String empName = employeesResultSet.getString("NAME");

                EmployeeIdNameVO empVO = new EmployeeIdNameVO();
                empVO.setEmpId(empId);
                empVO.setEmpName(empName);

                PreparedStatement leaveSummariesStatement = connection.prepareStatement(LEAVE_SUMMARIES_QUERY);
                leaveSummariesStatement.setInt(1, empId);
                ResultSet leaveSummariesResultSet = leaveSummariesStatement.executeQuery();

                List<EmployeeLeaveSummary> summaries = new ArrayList<>();
                while (leaveSummariesResultSet.next()) {
                    EmployeeLeaveSummary summary = new EmployeeLeaveSummary();
                    summary.summaryId = leaveSummariesResultSet.getInt("SUMMARY_ID");
                    summary.empId = empId;
                    summary.leaveTypeId = leaveSummariesResultSet.getInt("LEAVE_TYPE_ID");
                    summary.leaveType = leaveSummariesResultSet.getString("LEAVE_TYPE");
                    summary.pendingLeaves = leaveSummariesResultSet.getInt("PENDING_LEAVES");
                    summary.totalLeavesTaken = leaveSummariesResultSet.getInt("TOTAL_LEAVES_TAKEN");
                    summary.lastUpdated = leaveSummariesResultSet.getTimestamp("LAST_UPDATED");

                    summaries.add(summary);
                }

                resultMap.put(empVO, summaries);
            }

            return resultMap;
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error fetching all employee leave summaries", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isLeaveTypeWithinRange(int empId, int leaveTypeId, int totalDays) throws ServerUnavilableException {
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_PENDING_LEAVES);
            statement.setInt(1, empId);
            statement.setInt(2, leaveTypeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int pendingLeaves = resultSet.getInt("PENDING_LEAVES");
                    return totalDays <= pendingLeaves;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new ServerUnavilableException("Error checking leave type range", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}

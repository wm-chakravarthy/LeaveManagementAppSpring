package com.wavemaker.employee.service.impl;

import com.wavemaker.employee.exception.ServerUnavilableException;
import com.wavemaker.employee.pojo.Employee;
import com.wavemaker.employee.pojo.dto.EmployeeVO;
import com.wavemaker.employee.repository.EmployeeRepository;
import com.wavemaker.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    @Qualifier("employeeRepositoryInDB")
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeVO getEmployeeById(int empId) throws ServerUnavilableException {
        return employeeRepository.getEmployeeById(empId);
    }

    @Override
    public Employee addEmployee(Employee employee) throws ServerUnavilableException {
        return employeeRepository.addEmployee(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) throws ServerUnavilableException {
        return employeeRepository.updateEmployee(employee);
    }

    @Override
    public Employee deleteEmployee(int empId) throws ServerUnavilableException {
        return employeeRepository.deleteEmployee(empId);
    }

    @Override
    public List<Employee> getEmployees() throws ServerUnavilableException {
        return employeeRepository.getEmployees();
    }

    @Override
    public List<Employee> getAllManagers() throws ServerUnavilableException {
        return employeeRepository.getAllManagers();
    }
}

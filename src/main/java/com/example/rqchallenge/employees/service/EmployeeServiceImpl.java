package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.client.EmployeeClient;
import com.example.rqchallenge.employees.entity.*;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements IEmployeeService{

    private final EmployeeClient employeeClient;

    @Autowired
    public EmployeeServiceImpl(EmployeeClient employeeClient) {
        this.employeeClient = employeeClient;
    }

    @Override
    public EmployeeResponseForCreate createEmployee(EmployeeRequest employeeRequest) {
        return employeeClient.createEmployee(employeeRequest);
    }

    @Override
    public EmployeeResponseForList getAllEmployees() throws IOException {
        return employeeClient.getAllEmployees();
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) throws IOException {
        return getAllEmployees().getData().stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(String id) {
        return employeeClient.getEmployeeById(id);
    }

    @Override
    public Integer getHighestSalaryOfEmployees() throws IOException {
        return getAllEmployees().getData().stream()
                .map(Employee::getEmployeeSalary)
                .map(Integer::valueOf)
                .max(Integer::compare)
                .orElseThrow(() -> new EmployeeNotFoundException("No employee data found to determine the highest salary."));
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() throws IOException {
        return getAllEmployees().getData().stream()
                .sorted((e1, e2) -> Integer.valueOf(e2.getEmployeeSalary()).compareTo(Integer.valueOf(e1.getEmployeeSalary())))
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseForDelete deleteEmployeeById(String id) {
        return employeeClient.deleteEmployeeById(id);
    }
}

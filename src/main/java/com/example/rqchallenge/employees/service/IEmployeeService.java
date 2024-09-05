package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.entity.*;

import java.io.IOException;
import java.util.List;

public interface IEmployeeService {
    EmployeeResponseForList getAllEmployees() throws IOException;
    List<Employee> getEmployeesByNameSearch(String searchString) throws IOException;
    EmployeeResponse getEmployeeById(String id);
    Integer getHighestSalaryOfEmployees() throws IOException;
    List<String> getTopTenHighestEarningEmployeeNames() throws IOException;
    EmployeeResponseForCreate createEmployee(EmployeeRequest employeeRequest);
    EmployeeResponseForDelete deleteEmployeeById(String id);

}

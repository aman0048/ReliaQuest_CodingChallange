package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.entity.*;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.InvalidEmployeeDataException;
import com.example.rqchallenge.employees.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class EmployeeControllerImpl implements IEmployeeController{
    private final IEmployeeService employeeService;

    @Autowired
    public EmployeeControllerImpl(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<EmployeeResponseForList> getAllEmployees() throws IOException {
        log.info("Fetching all employees");
        EmployeeResponseForList employees = employeeService.getAllEmployees();
        if (employees == null || employees.getData().isEmpty()){
            throw new EmployeeNotFoundException("No employees found.");
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) throws IOException {
        log.info("Searching employees with name containing: {}", searchString);
        if (StringUtils.isBlank(searchString)) {
            throw new InvalidEmployeeDataException("Search string cannot be null or empty.");
        }
        List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees found with the given search string.");
        }
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<EmployeeResponse> getEmployeeById(String id) {
        log.info("Fetching Employee By Id: {}", id);
        validateId(id);
        EmployeeResponse employeeResponse = employeeService.getEmployeeById(id);
        if (employeeResponse == null) {
            throw new EmployeeNotFoundException("Employee not found with Id: " + id);
        }
        return ResponseEntity.ok(employeeResponse);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() throws IOException {
        log.info("Fetching the highest salary of employees");
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws IOException {
        log.info("Fetching top ten highest earning employee names");
        List<String> topTenHighestEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
        if (topTenHighestEmployeeNames == null || topTenHighestEmployeeNames.isEmpty()) {
            throw new EmployeeNotFoundException("No employee names found for the top ten highest earners.");
        }
        return ResponseEntity.ok(topTenHighestEmployeeNames);
    }

    @Override
    public ResponseEntity<EmployeeResponseForCreate> createEmployee(Map<String, Object> employeeInput) {
        log.info("Creating a new employee with input: {}", employeeInput);
        validateEmployeeInput(employeeInput);

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName(employeeInput.get("name").toString());
        employeeRequest.setSalary(employeeInput.get("salary").toString());
        employeeRequest.setAge(employeeInput.get("age").toString());

        validateEmployeeRequest(employeeRequest);
        EmployeeResponseForCreate employee = employeeService.createEmployee(employeeRequest);
        return new ResponseEntity<EmployeeResponseForCreate>(employee, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EmployeeResponseForDelete> deleteEmployeeById(String id) {
        log.info("Deleting an employee with Id: {}", id);
        validateId(id);
        EmployeeResponseForDelete employeeResponseForDelete = employeeService.deleteEmployeeById(id);
        if (employeeResponseForDelete == null) {
            throw new EmployeeNotFoundException("Employee not found with Id: " + id);
        }
        return ResponseEntity.ok(employeeResponseForDelete);
    }

    private void validateId(String id) {
        if (StringUtils.isBlank(id)) {
            throw new InvalidEmployeeDataException("Employee Id cannot be null or empty.");
        }
    }

    private void validateEmployeeInput(Map<String, Object> employeeInput) {
        if (employeeInput == null || !employeeInput.containsKey("name") || !employeeInput.containsKey("salary") || !employeeInput.containsKey("age")) {
            throw new InvalidEmployeeDataException("Invalid employee input. 'name', 'salary', and 'age' are required.");
        }
    }

    private void validateEmployeeRequest(EmployeeRequest employeeRequest) {
        if (StringUtils.isBlank(employeeRequest.getName())) {
            throw new InvalidEmployeeDataException("Employee name cannot be null or empty.");
        }
        if (StringUtils.isBlank(employeeRequest.getSalary())) {
            throw new InvalidEmployeeDataException("Employee salary cannot be null or empty.");
        }
        int salary = Integer.parseInt(employeeRequest.getSalary());
        if (salary <= 0) {
            throw new InvalidEmployeeDataException("Employee salary must be a positive number.");
        }
        if (StringUtils.isBlank(employeeRequest.getAge())) {
            throw new InvalidEmployeeDataException("Employee age cannot be null or empty.");
        }
    }
}

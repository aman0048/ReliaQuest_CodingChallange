package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("employee")
public interface IEmployeeController {

    @GetMapping("/getAllEmployees")
    ResponseEntity<EmployeeResponseForList> getAllEmployees() throws IOException;

    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) throws IOException;

    @GetMapping("/{id}")
    ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id);

    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees() throws IOException;

    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() throws IOException;

    @PostMapping("/create")
    ResponseEntity<EmployeeResponseForCreate> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<EmployeeResponseForDelete> deleteEmployeeById(@PathVariable String id);

}

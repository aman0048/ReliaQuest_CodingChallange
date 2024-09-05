package com.example.rqchallenge.controller;

import com.example.rqchallenge.employees.controller.EmployeeControllerImpl;
import com.example.rqchallenge.employees.entity.*;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exception.InvalidEmployeeDataException;
import com.example.rqchallenge.employees.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeControllerImplTest {
    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeControllerImpl employeeController;

    private EmployeeResponseForList mockEmployeeResponseForList;
    private EmployeeResponse mockEmployeeResponse;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock data for tests
        Employee employee = new Employee();
        employee.setId("1");
        employee.setEmployeeName("Aman Garg");
        employee.setEmployeeSalary("5000");
        employee.setEmployeeAge("30");

        mockEmployeeResponse = new EmployeeResponse();
        mockEmployeeResponse.setData(employee);

        mockEmployeeResponseForList = new EmployeeResponseForList();
        mockEmployeeResponseForList.setData(Collections.singletonList(employee));
    }

    @Test
    public void testGetAllEmployeesSuccess() throws IOException, IOException {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(mockEmployeeResponseForList);

        ResponseEntity<EmployeeResponseForList> response = employeeController.getAllEmployees();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Aman Garg", response.getBody().getData().get(0).getEmployeeName());
    }

    @Test
    public void testGetEmployeesByNameSearchSuccess() throws IOException {
        Mockito.when(employeeService.getEmployeesByNameSearch("Aman")).thenReturn(Collections.singletonList(mockEmployeeResponse.getData()));

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch("Aman");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Aman Garg", response.getBody().get(0).getEmployeeName());
    }

    @Test
    public void testGetEmployeesByNameSearchNotFound() throws IOException {
        Mockito.when(employeeService.getEmployeesByNameSearch("Unknown")).thenReturn(Collections.emptyList());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeController.getEmployeesByNameSearch("Unknown");
        });

        assertEquals("No employees found with the given search string.", exception.getMessage());
    }

    @Test
    public void testGetEmployeeByIdSuccess() {
        Mockito.when(employeeService.getEmployeeById("1")).thenReturn(mockEmployeeResponse);

        ResponseEntity<EmployeeResponse> response = employeeController.getEmployeeById("1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Aman Garg", response.getBody().getData().getEmployeeName());
    }

    @Test
    public void testGetEmployeeByIdNotFound() {
        Mockito.when(employeeService.getEmployeeById("0")).thenReturn(null);

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeController.getEmployeeById("0");
        });

        assertEquals("Employee not found with Id: 0", exception.getMessage());
    }
    @Test
    public void testCreateEmployeeSuccess() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Aman Garg");
        employeeInput.put("salary", "5000");
        employeeInput.put("age", "30");

        EmployeeDataForCreate employeeData = new EmployeeDataForCreate();
        employeeData.setName("Aman Garg");
        employeeData.setSalary("5000");
        employeeData.setAge("30");
        employeeData.setId("1");

        EmployeeResponseForCreate mockEmployeeResponseForCreate = new EmployeeResponseForCreate();
        mockEmployeeResponseForCreate.setStatus("success");
        mockEmployeeResponseForCreate.setData(employeeData);

        Mockito.when(employeeService.createEmployee(Mockito.any(EmployeeRequest.class)))
                .thenReturn(mockEmployeeResponseForCreate);

        ResponseEntity<EmployeeResponseForCreate> response = employeeController.createEmployee(employeeInput);
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Aman Garg", response.getBody().getData().getName());
        assertEquals("success", response.getBody().getStatus());
    }

    @Test
    public void testCreateEmployeeInvalidData() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "");
        employeeInput.put("salary", "5000");
        employeeInput.put("age", "30");

        InvalidEmployeeDataException exception = assertThrows(InvalidEmployeeDataException.class, () -> {
            employeeController.createEmployee(employeeInput);
        });

        assertEquals("Employee name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testDeleteEmployeeByIdSuccess() {
        EmployeeResponseForDelete mockEmployeeResponseForDelete = new EmployeeResponseForDelete();
        mockEmployeeResponseForDelete.setMessage("Successfully deleted");

        Mockito.when(employeeService.deleteEmployeeById("1"))
                .thenReturn(mockEmployeeResponseForDelete);

        ResponseEntity<EmployeeResponseForDelete> response = employeeController.deleteEmployeeById("1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Successfully deleted", response.getBody().getMessage());
    }

    @Test
    public void testDeleteEmployeeByIdNotFound() {
        Mockito.when(employeeService.deleteEmployeeById("99")).thenReturn(null);

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeController.deleteEmployeeById("99");
        });

        assertEquals("Employee not found with Id: 99", exception.getMessage());
    }
}

package com.example.rqchallenge.service;

import com.example.rqchallenge.employees.client.EmployeeClient;
import com.example.rqchallenge.employees.entity.*;
import com.example.rqchallenge.employees.exception.EmployeeNotFoundException;
import com.example.rqchallenge.employees.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee_Success() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        EmployeeResponseForCreate expectedResponse = new EmployeeResponseForCreate();

        when(employeeClient.createEmployee(employeeRequest)).thenReturn(expectedResponse);

        EmployeeResponseForCreate actualResponse = employeeService.createEmployee(employeeRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(employeeClient, times(1)).createEmployee(employeeRequest);
    }

    @Test
    void testGetAllEmployees_Success() throws IOException {
        EmployeeResponseForList expectedResponse = new EmployeeResponseForList();

        when(employeeClient.getAllEmployees()).thenReturn(expectedResponse);

        EmployeeResponseForList actualResponse = employeeService.getAllEmployees();

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(employeeClient, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeesByNameSearch_Success() throws IOException {
        Employee employee1 = setEmp1();
        Employee employee2 = setEmp2();
        EmployeeResponseForList employeeResponse = new EmployeeResponseForList();
        employeeResponse.setData(Arrays.asList(employee1, employee2));

        when(employeeClient.getAllEmployees()).thenReturn(employeeResponse);

        List<Employee> result = employeeService.getEmployeesByNameSearch("Aman");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(employeeClient, times(1)).getAllEmployees();
    }


    @Test
    void testGetEmployeeById_Success() {
        String employeeId = "1";
        EmployeeResponse expectedResponse = new EmployeeResponse();

        when(employeeClient.getEmployeeById(employeeId)).thenReturn(expectedResponse);

        EmployeeResponse actualResponse = employeeService.getEmployeeById(employeeId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(employeeClient, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void testGetHighestSalaryOfEmployees_Success() throws IOException {
        Employee employee1 = setEmp1();
        Employee employee2 = setEmp2();
        EmployeeResponseForList employeeResponse = new EmployeeResponseForList();
        employeeResponse.setData(Arrays.asList(employee1, employee2));

        when(employeeClient.getAllEmployees()).thenReturn(employeeResponse);

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        assertNotNull(highestSalary);
        assertEquals(5000, highestSalary);
        verify(employeeClient, times(1)).getAllEmployees();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_Success() throws IOException {
        Employee employee1 = setEmp1();
        Employee employee2 = setEmp2();
        Employee employee3 = setEmp3();
        EmployeeResponseForList employeeResponse = new EmployeeResponseForList();
        employeeResponse.setData(Arrays.asList(employee1, employee2, employee3));

        when(employeeClient.getAllEmployees()).thenReturn(employeeResponse);

        List<String> topEarners = employeeService.getTopTenHighestEarningEmployeeNames();

        assertNotNull(topEarners);
        assertEquals(3, topEarners.size());
        assertEquals("Sagar Garg", topEarners.get(0));
        verify(employeeClient, times(1)).getAllEmployees();
    }

    @Test
    void testDeleteEmployeeById_Success() {
        String employeeId = "1";
        EmployeeResponseForDelete expectedResponse = new EmployeeResponseForDelete();

        when(employeeClient.deleteEmployeeById(employeeId)).thenReturn(expectedResponse);

        EmployeeResponseForDelete actualResponse = employeeService.deleteEmployeeById(employeeId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(employeeClient, times(1)).deleteEmployeeById(employeeId);
    }

    @Test
    void testGetHighestSalaryOfEmployees_ThrowsEmployeeNotFoundException() throws IOException {
        EmployeeResponseForList employeeResponse = new EmployeeResponseForList();
        employeeResponse.setData(Collections.emptyList());
        when(employeeClient.getAllEmployees()).thenReturn(employeeResponse);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getHighestSalaryOfEmployees());
        verify(employeeClient, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeesByNameSearch_NoMatches() throws IOException {
        Employee employee1 = setEmp1();
        Employee employee2 = setEmp2();
        EmployeeResponseForList employeeResponse = new EmployeeResponseForList();
        employeeResponse.setData(Arrays.asList(employee1, employee2));

        when(employeeClient.getAllEmployees()).thenReturn(employeeResponse);

        List<Employee> result = employeeService.getEmployeesByNameSearch("Smith");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeClient, times(1)).getAllEmployees();
    }



    private static Employee setEmp1(){
        Employee employee = new Employee();
        employee.setId("1");
        employee.setEmployeeName("Aman Garg");
        employee.setEmployeeSalary("5000");
        employee.setEmployeeAge("30");
        return employee;
    }

    private static Employee setEmp2(){
        Employee employee = new Employee();
        employee.setId("2");
        employee.setEmployeeName("Aman Garg");
        employee.setEmployeeSalary("4000");
        employee.setEmployeeAge("25");
        return employee;
    }
    private static Employee setEmp3(){
        Employee employee = new Employee();
        employee.setId("3");
        employee.setEmployeeName("Sagar Garg");
        employee.setEmployeeSalary("7000");
        employee.setEmployeeAge("27");
        return employee;
    }
}

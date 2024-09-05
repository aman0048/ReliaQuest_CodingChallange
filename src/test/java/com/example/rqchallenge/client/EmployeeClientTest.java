package com.example.rqchallenge.client;

import com.example.rqchallenge.employees.client.EmployeeClient;
import com.example.rqchallenge.employees.entity.*;
import com.example.rqchallenge.employees.exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmployeeClientTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeClient employeeClient;

    private static final String dummyAPIBaseUrl = "https://dummy.restapiexample.com/api/v1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(employeeClient, "dummyAPIBaseUrl", dummyAPIBaseUrl);
    }

    @Test
    void testCreateEmployee_Success() {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        EmployeeResponseForCreate expectedResponse = new EmployeeResponseForCreate();
        ResponseEntity<EmployeeResponseForCreate> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.CREATED);

        String expectedUrl = dummyAPIBaseUrl + "/create";
        Mockito.when(restTemplate.postForEntity(Mockito.eq(expectedUrl), Mockito.eq(employeeRequest), Mockito.eq(EmployeeResponseForCreate.class)))
                .thenReturn(responseEntity);

        EmployeeResponseForCreate actualResponse = employeeClient.createEmployee(employeeRequest);

        assertNotNull(actualResponse);
        verify(restTemplate, times(1)).postForEntity(Mockito.eq(expectedUrl), Mockito.eq(employeeRequest), Mockito.eq(EmployeeResponseForCreate.class));
    }

    @Test
    void testCreateEmployee_HttpClientErrorException() {
        EmployeeRequest employeeRequest = new EmployeeRequest();

        String expectedUrl = dummyAPIBaseUrl + "/create";
        Mockito.when(restTemplate.postForEntity(Mockito.eq(expectedUrl), Mockito.eq(employeeRequest), Mockito.eq(EmployeeResponseForCreate.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(ExternalServiceException.class, () -> employeeClient.createEmployee(employeeRequest));

        verify(restTemplate, times(1)).postForEntity(Mockito.eq(expectedUrl), Mockito.eq(employeeRequest), Mockito.eq(EmployeeResponseForCreate.class));
    }

    @Test
    void testGetAllEmployees_Success() throws IOException {
        EmployeeResponseForList expectedResponse = new EmployeeResponseForList();
        ResponseEntity<EmployeeResponseForList> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        String expectedUrl = dummyAPIBaseUrl + "/employees";
        Mockito.when(restTemplate.getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponseForList.class)))
                .thenReturn(responseEntity);

        EmployeeResponseForList actualResponse = employeeClient.getAllEmployees();

        assertNotNull(actualResponse);
        verify(restTemplate, times(1)).getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponseForList.class));
    }

    @Test
    void testGetAllEmployees_HttpServerErrorException() throws IOException {
        String expectedUrl = dummyAPIBaseUrl + "/employees";
        Mockito.when(restTemplate.getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponseForList.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(ExternalServiceException.class, () -> employeeClient.getAllEmployees());

        verify(restTemplate, times(1)).getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponseForList.class));
    }

    @Test
    void testGetEmployeeById_Success() {
        String employeeId = "1";
        EmployeeResponse expectedResponse = new EmployeeResponse();
        ResponseEntity<EmployeeResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        String expectedUrl = dummyAPIBaseUrl + "/employee/" + employeeId;
        Mockito.when(restTemplate.getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponse.class)))
                .thenReturn(responseEntity);

        EmployeeResponse actualResponse = employeeClient.getEmployeeById(employeeId);

        assertNotNull(actualResponse);
        verify(restTemplate, times(1)).getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponse.class));
    }

    @Test
    void testGetEmployeeById_HttpClientErrorException() {
        String employeeId = "1";
        String expectedUrl = dummyAPIBaseUrl + "/employee/" + employeeId;

        Mockito.when(restTemplate.getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(ExternalServiceException.class, () -> employeeClient.getEmployeeById(employeeId));

        verify(restTemplate, times(1)).getForEntity(Mockito.eq(expectedUrl), Mockito.eq(EmployeeResponse.class));
    }

    @Test
    void testDeleteEmployeeById_Success() {
        String employeeId = "1";
        EmployeeResponseForDelete expectedResponse = new EmployeeResponseForDelete();
        ResponseEntity<EmployeeResponseForDelete> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        String expectedUrl = dummyAPIBaseUrl + "/delete/" + employeeId;
        Mockito.when(restTemplate.exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.DELETE), Mockito.isNull(), Mockito.eq(EmployeeResponseForDelete.class)))
                .thenReturn(responseEntity);

        EmployeeResponseForDelete actualResponse = employeeClient.deleteEmployeeById(employeeId);

        assertNotNull(actualResponse);
        verify(restTemplate, times(1)).exchange(Mockito.eq(expectedUrl), Mockito.eq(HttpMethod.DELETE), Mockito.isNull(), Mockito.eq(EmployeeResponseForDelete.class));
    }
}

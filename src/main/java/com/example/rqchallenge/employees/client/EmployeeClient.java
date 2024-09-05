package com.example.rqchallenge.employees.client;

import com.example.rqchallenge.employees.entity.*;
import com.example.rqchallenge.employees.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import static com.example.rqchallenge.employees.entity.Constants.*;

@Component
@Slf4j
public class EmployeeClient {

    @Value("${dummy.api.base.url}")
    private String dummyAPIBaseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public EmployeeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public EmployeeResponseForCreate createEmployee(EmployeeRequest employeeRequest) {

        String baseUrl = dummyAPIBaseUrl.concat(CREATE_EMPLOYEE_PATH);
        try {
            ResponseEntity<EmployeeResponseForCreate> response = restTemplate.postForEntity(baseUrl, employeeRequest, EmployeeResponseForCreate.class);
            if(response.getStatusCode() == HttpStatus.CREATED){
                log.info("Successfully created employee with response: {}", response.getBody());
                return response.getBody();
            }
            else{
                throw new HttpClientErrorException(response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error occurred while creating employee: {}", e.getMessage());
            throw new ExternalServiceException("Failed to create employee", e);
        }
    }

    public EmployeeResponseForList getAllEmployees() throws IOException {
        String baseUrl = dummyAPIBaseUrl.concat(EMPLOYEES_PATH);
        try {
            ResponseEntity<EmployeeResponseForList> response = restTemplate.getForEntity(baseUrl, EmployeeResponseForList.class);
            if (response.getStatusCode() == HttpStatus.OK){
                log.info("Successfully fetched all response");
                return response.getBody();
            }
            else{
                throw new HttpClientErrorException(response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error occurred while fetching employees: {}", e.getMessage());
            throw new ExternalServiceException("Failed to fetch employees", e);
        }
    }

    public EmployeeResponse getEmployeeById(String id){
        String baseUrl = dummyAPIBaseUrl.concat(EMPLOYEE_PATH_BY_ID).concat(id);
        try {
            ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(baseUrl, EmployeeResponse.class);
            if (response.getStatusCode() == HttpStatus.OK){
                log.info("Successfully fetched employee with ID: {}", id);
                return response.getBody();
            }
            else{
                throw new HttpClientErrorException(response.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error occurred while fetching employee by ID: {}", e.getMessage());
            throw new ExternalServiceException("Failed to fetch employee by ID", e);
        }
    }

    public EmployeeResponseForDelete deleteEmployeeById(String id){
        String baseUrl = dummyAPIBaseUrl.concat(DELETE_EMPLOYEE_PATH).concat(id);
        try {
            ResponseEntity<EmployeeResponseForDelete> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.DELETE,
                    null,
                    EmployeeResponseForDelete.class
            );
            log.info("Successfully deleted employee with ID: {}", id);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error occurred while deleting employee with ID: {}", e.getMessage());
            throw new ExternalServiceException("Failed to delete employee", e);
        }
    }
}

package com.example.rqchallenge.employees.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class EmployeeResponseForList {
    private String status;
    private List<Employee> data;
    private String message;

}

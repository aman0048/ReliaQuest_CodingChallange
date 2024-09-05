package com.example.rqchallenge.employees.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class EmployeeRequest {
    private String name;
    private String salary;
    private String age;
}

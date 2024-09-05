package com.example.rqchallenge.employees.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class EmployeeDataForCreate {
    private String name;
    private String salary;
    private String age;
    private String id;
}

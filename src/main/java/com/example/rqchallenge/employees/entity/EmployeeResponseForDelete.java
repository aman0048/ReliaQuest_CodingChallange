package com.example.rqchallenge.employees.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class EmployeeResponseForDelete {
    private String status;
    private String message;
}

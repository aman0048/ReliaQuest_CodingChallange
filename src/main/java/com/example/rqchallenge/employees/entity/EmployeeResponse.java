package com.example.rqchallenge.employees.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class EmployeeResponse {
    private String status;
    private Employee data;
    private String message;
}

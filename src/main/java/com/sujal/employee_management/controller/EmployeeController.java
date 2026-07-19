package com.sujal.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeController {

    @GetMapping("/employees")
    public List<Employee> getEmployees() {

        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(
                101,
                "Sujal",
                "DevOps",
                "sujal@gmail.com",
                70000
        ));

        employees.add(new Employee(
                102,
                "Rahul",
                "Testing",
                "rahul@gmail.com",
                55000
        ));

        return employees;
    }

    public static class Employee {
        private int id;
        private String name;
        private String department;
        private String email;
        private int salary;

        public Employee(int id, String name, String department, String email, int salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.email = email;
            this.salary = salary;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDepartment() {
            return department;
        }

        public String getEmail() {
            return email;
        }

        public int getSalary() {
            return salary;
        }
    }
}
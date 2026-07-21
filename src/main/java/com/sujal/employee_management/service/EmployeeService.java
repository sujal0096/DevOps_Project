package com.sujal.employee_management.service;

import com.sujal.employee_management.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class EmployeeService {

    private List<Employee> employees = new ArrayList<>();

    public EmployeeService() {

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

        employees.add(new Employee(
                103,
                "Amit",
                "Cloud",
                "amit@gmail.com",
                60000
        ));
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee getEmployeeById(int id) {

        for (Employee employee : employees) {

            if (employee.getId() == id) {
                return employee;
            }
        }

        return null;
    }

    public String addEmployee(Employee employee) {

        employees.add(employee);

        return "Employee Added Successfully";
    }

    public String updateEmployee(int id, Employee updatedEmployee) {

        for (Employee employee : employees) {

            if (employee.getId() == id) {

                employee.setName(updatedEmployee.getName());
                employee.setDepartment(updatedEmployee.getDepartment());
                employee.setEmail(updatedEmployee.getEmail());
                employee.setSalary(updatedEmployee.getSalary());

                return "Employee Updated Successfully";
            }
        }

        return "Employee Not Found";
    }

    public String deleteEmployee(int id) {

        Iterator<Employee> iterator = employees.iterator();

        while (iterator.hasNext()) {

            Employee employee = iterator.next();

            if (employee.getId() == id) {

                iterator.remove();

                return "Employee Deleted Successfully";
            }
        }

        return "Employee Not Found";
    }
}
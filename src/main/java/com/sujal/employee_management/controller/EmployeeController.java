package com.sujal.employee_management.controller;

// If external Employee model cannot be resolved, provide a local fallback
// to allow the controller to compile. Remove this fallback once the
// proper model package is available on the classpath.
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class EmployeeController {

    private List<Employee> employees = new ArrayList<>();

    public EmployeeController() {
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

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employees;
    }

    @PostMapping("/employees")
    public String addEmployee(@RequestBody Employee employee) {

        employees.add(employee);

        return "Employee Added Successfully";
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable int id) {

        for (Employee employee : employees) {

            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    @PutMapping("/employees/{id}")
    public String updateEmployee(@PathVariable int id,@RequestBody Employee updatedEmployee) {

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

    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id) {

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

// Local fallback Employee model
class Employee {
    private int id;
    private String name;
    private String department;
    private String email;
    private double salary;

    public Employee() {}

    public Employee(int id, String name, String department, String email, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.salary = salary;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
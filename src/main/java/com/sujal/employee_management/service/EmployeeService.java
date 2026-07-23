package com.sujal.employee_management.service;

import com.sujal.employee_management.exception.EmployeeNotFoundException;
import com.sujal.employee_management.model.Employee;
import com.sujal.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sujal.employee_management.dto.EmployeeDTO;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all employees
    public List<EmployeeDTO> getEmployees() {
    List<Employee> employees = employeeRepository.findAll();
            return employees.stream()
            .map(this::convertToDTO)
            .toList();
    }

    // Get employee by ID
    public EmployeeDTO getEmployeeById(int id) {
    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() ->
                    new EmployeeNotFoundException("Employee with ID " + id + " not found"));
        return convertToDTO(employee);
    }

    // Add employee
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Update employee
    public Employee updateEmployee(int id, Employee updatedEmployee) {

    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() ->
                    new EmployeeNotFoundException("Employee with ID " + id + " not found"));

        employee.setName(updatedEmployee.getName());
        employee.setSalary(updatedEmployee.getSalary());
        employee.setDepartment(updatedEmployee.getDepartment());
        employee.setEmail(updatedEmployee.getEmail());

        return employeeRepository.save(employee);
    }

    // Delete employee
    public void deleteEmployee(int id) {
    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() ->
                    new EmployeeNotFoundException("Employee with ID " + id + " not found"));
        employeeRepository.delete(employee);
    }

    // Convert Employee entity to EmployeeDTO
    private EmployeeDTO convertToDTO(Employee employee) {
    return new EmployeeDTO(
            employee.getId(),
            employee.getName(),
            employee.getDepartment(),
            employee.getEmail()
        );
    }
}
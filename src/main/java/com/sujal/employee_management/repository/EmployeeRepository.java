package com.sujal.employee_management.repository;

import com.sujal.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
package com.example.employee.service;

import com.example.employee.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.employee.model.Employee;
import com.example.employee.model.EmployeeRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class EmployeeH2Service implements EmployeeRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Employee> getEmployees() {
        List<Employee> employeeList = db.query("SELECT * FROM EMPLOYEELIST", new EmployeeRowMapper());
        ArrayList<Employee> employees = new ArrayList<>(employeeList);
        return employees;
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try {
            Employee employee = db.queryForObject("SELECT * FROM EMPLOYEELIST WHERE employeeId = ?",
                    new EmployeeRowMapper(), employeeId);
            return employee;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }
    }

    @Override
    public Employee addEmployee(Employee employee) {
        db.update("INSERT INTO EMPLOYEELIST(employeeName, email, department) values (?, ?, ?)",
                employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        Employee savedEmployee = db.queryForObject(
                "SELECT * FROM EMPLOYEELIST WHERE employeeName = ? and email = ? and department = ?",
                new EmployeeRowMapper(), employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        return savedEmployee;

    }

    @Override
    public Employee updateEmployee(int employeeId, Employee employee) {
        if (employee.getEmployeeName() != null) {
            db.update("UPDATE EMPLOYEELIST SET employeeName = ? WHERE employeeId = ?", employee.getEmployeeName(), employeeId);
    

        }
        if (employee.getEmail() != null) {
            db.update("UPDATE EMPLOYEELIST SET mail = ? WHERE employeeId = ?", employee.getEmail(), employeeId);

        }
        if (employee.getDepartment() != null) {
            db.update("UPDATE EMPLOYEELIST SET department ? WHERE employeId = ?", employee.getDepartment(), employeeId);

        }
        return getEmployeeById(employeeId);
        

    }

    @Override
    public void deleteEmployee(int employeeId) {
        db.update("DELETE FROM EMPLOYEELIST WHERE employeeId = ?", employeeId);

    }

}

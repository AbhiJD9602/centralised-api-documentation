package com.example.api.controller;

import com.example.api.entities.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {


    //Ideally you shall be using Service classes
    @Autowired
    EmployeeRepository employeeRepository;


    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @GetMapping("/{employeeid}")
    public ResponseEntity<Employee> getEmployeeByEmployeeId(@PathVariable("employeeid") int employeeid) {
        Optional<Employee> personInDB = employeeRepository.findById(employeeid);
        if (personInDB.isPresent()) {
            return ResponseEntity.ok(personInDB.get());
        } else {
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Employee> createNewEmployee(@RequestBody Employee person) {
        Employee personInDB = employeeRepository.save(person);
        return new ResponseEntity<Employee>(personInDB, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeid}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable("employeeid") int employeeid, @RequestBody(required = true) Employee employeeDataToBeUpdated) {

        if (employeeid != employeeDataToBeUpdated.getEmployeeId()) {    //Just to make sure we have same person_id in path param and body.
            return new ResponseEntity<Employee>(HttpStatus.BAD_REQUEST);
        }

        Optional<Employee> personInDB = employeeRepository.findById(employeeid);
        if (personInDB.isPresent()) {
            Employee person = employeeRepository.saveAndFlush(employeeDataToBeUpdated);
            return ResponseEntity.ok(person);
        } else {
            return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
        }
    }
}
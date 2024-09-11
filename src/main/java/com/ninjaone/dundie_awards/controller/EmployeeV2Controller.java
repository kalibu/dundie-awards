package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v2/employees")
@Tag(name = "employeesV2", description = "Rest API for Employees V2")
@Log
public class EmployeeV2Controller {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private AwardsCache awardsCache;

    @Operation(
            operationId = "/",
            summary = "Get all employees",
            description = "Get all employees",
            tags = {"employeesV2"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Employee.class)))
                    })
            }
    )
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Operation(
            operationId = "/",
            summary = "Create new employee",
            description = "Create new employee",
            tags = {"employeesV2"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "bad request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    })
            }
    )
    @PostMapping
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        log.info("Call createEmployee=" + employee);

        return employeeRepository.save(employee);
    }

    @Operation(
            operationId = "/{id}",
            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
            summary = "Get employee by id",
            description = "Get employee by id",
            tags = {"employeesV2"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "bad request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    })
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            return ResponseEntity.ok(optionalEmployee.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            operationId = "/{id}",
            summary = "Update employee by id",
            description = "Update employee by id",
            tags = {"employeesV2"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "bad request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
                    })
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (StringUtils.isBlank(employeeDetails.getFirstName())
                || StringUtils.isBlank(employeeDetails.getLastName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Employee employee = optionalEmployee.get();
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());

        Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(
            operationId = "/{id}",
            parameters = {@Parameter(name = "id", in = ParameterIn.PATH, required = true)},
            summary = "Delete employee by id",
            description = "Delete employee by id",
            tags = {"employeesV2"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
                    })
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Employee employee = optionalEmployee.get();
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
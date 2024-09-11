package com.ninjaone.dundie_awards.controller;

import com.google.gson.Gson;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeV1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    public void test_getAllEmployees() throws Exception {

        final String jsonAllEmployees = new Gson().toJson(employeeRepository.findAll());

        this.mockMvc.perform(
                        get("/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonAllEmployees));
    }

    @Test
    public void test_createEmployee_Valid() throws Exception {

        final Employee employee = new Employee();
        employee.setFirstName("firstName");
        employee.setLastName("lastName");
        final Organization organization = organizationRepository.findAll().getFirst();
        employee.setOrganization(organization);

        final MockHttpServletResponse response = this.mockMvc.perform(
                        post("/employees")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(employee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Employee returnEmployee = new Gson().fromJson(response.getContentAsString(), Employee.class);

        assertTrue(returnEmployee.getId() > 0);
    }

    @Test
    public void test_getEmployeeById_ValidId() throws Exception {

        Employee first = employeeRepository.findAll().getFirst();
        final String jsonFirstEmployees = new Gson().toJson(first);

        this.mockMvc.perform(
                        get("/employees/{id}", first.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonFirstEmployees));
    }

    @Test
    public void test_getEmployeeById_InvalidId() throws Exception {

        this.mockMvc.perform(
                        get("/employees/{id}", "A"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getEmployeeById_NotFound() throws Exception {

        this.mockMvc.perform(
                        get("/employees/{id}", 0))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_updateEmployee_Valid() throws Exception {

        final Employee first = employeeRepository.findAll().getFirst();

        final Employee employee = new Employee();
        employee.setFirstName("firstName");
        employee.setLastName("lastName");

        final MockHttpServletResponse response = this.mockMvc.perform(
                        put("/employees/{id}", first.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(employee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Employee returnEmployee = new Gson().fromJson(response.getContentAsString(), Employee.class);

        assertEquals(first.getId(), returnEmployee.getId());
        assertEquals(employee.getFirstName(), returnEmployee.getFirstName());
        assertEquals(employee.getLastName(), returnEmployee.getLastName());
    }

    @Test
    public void test_updateEmployee_NotFound() throws Exception {

        final Employee employee = new Employee();
        employee.setFirstName("firstName");
        employee.setLastName("lastName");

        this.mockMvc.perform(
                        put("/employees/{id}", 0)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(employee)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_updateEmployee_InvalidId() throws Exception {

        final Employee employee = new Employee();
        employee.setFirstName("firstName");
        employee.setLastName("lastName");

        this.mockMvc.perform(
                        put("/employees/{id}", "A")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new Gson().toJson(employee)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_deleteEmployee_Valid() throws Exception {

        final Employee first = employeeRepository.findAll().getFirst();

        final MockHttpServletResponse response = this.mockMvc.perform(
                        delete("/employees/{id}", first.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Optional<Employee> removedEmployee = employeeRepository.findById(first.getId());
        assertFalse(removedEmployee.isPresent());

        final Map<String, Boolean> mapResponse = new Gson().fromJson(response.getContentAsString(), Map.class);
        assertTrue(mapResponse.containsKey("deleted"));
        assertTrue(mapResponse.get("deleted"));
    }

    @Test
    public void test_deleteEmployee_NotFound() throws Exception {

        this.mockMvc.perform(
                        delete("/employees/{id}", 0))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_deleteEmployee_InvalidId() throws Exception {

        this.mockMvc.perform(
                        delete("/employees/{id}", "A"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

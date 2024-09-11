package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    @NotNull
    @NotEmpty
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @NotEmpty
    private String lastName;

    @Column(name = "dundie_awards")
    private Integer dundieAwards;

    @ManyToOne
    @NotNull
    private Organization organization;

    public Employee(String firstName, String lastName, Organization organization) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
    }

}
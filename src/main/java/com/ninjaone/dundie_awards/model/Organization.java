package com.ninjaone.dundie_awards.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizations")
@NoArgsConstructor
@Data
public class Organization {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  private long id;

  @Column(name = "name")
  private String name;

  public Organization(String name) {
    super();
    this.name = name;
  }
}

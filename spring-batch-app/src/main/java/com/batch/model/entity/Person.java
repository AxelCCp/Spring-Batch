package com.batch.model.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;


import javax.persistence.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@Entity
@Table(name="persons") @Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private Integer age;
    private String insertiondate;

}

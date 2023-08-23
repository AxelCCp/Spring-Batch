package com.batch.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="persons") @Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String age;
    private String createAt;
}

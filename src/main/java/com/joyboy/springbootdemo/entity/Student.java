package com.joyboy.springbootdemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "student")
public class Student {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @JoinColumn(name = "address_id")
    @OneToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Student(String name, int age, Address address, Gender gender) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.gender = gender;
    }
}

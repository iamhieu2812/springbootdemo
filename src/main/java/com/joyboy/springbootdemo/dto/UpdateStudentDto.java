package com.joyboy.springbootdemo.dto;

import com.joyboy.springbootdemo.entity.Address;
import com.joyboy.springbootdemo.entity.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateStudentDto {

    private final int id;

    private final String name;

    private final int age;

    private final String province;

    private final String district;

    private final String village;

    private final Gender gender;

    public UpdateStudentDto(int id, String name, int age, String province, String district, String village, Gender gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.province = province;
        this.district = district;
        this.village = village;
        this.gender = gender;
    }
}

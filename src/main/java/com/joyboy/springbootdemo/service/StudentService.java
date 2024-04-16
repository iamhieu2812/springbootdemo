package com.joyboy.springbootdemo.service;

import com.joyboy.springbootdemo.dto.GetAllStudentResponseDto;
import com.joyboy.springbootdemo.entity.Gender;
import com.joyboy.springbootdemo.entity.Student;

import java.util.List;

public interface StudentService {

    GetAllStudentResponseDto findAll(int pageNo, int pageSize);

    Student findById(int id);

    List<Student> findByName(String name);

    List<Student> findByAge(int age);

    List<Student> findByGender(Gender gender);

    Student save(Student student);

    void deleteById(int id);

    List<Student> findByProvince(String province);

    int countStudentByGenderAndAddress(Gender gender, String province, int age);

    boolean studentExistByNameAndAgeAndGenderAndProvince(String name, int age, Gender gender, String province);

    List<Integer> findAgeByStudentPrefixAndGender(Gender gender, String prefix);
}

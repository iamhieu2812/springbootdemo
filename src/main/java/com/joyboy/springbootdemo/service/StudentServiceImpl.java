package com.joyboy.springbootdemo.service;

import com.joyboy.springbootdemo.dto.GetAllStudentResponseDto;
import com.joyboy.springbootdemo.exception.StudentNotFoundException;
import com.joyboy.springbootdemo.repository.StudentRepository;
import com.joyboy.springbootdemo.entity.Gender;
import com.joyboy.springbootdemo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public GetAllStudentResponseDto findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Student> students = studentRepository.findAll(pageable);
        GetAllStudentResponseDto responseDto = new GetAllStudentResponseDto();
        responseDto.setStudents(students.getContent());
        responseDto.setPageNo(pageNo);
        responseDto.setPageSize(pageSize);
        responseDto.setTotalElements(students.getTotalElements());
        responseDto.setTotalPages(students.getTotalPages());
        responseDto.setLast(students.isLast());
        return responseDto;
    }

    @Override
    public Student findById(int id) {
        Optional<Student> result = studentRepository.findById(id);

        Student student = null;

        if (result.isPresent()) {
            student = result.get();
        } else {
            throw new StudentNotFoundException("Student id = " + id + " not found");
        }
        return student;
    }

    @Override
    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public List<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    @Override
    public List<Student> findByGender(Gender gender) {
        return studentRepository.findByGender(gender);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteById(int id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findByProvince(String province) {
        return studentRepository.findByProvince(province);
    }

    @Override
    public int countStudentByGenderAndAddress(Gender gender, String province, int age) {
        return studentRepository.countByGenderAndAddress_ProvinceIgnoreCase(gender, province, age);
    }

    @Override
    public boolean studentExistByNameAndAgeAndGenderAndProvince(String name, int age, Gender gender, String province) {
        return studentRepository.existsByNameIgnoreCaseAndAgeAndGenderAndAddress_ProvinceIgnoreCase(name, age, gender, province);
    }

    @Override
    public List<Integer> findAgeByStudentPrefixAndGender(Gender gender, String prefix) {
        return studentRepository.selectAgeOfStudentNameStartingWith(gender, prefix);
    }
}

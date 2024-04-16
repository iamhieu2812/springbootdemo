package com.joyboy.springbootdemo.controller;

import com.joyboy.springbootdemo.dto.CreateStudentDto;
import com.joyboy.springbootdemo.dto.GetAllStudentResponseDto;
import com.joyboy.springbootdemo.dto.UpdateStudentDto;
import com.joyboy.springbootdemo.entity.Address;
import com.joyboy.springbootdemo.entity.Gender;
import com.joyboy.springbootdemo.entity.Student;
import com.joyboy.springbootdemo.exception.IllegalArgumentException;
import com.joyboy.springbootdemo.exception.StudentNotFoundException;
import com.joyboy.springbootdemo.service.AddressService;
import com.joyboy.springbootdemo.service.StudentService;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private final StudentService studentService;

    private final AddressService addressService;

    @Autowired
    public StudentRestController(StudentService studentService, AddressService addressService) {
        this.studentService = studentService;
        this.addressService = addressService;
    }

    @GetMapping("/students")
    public ResponseEntity<GetAllStudentResponseDto> findAllStudents(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize
    ) {

        return new ResponseEntity<>(studentService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public Student findStudentById(@PathVariable int id) {
        return studentService.findById(id);
    }

    @GetMapping("/students/byName/{name}")
    public List<Student> findStudentByName(@PathVariable String name) {
        List<Student> student = studentService.findByName(name);

        if (student == null) {
            throw new StudentNotFoundException("Student name = " + name + " not found");
        }

        return student;
    }

    @GetMapping("/students/byAge/{age}")
    public List<Student> findStudentsByAge(@PathVariable int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("/students/byGender/{gender}")
    public List<Student> findStudentByGender(@PathVariable Gender gender) {
        return studentService.findByGender(gender);
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody CreateStudentDto createStudentDto) {

        String name = createStudentDto.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên không được để trống");
        }

        int age = createStudentDto.getAge();
        if (age <= 0 || age >= 100) {
            throw new IllegalArgumentException("Tuổi phải lớn hơn 0 và nhỏ hơn 100");
        }

        Address address = new Address();

        String province = createStudentDto.getProvince();
        String district = createStudentDto.getDistrict();
        String village = createStudentDto.getVillage();

        // get Address
        if (province == null && district == null && village == null) {
            address = null;
        } else {
            address.setProvince(province);
            address.setDistrict(district);
            address.setVillage(village);
            address = addressService.save(address);
        }

        Student student = new Student(createStudentDto.getName(), createStudentDto.getAge(), address, createStudentDto.getGender());

        // then save the Student
        return studentService.save(student);
    }

    @PutMapping("/students")
    public Student updateStudent(@RequestBody UpdateStudentDto updateStudentDTO) {

        // get Student
        Student student = studentService.findById(updateStudentDTO.getId());

        // get Address
        Address address = student.getAddress();
        String province = updateStudentDTO.getProvince();
        String district = updateStudentDTO.getDistrict();
        String village = updateStudentDTO.getVillage();

        if (province != null) {
            address.setProvince(province);
        }
        if (district != null) {
            address.setDistrict(district);
        }
        if (village != null) {
            address.setVillage(village);
        }
        address = addressService.save(address);
        student.setAddress(address);
        if (updateStudentDTO.getName() != null) {
            student.setName(updateStudentDTO.getName());
        }
        if (updateStudentDTO.getAge() != 0) {
            student.setAge(updateStudentDTO.getAge());
        }
        if (updateStudentDTO.getGender() != null) {
            student.setGender(updateStudentDTO.getGender());
        }

        // then save the Student
        return studentService.save(student);
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudentById(@PathVariable int id) {
        Student student = studentService.findById(id);

        if (student == null) {
            throw new StudentNotFoundException("Student id = " + id + " not found");
        }

        studentService.deleteById(id);

        return "Deleted student id: " + id;
    }

   @GetMapping("students/byProvince/{province}")
    public List<Student> findStudentByProvince(@PathVariable String province){

       return studentService.findByProvince(province);
    }

    @GetMapping("students/countByGenderAndProvince")
    public int findStudentByGenderAndProvince(
            @RequestParam Gender gender,
            @RequestParam String province,
            @RequestParam int age
    ) {
        return studentService.countStudentByGenderAndAddress(gender, province, age);
    }

    @GetMapping("students/existByNameAgeGenderProvince")
    public boolean studentExistByNameAgeGenderProvince(
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam Gender gender,
            @RequestParam String province
    ) {
        return studentService.studentExistByNameAndAgeAndGenderAndProvince(name, age, gender, province);
    }

    @GetMapping("students/getAgeByNamePrefixAndGender")
    public List<Integer> findAgesByNamePrefixAndGender(
            @RequestParam Gender gender,
            @RequestParam String prefix
    ) {
        return studentService.findAgeByStudentPrefixAndGender(gender, prefix);
    }
}

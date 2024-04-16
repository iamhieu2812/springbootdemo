package com.joyboy.springbootdemo;

import com.joyboy.springbootdemo.dto.GetAllStudentResponseDto;
import com.joyboy.springbootdemo.entity.Gender;
import com.joyboy.springbootdemo.entity.Student;
import com.joyboy.springbootdemo.exception.StudentNotFoundException;
import com.joyboy.springbootdemo.repository.StudentRepository;
import com.joyboy.springbootdemo.service.AddressService;
import com.joyboy.springbootdemo.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class StudentServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @SpyBean
    private StudentRepository mockStudentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AddressService addressService;

    @BeforeEach
    public void setupDatabase() {

        // Insert Address
        jdbc.execute("INSERT INTO address (province, district, village) VALUES\n" +
                "('Hanoi', 'Ba Dinh', 'Quan Thanh'),\n" +
                "('Hanoi', 'Hoan Kiem', 'Hang Bac'),\n" +
                "('Hanoi', 'Dong Da', 'Cat Linh'),\n" +
                "('Hanoi', 'Tay Ho', 'Nhat Tan'),\n" +
                "('Ho Chi Minh', 'District 1', 'Ben Thanh'), \n" +
                "('Ho Chi Minh', 'District 28', 'Ben Thanh')");

        // Insert Student
        jdbc.execute("INSERT INTO student (name, age, address_id, gender) VALUES\n" +
                "('John Doe', 25, (SELECT id FROM address WHERE province = 'Hanoi' AND district = 'Ba Dinh' AND village = 'Quan Thanh'), 'MALE'),\n" +
                "('Jane Smith', 22, (SELECT id FROM address WHERE province = 'Hanoi' AND district = 'Hoan Kiem' AND village = 'Hang Bac'), 'FEMALE'),\n" +
                "('Michael Johnson', 28, (SELECT id FROM address WHERE province = 'Hanoi' AND district = 'Dong Da' AND village = 'Cat Linh'), 'MALE'),\n" +
                "('Emily Davis', 20, (SELECT id FROM address WHERE province = 'Hanoi' AND district = 'Tay Ho' AND village = 'Nhat Tan'), 'FEMALE'),\n" +
                "('James Brown', 30, (SELECT id FROM address WHERE province = 'Ho Chi Minh' AND district = 'District 1' AND village = 'Ben Thanh'), 'MALE'),\n" +
                "('John Doo', 23, (SELECT id FROM address WHERE province = 'Ho Chi Minh' AND district = 'District 28' AND village = 'Ben Thanh'), 'MALE');");
    }

    @Test
    public void testFindAll() {

        // Given
        int pageNo = 0;
        int pageSize = 2;

        // When
        GetAllStudentResponseDto dto = studentService.findAll(pageNo, pageSize);

        // Then
        assertEquals(2, dto.getStudents().size()); // Kiểm tra kích thước danh sách sinh viên trả về
        assertEquals(pageNo, dto.getPageNo()); // Kiểm tra số trang
        assertEquals(pageSize, dto.getPageSize()); // Kiểm tra kích thước trang
        assertEquals(6, dto.getTotalElements()); // Kiểm tra tổng số sinh viên
        assertEquals(3, dto.getTotalPages()); // Kiểm tra tổng số trang
        assertFalse(dto.isLast()); // Kiểm tra xem trang cuối cùng không
    }

    @Test
    public void testFindByIdSuccess() {

        // Given
        int id = 1;
        Student existingStudent = new Student("Hieu", 20, null, Gender.MALE);
        when(mockStudentRepository.findById(id)).thenReturn(Optional.of(existingStudent));

        // When
        Student foundStudent = studentService.findById(id);

        // Assert
        assertEquals(existingStudent, foundStudent);
    }

    @Test
    public void testFindById_StudentNotFound() {

        // Given
        int id = 2;
        when(mockStudentRepository.findById(id)).thenReturn(Optional.empty());

        // When and then
        assertThrows(StudentNotFoundException.class, () -> studentService.findById(id));
    }

    @Test
    public void testFindByName() {

        // Given
        String studentName = "Hieu";
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Hieu", 20, null, Gender.MALE));
        when(mockStudentRepository.findByName(studentName)).thenReturn(studentList);

        // When
        List<Student> foundStudents = studentService.findByName(studentName);

        // Then
        assertEquals(studentList, foundStudents);
    }

    @Test
    public void testSave() {

        // Given
        int id = 1;
        Student student = new Student("Hieu", 18, null, Gender.MALE);
        when(mockStudentRepository.save(student)).thenReturn(student);

        // When
        Student savedStudent = studentService.save(student);

        // Assert
        assertEquals(student, savedStudent);
    }

    @Test
    public void testDeleteById() {

        // Given
        int id = 1;

        // When
        studentService.deleteById(id);

        // Verify
        assertThrows(StudentNotFoundException.class, () -> studentService.findById(id));
    }

    @Test
    public void testFindByProvince() {

        // Given
        String province = "Hanoi";

        // When
        List<Student> students = studentService.findByProvince("Hanoi");

        // Then
        assertEquals(4, students.size());
    }

    @Test
    public void testStudentExistByNameAndAgeAndGenderAndProvince() {

        // Given
        String name = "John Doe";
        int age = 25;
        Gender gender = Gender.MALE;
        String province = "Hanoi";

        // When and Then
        assertTrue(studentService.studentExistByNameAndAgeAndGenderAndProvince(name, age, gender, province));
    }

    @Test
    public void testFindAgeByStudentPrefixAndGender() {

        // Given
        List<Integer> expectedList = List.of(23, 25);
        String prefix = "John";
        Gender gender = Gender.MALE;

        // When
        List<Integer> actualList = studentService.findAgeByStudentPrefixAndGender(gender, prefix);

        // Then
        assertIterableEquals(expectedList, actualList);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM address");
    }
}

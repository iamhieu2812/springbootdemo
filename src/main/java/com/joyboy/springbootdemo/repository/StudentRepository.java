package com.joyboy.springbootdemo.repository;

import com.joyboy.springbootdemo.entity.Gender;
import com.joyboy.springbootdemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByName(String name);

    List<Student> findByAge(int age);

    List<Student> findByGender(Gender gender);

//    @Query(value = "SELECT s.* FROM student s JOIN address a ON a.id = s.address_id WHERE UPPER(a.province) = UPPER(:province)", nativeQuery = true)
//    @Query(value = "SELECT s FROM Student s JOIN Address a ON a.id = s.address.id WHERE UPPER(a.province) = UPPER(:province)")
    @Query(value = "SELECT s FROM Student s WHERE s.address.province = :province")
    List<Student> findByProvince(@Param("province") String province);

    @Query(value = "SELECT COUNT(s) FROM Student s WHERE s.gender = :gender AND s.address.province = :province AND s.age > :age")
    int countByGenderAndAddress_ProvinceIgnoreCase(@Param("gender") Gender gender, @Param("province") String province, @Param("age") int age);

    boolean existsByNameIgnoreCaseAndAgeAndGenderAndAddress_ProvinceIgnoreCase(String name, int age, Gender gender, String province);

    @Query(value = "SELECT DISTINCT s.age FROM Student s WHERE s.name LIKE :prefix% AND s.gender = :gender")
    List<Integer> selectAgeOfStudentNameStartingWith(@Param("gender") Gender gender, @Param("prefix") String prefix);
}

package com.joyboy.springbootdemo.dto;

import com.joyboy.springbootdemo.entity.Student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllStudentResponseDto {

    private List<Student> students;

    private int pageNo;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean isLast;

}

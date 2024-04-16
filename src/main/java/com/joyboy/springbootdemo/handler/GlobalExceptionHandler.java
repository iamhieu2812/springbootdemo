package com.joyboy.springbootdemo.handler;

import com.joyboy.springbootdemo.dto.StudentErrorResponseDto;
import com.joyboy.springbootdemo.exception.IllegalArgumentException;
import com.joyboy.springbootdemo.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponseDto> handleException(StudentNotFoundException exc) {

        StudentErrorResponseDto err = new StudentErrorResponseDto();

        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponseDto> handleException(IllegalArgumentException exc) {

        StudentErrorResponseDto err = new StudentErrorResponseDto();

        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponseDto> handleException(Exception exc) {

        StudentErrorResponseDto err = new StudentErrorResponseDto();

        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setMessage(exc.getMessage());
        err.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

}

package com.joyboy.springbootdemo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentErrorResponseDto {
    private int status;
    private String message;
    private long timeStamp;
}

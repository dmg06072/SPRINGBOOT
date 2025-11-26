package com.example.demo.Domain.Common.Dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemoDto {

    @NotBlank(message="TEXT는 필수 항목입니다.")
    private String text;
    @NotBlank(message="작성자를 입력하세요.")
    @Email(message="example@example.com 형식으로 입력하세요")
    private String writer;


}

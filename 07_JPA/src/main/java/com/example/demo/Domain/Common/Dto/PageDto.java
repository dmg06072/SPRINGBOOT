package com.example.demo.Domain.Common.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
public class PageDto {
    private Integer pageNo;
    private Integer amount;
    private String keyword;
    private String type;



}

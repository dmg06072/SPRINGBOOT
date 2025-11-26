package com.example.demo.Dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private long id;
    private String category;
    private int price;
    MultipartFile [] files;
}

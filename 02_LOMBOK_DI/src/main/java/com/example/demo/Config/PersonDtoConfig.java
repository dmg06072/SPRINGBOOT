package com.example.demo.Config;

import com.example.demo.Dto.PersonDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonDtoConfig {

    @Bean
    public PersonDto person03(){
        return PersonDto.builder()
                .name("박효신")
                .addr("서울")
                .age(45)
                .build();
    }
    @Bean(name = "personBean")
    public PersonDto person04() {
        return PersonDto.builder()
                .name("버즈")
                .addr("울산")
                .age(30)
                .build();
    }
}

package com.example.demo.Controller;

import com.example.demo.Dto.MemoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/th")
public class ThymeleafTestController {

    @GetMapping("/test1")
    public void t1(Model model){
        log.info("GET /th/test1....");
        //기본
        model.addAttribute("name","HONG");

        //DTO형
        MemoDto dto = new MemoDto(1L,"CONTENTS_1","TEST@NAVER.COM");
        model.addAttribute("dto",dto);
        
        //LIST형
        List<MemoDto> list = new ArrayList();
        for(long i=1;i<=5;i++){
            MemoDto tmp = new MemoDto(i,"T-"+i,"W-"+i);
            list.add(tmp);
        }
        model.addAttribute("list",list);
        //TRUE/FALSE
        model.addAttribute("isAuth",false);
    }
}

package com.example.demo.Controller;

import com.example.demo.Dto.MemoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @GetMapping("/param1")
    @ResponseBody
    public void param1(MemoDto dto){
        log.info("GET /memo/param1..."+dto);
    }
    @GetMapping("/param2/{id}/{text}/{writer}")
    @ResponseBody
    public void param2(MemoDto dto){
        log.info("GET /memo/param2..."+dto);
    }
}

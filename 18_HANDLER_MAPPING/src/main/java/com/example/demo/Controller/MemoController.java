package com.example.demo.Controller;


import com.example.demo.Model.Dto.MemoDto;
import com.example.demo.Model.Service.MemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoService memoService;

    @GetMapping("/add")
    public void memo_get(){
        log.info("GET /memo/add...");
    }
    @PostMapping("/add")
    public void memo_post(MemoDto dto){
        log.info("POST /memo/add..." + dto);
        //1 파라미터
        //2 유효성
        //3 서비스
        boolean isAdded =memoService.addMemo(dto);
        
        //4 뷰이동
    }
    @GetMapping("/list")
    @ResponseBody
    public void list(){
        log.info("GET /memo/list...");
        memoService.getMemos();
    }
}

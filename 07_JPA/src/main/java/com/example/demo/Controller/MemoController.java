package com.example.demo.Controller;

import com.example.demo.Domain.Common.Dto.MemoDto;
import com.example.demo.Domain.Common.Dto.PageBlock;
import com.example.demo.Domain.Common.Dto.PageDto;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/memo")
public class MemoController {


    @Autowired
    private MemoService memoService;

//    @ExceptionHandler(Exception.class)
//    public String exception_handler(Exception e){
//        log.error("MemoController's Exception..." + e);
//        return "memo/error";
//    }

    @GetMapping("/add")
    public void add_memo_get() throws Exception
    {
        log.info("GET /memo/add...");
    }
    @PostMapping("/add")
    public String add_memo_post(@Valid MemoDto dto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws Exception
    {
        log.info("POST /memo/add..." + dto);
        //파라미터
        //입력값검증(데이터)
        log.info("유효성 오류 발생여부 : " + bindingResult.hasErrors());
        if(bindingResult.hasErrors()){
            for(FieldError error  : bindingResult.getFieldErrors()){
              log.info("Error Field : "+error.getField()+" Error Message : "+error.getDefaultMessage());
                model.addAttribute(error.getField(),error.getDefaultMessage());
            }
            //throw new Exception("유효성 검증 오류!");
            return "memo/add";
        }

        //서비스 요청 -> Domain.Common.Service
//        boolean isAdded = memoService.memoRegistration(dto);
        Long insertedId = memoService.memoRegistration2(dto);
        if(insertedId!=null)
            redirectAttributes.addFlashAttribute("message","메모등록완료! : " + insertedId );
        //뷰로 이동
        return insertedId!=null ? "redirect:/":"memo/add";
    }

    @GetMapping("/list")
    public void list(
//            @RequestParam(value="pageNo",defaultValue = "0") int pageNo,
//            @RequestParam(value="amount",defaultValue = "10") int amount
            PageDto pageDto,
            Model model
    ) throws Exception
    {
        log.info("GET /memo/list...pageDto : " + pageDto);
        //파라미터 받기
        //유효성체크 생략
        //서비스 실행

        Map<String,Object> values = memoService.listMemo(pageDto);;

        Page<Memo> page = (Page<Memo>)values.get("page");
        PageBlock pageBlock = (PageBlock)values.get("pageBlock");

        //뷰로이동(+데이터)
        model.addAttribute("page" ,page);
        model.addAttribute("list" ,page.getContent());
        //페이징블럭 추가!
        model.addAttribute("pageBlock" ,pageBlock);


    }


}

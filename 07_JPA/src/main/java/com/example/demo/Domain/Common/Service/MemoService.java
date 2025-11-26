package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dao.MemoDao;
import com.example.demo.Domain.Common.Dto.MemoDto;
import com.example.demo.Domain.Common.Dto.PageBlock;
import com.example.demo.Domain.Common.Dto.PageDto;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Repository.MemoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MemoService {
    @Autowired
    private MemoDao memoDao;

    @Autowired
    private MemoRepository memoRepository;


    public boolean memoRegistration(MemoDto dto) throws Exception {
        int result = memoDao.insert(dto);
        return result>0;
    }

    public Long memoRegistration2(MemoDto dto) throws Exception {
        //dto -> entity
        Memo memo = Memo.builder()
                    .id(null)
                    .text(dto.getText())
                    .writer(dto.getWriter())
                    .createAt(LocalDateTime.now())
                    .build();
        memoRepository.save(memo);
        return memo.getId();
    }

    @Transactional(rollbackFor=Exception.class)
    public Map<String,Object> listMemo(PageDto pageDto) throws SQLException {
        Map<String,Object> resultValue = new LinkedHashMap();
        
        //01 PAGEING 처리
        int pageNo = 0;
        int amount = 10;
        if(pageDto.getPageNo()!=null)
            pageNo = pageDto.getPageNo();
        if(pageDto.getAmount()!=null)
            amount = pageDto.getAmount();

        Pageable pageable = PageRequest.of(pageNo,amount, Sort.by("id").descending());
        Page<Memo> page =  memoRepository.findAll(pageable);

        System.out.println("현재 페이지 번호 : "+page.getNumber());
        System.out.println("한페이지에 표시할 건수 : "+page.getSize());
        System.out.println("총게시물 개수 : "+page.getTotalElements());
        System.out.println("총페이지 개수 : "+page.getTotalPages());
        System.out.println("첫번째 페이지인지 여부 : "+page.isFirst());
        System.out.println("다음페이지가 있는지 여부 : "+page.hasNext());
        System.out.println("이전페이지가 있는지 여부 : "+page.hasPrevious());

        //02 PAGING BLOCK 처리
        pageDto.setPageNo(pageNo);
        pageDto.setAmount(amount);
        PageBlock<Memo> pageBlock = new PageBlock<Memo>(pageDto,page);
        System.out.println("페이징 블럭 결과 : " + pageBlock);
        //03 Map 에 여러 Object 저장
        resultValue.put("page",page);
        resultValue.put("pageBlock",pageBlock);
        return resultValue;
    }



}

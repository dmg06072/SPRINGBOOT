package com.example.demo.Model.Service;

import com.example.demo.Model.Dto.MemoDto;
import com.example.demo.Model.Entity.Memo;
import com.example.demo.Model.Repository.MemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MemoService {
    @Autowired
    private MemoRepository memoRepository;

    public boolean addMemo(MemoDto dto){
        log.info("MemoService's addMemo Start...");

        //dto -> entity
        Memo memo = new Memo();
        memo.setText(dto.getText());
        //memo add
        memoRepository.save(memo);

        log.info("MemoService's addMemo End...");
        return memo.getId()>0;
    }

    public List<Memo> getMemos(){
        log.info("MemoService's getMemos Start...");
        return memoRepository.findAll();
    }

}

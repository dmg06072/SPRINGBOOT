package com.example.demo.Domain.Common.Service;


import com.example.demo.Domain.Common.Dto.MemoDto;
import com.example.demo.Domain.Common.Entity.Memo;
import com.example.demo.Domain.Common.Mapper.MemoMapper;
import com.example.demo.Domain.Common.Repository.MemoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class TxTestService {
    @Autowired
    private MemoRepository memoRepository;

    public void addMemo() throws Exception
    {
        log.info("TxTestService's addMemo...");
        Memo memo = Memo.builder()
                .id(null)
                .text("tx")
                .writer("a")
                .createAt(LocalDateTime.now())
                .build();
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        throw new SQLException();
//        memoRepository.save(memo);
    }
    @Transactional(rollbackFor = SQLException.class,transactionManager = "jpaTransactionManager")
    public void addMemoTx() throws Exception
    {
        log.info("TxTestService's addMemoTx...");
        Memo memo = Memo.builder()
                .id(null)
                .text("tx")
                .writer("a")
                .createAt(LocalDateTime.now())
                .build();
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        memoRepository.save(memo);
        memo.setId(null);
        throw new SQLException();
//        memoRepository.save(memo);
    }


    @Autowired
    MemoMapper memoMapper;

    public void addMemoWithMybatis(MemoDto dto) throws Exception{
        dto.setId(9994L);
        memoMapper.insert(dto);
        dto.setId(9995L);
        memoMapper.insert(dto);
        dto.setId(9996L);
        memoMapper.insert(dto);
        throw new SQLException("예외발생!");
    }
    @Transactional(rollbackFor = SQLException.class ,transactionManager = "dataSourceTransactionManager")
    public void addMemoWithMybatisTx(MemoDto dto) throws Exception {
        dto.setId(8994L);
        memoMapper.insert(dto);
        dto.setId(8995L);
        memoMapper.insert(dto);
        dto.setId(8996L);
        memoMapper.insert(dto);
        throw new SQLException("예외발생!");
    }

}

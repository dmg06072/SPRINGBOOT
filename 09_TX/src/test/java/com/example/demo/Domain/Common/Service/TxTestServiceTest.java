package com.example.demo.Domain.Common.Service;

import com.example.demo.Domain.Common.Dto.MemoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TxTestServiceTest {
    @Autowired
    private TxTestService txTestService;

    @Test
    public void t1() throws Exception{
//        txTestService.addMemo();
        txTestService.addMemoTx();
    }


    @Test
    public void t2() throws Exception{
        txTestService.addMemoWithMybatis(new MemoDto(9990L,"a","a",null));
    }

    @Test
    public void t3() throws Exception{
        txTestService.addMemoWithMybatisTx(new MemoDto(8990L,"a","a",null));
    }

}
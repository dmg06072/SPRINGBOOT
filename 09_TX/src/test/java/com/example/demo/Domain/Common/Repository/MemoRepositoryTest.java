package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    private MemoRepository memoRepository;

    @Test
    public void t1(){
        Memo memo = new Memo(null,"내용1","test@test.com", LocalDateTime.now());
        memoRepository.save(memo);

        System.out.println("ID : " + memo.getId());
    }

    @Test
    public void t2 (){
        Memo memo = new Memo(1L,"수정된내용","test@test.com", LocalDateTime.now());
        memoRepository.save(memo);
        System.out.println("ID : " + memo.getId());
    }
    @Test
    public void t3(){
        memoRepository.deleteById(2L);
    }
    @Test
    public void t4(){
       Optional<Memo> memoOptional =   memoRepository.findById(10L);
       if(memoOptional.isPresent()){
           Memo memo = memoOptional.get();
           System.out.println(memo);
       }
    }
    @Test
    public void t5(){
        List<Memo> list = memoRepository.findAll();
        list.forEach(System.out::println);
        //(el)->{System.out.println(el);}
        //el->System.out.println(el);
        //System.out::println;
    }


}

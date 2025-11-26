package com.example.demo.Domain.Common.Mapper;

import com.example.demo.Domain.Common.Dto.MemoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemoMapperTest {

    @Autowired
    private MemoMapper memoMapper;

//    @Test
//    public void post1000(){
//        for(int i=1;i<1000;i++){
//            memoMapper.insert(new MemoDto(null,"content-"+i,"writer-"+i,LocalDateTime.now(),null));
//        }
//    }


    @Test
    public void t1(){
        MemoDto dto = new MemoDto(1L,"내용1","작성자1", LocalDateTime.now(),null);
        memoMapper.insert(dto);
    }
    @Test
    public void t2(){
        MemoDto dto = new MemoDto(1L,"내용1-UPDATE","작성자1-UPDATE", null,null);
        memoMapper.update(dto);

    }
    @Test
    public void t3(){
        memoMapper.delete(1L);
    }

    @Test
    public void t4(){
        MemoDto dto = new MemoDto(null,"내용1","작성자1", LocalDateTime.now(),null);
        memoMapper.insert(dto);
        System.out.println(dto);
    }

    @Test
    public void t5(){
        //
//        List<MemoDto> list = memoMapper.selectAll();
//        list.forEach(System.out::println);

        List<MemoDto> list = memoMapper.selectAllContains("writer","2");
        list.forEach(System.out::println);
    }


    @Test
        public void t6(){
            List<Map<String,Object>> list =
                    memoMapper.selectAllWithResultMap();

            list.forEach((map)->{
                System.out.println(map);
            });
    }

    @Test
    public void t7(){
        MemoDto dto = new MemoDto(2000L,"내용1","작성자1", LocalDateTime.now(),null);
        memoMapper.insertXML(dto);
    }

    @Test
    public void t8(){
        MemoDto dto = new MemoDto(1L,"내용1-UPDATE","작성자1-UPDATE", LocalDateTime.now(),null);
        memoMapper.updateXML(dto);
    }
    @Test
    public void t9(){
        memoMapper.deleteXML(1L);
    }
    @Test
    public void t10(){
        MemoDto dto = memoMapper.selectXML(2L);
        System.out.println(dto);
    }
    @Test
    public void t11(){
        List<MemoDto> list = memoMapper.selectAllXML();
        list.forEach(System.out::println);
    }

    @Test
    public void t12(){
        List< Map<String,Object> > list = memoMapper.selectAllMapXML();
        list.forEach(System.out::println);
    }
    @Test
    public void t13(){
        Map<String,Object> param = new HashMap();
        param.put("type","text");
        param.put("keyword","5");
        List< Map<String,Object> > list =  memoMapper.selectAllIfXML(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
    @Test
    public void t14(){
        Map<String,Object> param = new HashMap();
        param.put("field","1");
        param.put("type","writer");
        param.put("keyword","작성자");
        List< Map<String,Object> > list =  memoMapper.selectAllChooseXML(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
    @Test
    public void t15(){
        Map<String,Object> param = new HashMap();
        param.put("field","3");
        param.put("type",Arrays.asList("text","writer"));
        param.put("keyword","5");
        List< Map<String,Object> > list =  memoMapper.selectAllIfAndXML(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
    @Test
    public void t16(){
        Map<String,Object> param = new HashMap();
        param.put("fields", Arrays.asList("id","text","writer"));
        param.put("keyword","5");
        List< Map<String,Object> > list =  memoMapper.selectForEachAnd(param);
        System.out.println("TOTAL : " + list.size());
        list.forEach(System.out::println);
    }
}
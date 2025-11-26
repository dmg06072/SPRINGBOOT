package com.example.demo.Config;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MybatisConfigTest {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Test
    public void t1(){
        assertNotNull(sqlSessionFactory);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection conn =  sqlSession.getConnection();
        assertNotNull(conn);

    }


}
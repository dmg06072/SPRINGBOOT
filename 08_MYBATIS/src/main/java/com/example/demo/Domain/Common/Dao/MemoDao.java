package com.example.demo.Domain.Common.Dao;

import com.example.demo.Domain.Common.Dto.MemoDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Repository
public class MemoDao {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSession sqlSession;
    private String NAMESPACE = "com.example.demo.Domain.Common.Mapper.MemoMapper.";
    //MYBATIS 사용
    public Long insert(MemoDto dto) throws SQLException {
        sqlSession.insert(NAMESPACE+"insert",dto);
        System.out.println(dto);
        return dto.getId();
    }
    
//    public int insert(MemoDto dto) throws SQLException {
//        Connection conn = dataSource.getConnection();
//        PreparedStatement pstmt = conn.prepareStatement("insert into tbl_memo values(?,?,?,?)");
//        pstmt.setLong(1,dto.getId());
//        pstmt.setString(2,dto.getText());
//        pstmt.setString(3,dto.getWriter());
//        pstmt.setString(4, dto.getCreateAt().toString());
//        int result = pstmt.executeUpdate();
//        return result;
//    }
}

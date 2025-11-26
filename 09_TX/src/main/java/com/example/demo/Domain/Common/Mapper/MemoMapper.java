package com.example.demo.Domain.Common.Mapper;

import com.example.demo.Domain.Common.Dto.MemoDto;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemoMapper {

    @SelectKey(statement = "SELECT max(id) FROM testdb.tbl_memo",keyProperty = "id",before = false,resultType = Long.class)
    @Insert("insert into tbl_memo values(#{id},#{text},#{writer},#{createAt})")
    public int insert(MemoDto memoDto);

    @Update("update tbl_memo set text=#{text},writer=#{writer} where id=#{id}")
    public int update(MemoDto memoDto);

    @Delete("delete from tbl_memo where id=#{id}")
    public int delete(@Param("id") Long memoId);

    @Select("select * from tbl_memo")
    public List<MemoDto> selectAll();

    @Select("select * from tbl_memo where ${type} like concat('%',#{keyword},'%')")
    public List<MemoDto> selectAllContains(String type,String keyword);

//    @Results(id="MemoResultMap",value={
//            @Result(property = "text",column = "text"),
//            @Result(property = "writer",column = "writer")
//    })
    @Select("select text,writer from tbl_memo")
    public List< Map<String,Object> > selectAllWithResultMap();


    //XML
    public int insertXML(MemoDto memoDto);
    public int updateXML(MemoDto memoDto);
    public int deleteXML(@Param("id") Long memoId);
    public MemoDto selectXML(@Param("id") Long id);
    public List<MemoDto> selectAllXML();
    public List< Map<String,Object> > selectAllMapXML();
    public List< Map<String,Object> > selectAllIfXML(Map<String,Object> param);
    public List< Map<String,Object> > selectAllChooseXML(Map<String,Object> param);
    public List< Map<String,Object> > selectAllIfAndXML(Map<String,Object> param);
    public List< Map<String,Object> > selectForEachAnd(Map<String,Object> param);

}

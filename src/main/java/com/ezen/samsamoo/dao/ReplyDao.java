package com.ezen.samsamoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ezen.samsamoo.dto.Reply;

import java.util.List;

@Mapper
public interface ReplyDao {
	
    // 댓글 번호를 이용해 댓글 가지고 오기
    Reply getReplyById(@Param("id") int id);

    // 관련 데이터 코드와 관련 데이터 번호를 이용해서 댓글 리스트 출력
    List<Reply> getForPrintRepliesByRelTypeCodeAndRelId(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);
    
	// 댓글 쓰기
    void write(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("memberId") int memberId, @Param("body") String body);
    
    // 댓글 수정하기
    void modify(@Param("id") int id, @Param("body") String body);

    // 댓글 삭제하기
    void delete(@Param("id") int id);

    // insert 이후 데이터가 들어가 auto_increment로 되어있는 PK값을 가져오기  
    int getLastInsertId();

}

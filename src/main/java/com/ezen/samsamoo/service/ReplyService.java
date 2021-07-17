package com.ezen.samsamoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.samsamoo.dao.ReplyDao;
import com.ezen.samsamoo.dto.Reply;
import com.ezen.samsamoo.dto.ResultData;

import java.util.List;

@Service
public class ReplyService {
	
    @Autowired
    private ReplyDao replyDao;

    
//----------------------------------------------------------------------------------------------------    
    // 댓글 번호를 통해 댓글 한 개 가져오기
    public Reply getReplyById(int id) {
        return replyDao.getReplyById(id);
    }
    
    
    // 관련 타입과 관련 번호를 통해서 댓글의 자세한 정보 가져오기
    public List<Reply> getForPrintRepliesByRelTypeCodeAndRelId(String relTypeCode, int relId) {
        return replyDao.getForPrintRepliesByRelTypeCodeAndRelId(relTypeCode, relId);
    }
    
    
//----------------------------------------------------------------------------------------------------  
   // 댓글 작성하기
    public ResultData write(String relTypeCode, int relId, int memberId, String body) {
        replyDao.write(relTypeCode, relId, memberId, body);
        int id = replyDao.getLastInsertId();

        return new ResultData("S-1", "댓글이 작성되었습니다.", "id", id);
    }

  //----------------------------------------------------------------------------------------------------         
    // 댓글 수정하기
    public ResultData modify(int id, String body) {
        replyDao.modify(id, body);

        return new ResultData("S-1", id + "번 댓글이 수정되었습니다.", "id", id);
    }

    
    // 댓글 삭제하기
    public ResultData delete(int id) {
        replyDao.delete(id);

        return new ResultData("S-1", id + "번 댓글이 삭제되었습니다.", "id", id);
    }

}

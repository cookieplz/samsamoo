package com.ezen.samsamoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.samsamoo.dto.Article;
import com.ezen.samsamoo.dto.Board;
import com.ezen.samsamoo.dto.Reply;
import com.ezen.samsamoo.dto.ResultData;
import com.ezen.samsamoo.dto.Rq;
import com.ezen.samsamoo.service.ArticleService;
import com.ezen.samsamoo.service.ReplyService;
import com.ezen.samsamoo.util.Util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdmReplyController {
	
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private ReplyService replyService;


//----------------------------------------------------------------------------------------------------    
    // 댓글 쓰기
    @RequestMapping("/adm/reply/doWrite")
    public String doWrite(HttpServletRequest req, String relTypeCode, int relId, String body, String redirectUri) {
        switch ( relTypeCode ) {
            case "article":
                Article article = articleService.getArticleById(relId);
                if ( article == null ) {
                    return Util.msgAndBack(req, "해당 게시물이 존재하지 않습니다.");
                }
                break;
            default:
                return Util.msgAndBack(req, "올바르지 않은 relTypeCode 입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        int memberId = rq.getLoginedMemberId();

        ResultData rd = replyService.write(relTypeCode, relId, memberId, body);

        int newReplyId = (int)rd.getBody().get("id");

        redirectUri = Util.getNewUri(redirectUri, "focusReplyId", newReplyId + "");

        return Util.msgAndReplace(req, rd.getMsg(), redirectUri);
    }
  //---------------------------------------------------------------------------------------------------- 

   
     
//----------------------------------------------------------------------------------------------------       
    // 답글 id를 통해 객체 얻어와서 권한 등 체크 후 답글 수정하는 페이지로 넘겨줌
    @RequestMapping("/adm/reply/modify")
    public String showModify(HttpServletRequest req, int id, String redirectUri) {
        Reply reply = replyService.getReplyById(id);

        if ( reply == null ) {
            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
            return Util.msgAndBack(req, "권한이 없습니다.");
        }

        req.setAttribute("reply", reply);		// 수정해야 할 댓글을 req에 담고

        String title = "";
        // 만약 댓글의 관련타입코드가 article이라면
        switch ( reply.getRelTypeCode() ) {
            case "article":
                Article article = articleService.getArticleById(reply.getRelId());
                title = article.getTitle();			// 게시물 제목도 req에 담음
        }
       
        req.setAttribute("title", title);

        return "adm/reply/modify";
    }

    
    // 답글 수정하기
    @RequestMapping("/adm/reply/doModify")
    public String doModify(HttpServletRequest req, int id, String body, String redirectUri) {
        Reply reply = replyService.getReplyById(id);

        if ( reply == null ) {
            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");
        // 댓글 쓴 회원이랑 지금 로그인 중인 회원이 다르다면 
        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
            return Util.msgAndBack(req, "권한이 없습니다.");
        }

        ResultData modifyResultData = replyService.modify(id, body);

        redirectUri = Util.getNewUri(redirectUri, "focusReplyId", id + "");

        return Util.msgAndReplace(req, modifyResultData.getMsg(), redirectUri);
    }
    
//----------------------------------------------------------------------------------------------------      
    // ajax로 댓글 삭제하기
    @RequestMapping("/adm/reply/doDeleteAjax")
    @ResponseBody
    public ResultData doDeleteAjax(HttpServletRequest req, int id, String redirectUri) {
        Reply reply = replyService.getReplyById(id);

        if ( reply == null ) {
            return new ResultData("F-1", "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
            return new ResultData("F-1", "권한이 없습니다.");
        }

        ResultData deleteResultData = replyService.delete(id);

        return new ResultData("S-1", String.format("%d번 댓글이 삭제되었습니다.", id));
    }


    // 댓글 삭제하기
    @RequestMapping("/adm/reply/doDelete")
    public String doDelete(HttpServletRequest req, int id, String redirectUri) {
        Reply reply = replyService.getReplyById(id);

        if ( reply == null ) {
            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
            return Util.msgAndBack(req, "권한이 없습니다.");
        }

        ResultData deleteResultData = replyService.delete(id);

        return Util.msgAndReplace(req, deleteResultData.getMsg(), redirectUri);
    }       
    
}

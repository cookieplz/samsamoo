package com.ezen.samsamoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article {
    private int id;	// 게시물 번호
    private String regDate;	// 게시물 등록날짜
    private String updateDate;	// 게시물 수정날짜
    private int boardId;	// 게시판 번호	
    private int memberId;	// 글쓴회원 번호
    private String title;	// 게시글 제목
    private String body;	// 게시글 내용
    private boolean blindStatus;	// 블라인드 여부
    private String blindDate;	// 블라인드 날짜	
    private boolean delStatus;	// 삭제 여부
    private String delDate;	//삭제날짜
    private int hitCount;	// 조회수
    private int repliesCount;	// 댓글수
    private int likeCount;	// 좋아요수
    private int dislikeCount;	// 싫어요수

    // 기타 다른 정보들을 담기 위해서
    private Map<String, Object> extra;

    // 작성자 별명을 담기 위해
    private String extra__writerName;

    
//--------------------------------------------------------------------------  
   // html로 게시글 내용 그대로 가져오기
   // 개행문자(\n을 <br/>로 치환)
    public String getBodyForPrint() {
        String bodyForPrint = body.replaceAll("\r\n", "<br>");
        bodyForPrint = bodyForPrint.replaceAll("\r", "<br>");
        bodyForPrint = bodyForPrint.replaceAll("\n", "<br>");

        return bodyForPrint;
    }

    
    // 프로필 이미지 Uri 가지고 오기
    public String getWriterProfileImgUri() {
        return "/common/samFile/file/member/" + memberId + "/extra/profileImg/1";
    }

    
    // 프사 없을 때 대체 이미지
    public String getWriterProfileFallbackImgUri() {
        return "https://via.placeholder.com/300?text=(^o^)/★";
    }

    // 대체 이미지를 html에 삽입하기 위한 메서드 
    public String getWriterProfileFallbackImgOnErrorHtmlAttr() {
        return "this.src = '" + getWriterProfileFallbackImgUri() + "'";
    }
    
}
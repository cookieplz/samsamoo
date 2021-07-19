package com.ezen.samsamoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reply {
    private int id;	// 번호	
    private String regDate;	// 작성 날짜	
    private String updateDate;	// 수정 날짜
    private String relTypeCode;		// 관련 데이터 타입 (ex: article, member)
    private int relId;	// 관련 데이터 번호(ex: 게시글 id)
    private int memberId;	// 댓글쓴 회원 id
    private int parentId;		// 부모 댓글 id
    private String body;		// 댓글 내용
    private boolean blindStatus;	// 블라인드 여부
    private String blindDate;	// 블라인드 날짜
    private boolean delStatus;	//	삭제 여부
    private String delDate;	// 삭제 날짜
    private int likeCount;	// 좋아요 수
    private int dislikeCount;	//싫어요 수

    private String extra__writerName;		// 댓글쓴 회원 닉네임

    
    // 댓글 내용 그대로 가져오는 함수
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
        return "/resource/img/profileNongdamgom";
    }

    // 대체 이미지를 html에 삽입하기 위한 메서드 
    public String getWriterProfileFallbackImgOnErrorHtmlAttr() {
        return "this.src = '" + getWriterProfileFallbackImgUri() + "'";
    }
    
}
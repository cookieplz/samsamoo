package com.ezen.samsamoo.dto;

import com.ezen.samsamoo.service.MemberService;
import com.ezen.samsamoo.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {
    private int id;	// 멤버 번호	
    private String regDate;	// 가입 날짜
    private String updateDate;	// 회원수정 날짜
    private String loginId;	// 로그인 아이디
    private String loginPw;		// 로그인 패스워드
    private int authLevel;	// 회원 등급
    private String name;		// 이름
    private String nickname;		// 닉네임
    private String cellphoneNo;	// 폰번호
    private String email;		// 이메일
    private boolean delStatus;	// 탈퇴여부 (탈퇴안한 기본값이 0)
    private String delDate;		// 탈퇴날짜

    
    // 제이슨 문자열로 변환하는 함수
    public String toJsonStr() {
        return Util.toJsonStr(this);
    }

    
    // 프로필 이미지 uri 가져오기
    public String getProfileImgUri() {
        return "/common/samFile/file/member/" + id + "/extra/profileImg/1";
    }

    
    // 프로필 대체 이미지 uri 가져오기
    public String getProfileFallbackImgUri() {
        return "https://via.placeholder.com/300?text=^_^";
    }

    
    // 대체 이미지를 html에 삽입하기 위한 메서드
    public String getProfileFallbackImgOnErrorHtmlAttr() {
        return "this.src = '" + getProfileFallbackImgUri() + "'";
    }

    
    // 이미지 없으면 걍 삭제해주는 메서드
    public String getRemoveProfileImgIfNotExistsOnErrorHtmlAttr() {
        return "$(this).remove();";
    }

    
    // 회원의 회원등급을 가져오는 메서드
    public String getAuthLevelName() {
        return MemberService.getAuthLevelName(this);
    }

    
    // 회원의 회원등급에 따라 색깔을 다르게 분류한 것을 가져오는 메서드
    public String getAuthLevelNameColor() {
        return MemberService.getAuthLevelNameColor(this);
    }
    
    
}
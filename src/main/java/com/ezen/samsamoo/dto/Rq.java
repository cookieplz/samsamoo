package com.ezen.samsamoo.dto;

import lombok.Getter;

import java.util.Map;

import com.ezen.samsamoo.util.Util;

//Rq를 이용해서 로그인 상태에 따라서 메뉴를 분기한다.
public class Rq {
    @Getter
    private boolean isAjax;

    @Getter
    private boolean isAdmin;
    
    @Getter
    private String currentUrl;
    
    @Getter
    private String currnetUri;
    
    private Member loginedMember;
    
    private Map<String, String> paramMap;
    
    @Getter
    private boolean needToChangePassword;

    
    // Rq 객체 생성할 때 이하의 정보들을 다 담음
    public Rq(boolean isAjax, boolean isAdmin, Member loginedMember, String currentUri, Map<String, String> paramMap, boolean needToChangePassword) {
        this.isAjax = isAjax;
        this.isAdmin = isAdmin;
        this.loginedMember = loginedMember;
        this.currentUrl = currentUri.split("\\?")[0];
        this.currnetUri = currentUri;
        this.paramMap = paramMap;
        this.needToChangePassword = needToChangePassword;
    }

    // Json 문자열로 변환
    public String getParamJsonStr() {
        return Util.toJsonStr(paramMap);
    }

    // 관리자인지 아닌지
    public boolean isNotAdmin() {
        return isAdmin == false;
    }

    // 로그인 되어있는지
    public boolean isLogined() {
        return loginedMember != null;
    }

    // 로그아웃 되어있는지
    public boolean isNotLogined() {
        return isLogined() == false;
    }

    // 로그인된 멤버 아이디를 가져오기. 로그아웃되어있으면 0으로 리턴
    public int getLoginedMemberId() {
        if (isNotLogined()) return 0;

        return loginedMember.getId();
    }

    // 로그인된 멤버의 정보를 가져오기
    public Member getLoginedMember() {
        return loginedMember;
    }

    //현재 uri를 인코딩해서 가져오기
    public String getEncodedCurrentUri() {
        return Util.getUriEncoded(getCurrentUri());
    }

    // 현재 uri를 가져오기
    public String getCurrentUri() {
        return currnetUri;
    }

    // 로그인 상태에 따라 페이지의 uri 가져오기
    public String getLoginPageUri() {
        String afterLoginUri;

        if (isLoginPage()) {
            afterLoginUri = Util.getUriEncoded(paramMap.get("afterLoginUri"));
        } else {
            afterLoginUri = getEncodedCurrentUri();
        }

        return "../member/login?afterLoginUri=" + afterLoginUri;
    }

    // 현재 uri가 로그인페이지인지 판별
    private boolean isLoginPage() {
        return currentUrl.equals("/usr/member/login");
    }
}

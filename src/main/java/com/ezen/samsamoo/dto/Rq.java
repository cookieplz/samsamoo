package com.ezen.samsamoo.dto;

import lombok.Getter;

import java.util.Map;

import com.ezen.samsamoo.util.Util;

//Rq를 이용해서 로그인 상태에 따라서 메뉴를 분기한다.
public class Rq{
	@Getter
	private boolean isAjax;
	
	@Getter
	private boolean isAdmin;
	
	private String currentUrl;
	
	@Getter
	private String currentUri;
	
	private Member loginedMember;
	
	private Map<String, String> paramMap;
	
	@Getter
	private boolean needToChangePassword;
	

	 public Rq(boolean isAjax, boolean isAdmin, Member loginedMember, String currentUri, Map<String, String> paramMap, boolean needToChangePassword) {
	        this.isAjax = isAjax;
	        this.isAdmin = isAdmin;
	        this.loginedMember = loginedMember;
	        this.currentUrl = currentUri.split("\\?")[0];
	        this.currentUri = currentUri;
	        this.paramMap = paramMap;
	        this.needToChangePassword = needToChangePassword;
	    }
	 
	 
	 public String getParamJsonStr() {
		 return Util.toJsonStr(paramMap);
	 }
	 
	 public boolean isNotAdmin() {
		 return isAdmin == false;
	 }
	 
	 public boolean isLogined() {
		 return loginedMember != null;
	 }
	 
	 public boolean isNotLogined() {
		 return isLogined() == false;
	 }
	 
	 public int getLoginedMemberId() {
		 if(isNotLogined()) {
			 return 0;
		 }
		 return loginedMember.getId();
	 }
	 
	 public Member getLoginedMember() {
		 return loginedMember;
	 }
	 
	 public String getEncodedCurrentUri() {
		 return Util.getUriEncoded(getCurrentUri());
	 }
	 
	 public String getCurrentUri() {
		 return currentUri;
	 }
	 
	 public String getLoginPageUri() {
		 String afterLoginUri;
		 if(isLoginPage()) {
			 afterLoginUri = Util.getUriEncoded(paramMap.get("afterLoginUri"));
		 }else {
			 afterLoginUri = getEncodedCurrentUri();
		 }
		 return "../member/login?afterLoginUri=" + afterLoginUri;
	 }
	 
	 private boolean isLoginPage() {
		 return currentUrl.equals("/usr/member/login");
	 }
	 
}

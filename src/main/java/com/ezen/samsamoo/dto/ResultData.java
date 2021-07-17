package com.ezen.samsamoo.dto;

import java.util.Map;

import com.ezen.samsamoo.util.Util;

import lombok.Data;


// 결과코드, 메시지, 변수를 하나의 객체에 다 담을 수 있다. 유틸성 클래스
@Data
public class ResultData {
	private String resultCode;	// 결과 코드 (ex: S-1, F-1 등)
	private String msg;	// 저장하고 싶은 메시지
	private Map<String, Object> body;	// 저장하고 싶은 변수 아무거나 다 들어갈 수 있도록 맵으로 함. 반드시 
	
	
	// 객체 생성될 때 resultCode, msg, args 다 담을 수 있음)
	public ResultData(String resultCode, String msg, Object... args) {
		this.resultCode = resultCode;
		this.msg = msg;
		this.body = Util.mapOf(args);
	}
	
	
	// 성공 했는지 resultCode의 앞자리를 보고 확인해주는 함수
	public boolean isSuccess() {	
		return resultCode.startsWith("S-");
	}
	
	
	// 실패 했는지 resultCode의 앞자리를 보고 확인해주는 함수
	public boolean isFail() {
		return isSuccess() == false;
	}
	
	
}

package com.ezen.samsamoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 부가 정보 테이블
// 메인에 들어가기는 아쉽고 그러나 없으면 안되는 쩌리 정보들
// 예컨대 어떤 회원이 가장 최근에 언제 정지를 당했는지와 같은 정보를 저장하기 위해서
// member 테이블에 칼럼을 하나 만드면 그건 너무 데이터 낭비가 크다
// 따라서 이러한 사소한 기록을 남기기 위해 만든 테이블이 바로 EtcInfo 테이블이다.
// 다수의 테이블의 데이터를 담기 위해 존재한다.
public class EtcInfo {
    private int id;	// 부가 정보 테이블 번호	
    private String regDate;	// 작성 일자
    private String updateDate;	// 수정 일자
    private String expireDate;	// 만료 일자
    private String relTypeCode;	// 관련타입코드	-> 이 테이블에 들어가는 데이터가 온갖 잡것들(ex: member)이 다 들어가기때문에 구분을 해주기 위해 존재   
    private int relId;	// 관련번호	// ex: 1
    private String typeCode;	// 타입코드	-> 대분류(ex: extra(부가정보)) 
    private String type2Code;	//타입2코드	-> 소분류(ex: modifyPrivateAuthCode)
    private String value;		// ex: 1234
    
    
    // 따라서 이 경우에는 
    /* 	INSERT INTO attr
     * 		SET regDate = NOW(),
     * 		updateDate = NOW(),
     * 		relTypeCode = 'member',
     * 		relId = 1,
     * 		typeCode = 'extra',
     * 		type2Code = 'modifyPrivateAuthCode',
     * 		`value` = '1234',
     * 		expireDate = '2021-07-17 17:00:50';			지금 시간이 16:00:00이라면 1시간 짜리 인증코드를 발급해준셈
     * 
     * -> 1번 회원이 회원정보를 수정하기 위해 비밀번호를 입력하고 서버쪽에서 비밀번호를 체크 한후
     * 		한 시간 짜리 authCode(인증코드)를 발급해준다. 
     */
}

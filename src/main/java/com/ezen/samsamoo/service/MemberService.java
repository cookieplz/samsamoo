package com.ezen.samsamoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ezen.samsamoo.dao.MemberDao;
import com.ezen.samsamoo.dto.Member;
import com.ezen.samsamoo.dto.ResultData;
import com.ezen.samsamoo.util.Util;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private EtcInfoService etcInfoService;

    @Autowired
    private MailService mailService;

    // application.yml의 변수를 쓰기위해서 @Value 사용
    @Value("${custom.siteMainUri}")
    private String siteMainUri;
    
    @Value("${custom.siteName}")
    private String siteName;

    @Value("${custom.needToChangePasswordFreeDays}")
    private int needToChangePasswordFreeDays;

    @Autowired
    private MemberDao memberDao;

    
//----------------------------------------------------------------------------------------------------         
    // 회원 번호를 통해 회원 한 명의 데이터를 가져오는 함수
    public Member getMemberById(int id) {
        return memberDao.getMemberById(id);
    }

    
    // 로그인 아이디를 통해 회원 한명의 데이터를 가져오는 함수
    public Member getMemberByLoginId(String loginId) {
        return memberDao.getMemberByLoginId(loginId);
    }

    
    // 회원 이름과 이메일로 회원 한 명의 데이터를 가져오는 함수
    public Member getMemberByNameAndEmail(String name, String email) {
        return memberDao.getMemberByNameAndEmail(name, email);
    }


    // 회원들의 자세한 정보를 뽑아서 리스트로 가져오는 함수
    public List<Member> getForPrintMembers() {
        return memberDao.getForPrintMembers();
    }

    
//----------------------------------------------------------------------------------------------------      
    // 관리자인지 확인하는 함수
    public boolean isAdmin(Member member) {
        if ( member == null ) {
            return false;
        }
        return member.getAuthLevel() == 1;
    }

    
    // getAuthLevel이 1일 경우 관리자를, 2일 경우 일반을 리턴해주는 함수
    public static String getAuthLevelName(Member member) {
        switch (member.getAuthLevel()) {
            case 1:
                return "관리자";
            case 2:
                return "일반";
            default:
                return "유형정보없음";
        }
    }

    
    //  getAuthLevel이 1일 경우 red를, 2일 경우 gray을 리턴해주는 함수(authLevel에 따라 이름색을 달리 표시해주기 위해)
    public static String getAuthLevelNameColor(Member member) {
        switch (member.getAuthLevel()) {
            case 1:
                return "red";
            case 2:
                return "gray";
            default:
                return "";
        }
    }
//----------------------------------------------------------------------------------------------------  
    // 회원 가입하기
    public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
        memberDao.join(loginId, loginPw, name, nickname, cellphoneNo, email);
        int id = memberDao.getLastInsertId();

       //setNeedToChangePasswordLater(id); -> 안쓰는 기능 일단 주석

        return new ResultData("S-1", "회원가입이 완료되었습니다.", "id", id);
    }

    
    //  회원정보 수정하기
    public ResultData modify(int id, String loginPw, String name, String nickname, String cellphoneNo, String email) {
        memberDao.modify(id, loginPw, name, nickname, cellphoneNo, email);

        if (loginPw != null) {
            setNeedToChangePasswordLater(id);
            etcInfoService.remove("member", id, "extra", "useTempPassword");
        }

        return new ResultData("S-1", "회원정보가 수정되었습니다.", "id", id);
    }
//----------------------------------------------------------------------------------------------------    
    // 인증코드가 유효한지 체크하는 메서드
    public ResultData checkValidCheckPasswordAuthCode(int actorId, String checkPasswordAuthCode) {
        if (etcInfoService.getValue("member__" + actorId + "__extra__checkPasswordAuthCode").equals(checkPasswordAuthCode)) {
            return new ResultData("S-1", "유효한 키 입니다.");
        }

        return new ResultData("F-1", "유효하지 않은 키 입니다.");
    }


    /*
	 * ★ checkPasswordAuthCode -> 이게 뭐냐면 예컨대 우리가 중요한 일을 하기 전에 사이트 쪽에서 너 비밀번호 달라고
	 * 요구하는데 그때 우리가 비밀번호를 사이트에 입력해서 사이트 측에서 비밀번호를 다시 한번 확인했을 때 발급되는 인증 코드를 말한다.
	 * 인증코드의유효성을 체크한다 checkPasswordAuthCode -> 체크비밀번호인증코드. 이 코드를 가지고 있는 사람은 방금 전에
	 * 비밀번호를 체크했던 사람이라는 것을 알려줌 checkValidCheckPasswordAuthCode -> 체크비밀번호인증코드가 유효한지
	 * 체크
	 * 
	 * ★ member__1__extra__modify 의미 etcInfoService.getValue("member", 1, "extra",
	 * "modifyPrivateAuthCode") 이렇게 쓰는게 너무 불편함
	 * etcInfoService.getValue("member__1__extra__modifyPrivateAuthCode") 이렇게 쓸 수
	 * 있도록 메서드를 만들어 놓았음
	 */
    
    
    // 비밀번호 체크해서 비밀번호가 맞으면 인증코드 발급해주는 함수
    public String samCheckPasswordAuthCode(int actorId) {
        String attrName = "member__" + actorId + "__extra__checkPasswordAuthCode";
        String authCode = UUID.randomUUID().toString();	//UUID.randomUUID()를 통해 2f48f241-9d64-4d16-bf56-70b9d4e0e79a 이와같은 형태로 고유한 값을 생성 (유일식별자.)
        String expireDate = Util.getDateStrLater(60 * 60);		// 만료 시간은 1시간짜리로~

        etcInfoService.setValue(attrName, authCode, expireDate);	// 

        return authCode;
    }
    
    
//----------------------------------------------------------------------------------------------------      
	// 비번이 유효한 날짜
	// 비번 사용한지 몇일 지났는지 리턴해줌
	// 참고로 90일이 지나면 비밀번호 변경해야 함
	public int getNeedToChangePasswordFreeDays() {
		return needToChangePasswordFreeDays;
	}

    
    // 비밀변호 사용한지 얼마나 지났는지 알려주기 위한 메서드
    // "현재 비밀번호를 사용한지 " + memberService.getNeedToChangePasswordFreeDays() + "일이 지났습니다. 비밀번호를 변경해주세요.";
    private void setNeedToChangePasswordLater(int actorId) {
        int days = getNeedToChangePasswordFreeDays();
     // 참고로 60*60 -> 1시간. 60*60*24 -> 하루. 60*60*24*90 -> 90일
        etcInfoService.setValue("member", actorId, "extra", "needToChangePassword", "0", Util.getDateStrLater(60 * 60 * 24 * days));	// (60*60*24*days = days일)	
    }
   

    //	회원 아이디를 통해 이 회원이 비밀번호를 변경이 필요한지 여부를 가리는 함수
    // return 값이 0이면 비밀번호 변경할 필요 없음
    // return 값이 0이 아닐 경우 비밀번호를 변경해야 함
    public boolean needToChangePassword(int memberId) {
        return etcInfoService.getValue("member", memberId, "extra", "needToChangePassword").equals("0") == false;
    }
     
    
//----------------------------------------------------------------------------------------------------  
    // 비밀번호를 임시 비밀번호로 수정해서 설정해주는 함수
    private void setTempPassword(Member member, String tempPassword) {
    	// expireDate: null -> 이 데이터는 만료되어서는 안된다.
        etcInfoService.setValue("member", member.getId(), "extra", "useTempPassword", "1", null);
        memberDao.modify(member.getId(), tempPassword, null, null, null, null);
    }
    
    
    // 임시 비밀번호 발송을 위한 함수
    public ResultData notifyTempLoginPwByEmail(Member member) {
        String title = "[" + siteName + "] 임시 패스워드 발송";	// 메일 제목
        String tempPassword = Util.getTempPassword(6);	// 임시비밀번호
        String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";	// 메일 내용
        body += "<a href=\"" + siteMainUri + "/mpaUsr/member/login\" target=\"_blank\">로그인 하러가기</a>";	// 로그인 uri를 보냄

        ResultData sendResultData = mailService.send(member.getEmail(), title, body);

        if (sendResultData.isFail()) {
            return sendResultData;
        }
        // 임시 비밀번호를  sha256 해시로 변환
        tempPassword = Util.sha256(tempPassword);

        setTempPassword(member, tempPassword);

        return new ResultData("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
    }

   
   	/* 임시 비밀번호를 사용하고 있는지 여부를 알려주기 위한 메서드		-> 일단 주석 처리.
   	public boolean usingTempPassword(int memberId) {
   		return etcInfoService.getValue("member", memberId, "extra", "useTempPassword").equals("1");
   	}
   	*/


}

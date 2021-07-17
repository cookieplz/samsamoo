package com.ezen.samsamoo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.ezen.samsamoo.dto.Member;
import com.ezen.samsamoo.dto.ResultData;
import com.ezen.samsamoo.dto.Rq;
import com.ezen.samsamoo.service.SamFileService;
import com.ezen.samsamoo.service.MemberService;
import com.ezen.samsamoo.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UsrMemberController {
	
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private SamFileService samFileService;

    
//----------------------------------------------------------------------------------------------------  
    // 로그인 페이지로 가기
    @RequestMapping("/usr/member/login")
    public String showLogin(HttpServletRequest req) {
        return "usr/member/login";
    }
    
    
    // 로그인 하기
    @RequestMapping("/usr/member/doLogin")
    public String doLogin(HttpServletRequest req, HttpSession session, String loginId, String loginPw, String
            redirectUri) {
        if (Util.isEmpty(redirectUri)) {
            redirectUri = "/";
        }

        Member member = memberService.getMemberByLoginId(loginId);

        if (member == null) {
            return Util.msgAndBack(req, loginId + "(은)는 존재하지 않는 로그인아이디 입니다.");
        }

        if (member.getLoginPw().equals(loginPw) == false) {
            return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute("loginedMemberId", member.getId());
        session.setAttribute("loginedMemberJsonStr", member.toJsonStr());

        String msg = "환영합니다.♥";

        boolean needToChangePassword = memberService.needToChangePassword(member.getId());
        // 만약 needTochangePassword가 참(1)일 경우에는
        if ( needToChangePassword ) {
            msg = "현재 비밀번호를 사용한지 " + memberService.getNeedToChangePasswordFreeDays() + "일이 지났습니다. 비밀번호를 변경해주세요.";
            redirectUri = "/usr/member/mypage";
        }

        return Util.msgAndReplace(req, msg, redirectUri);
    }

    
    // 로그아웃 하기
    @RequestMapping("/usr/member/doLogout")
    public String doLogout(HttpServletRequest req, HttpSession session) {
        session.removeAttribute("loginedMemberId");

        String msg = "로그아웃 되었습니다.";
        return Util.msgAndReplace(req, msg, "/");
    }

//----------------------------------------------------------------------------------------------------  
    // 마이페이지로 가기
    @RequestMapping("/usr/member/mypage")
    public String showMypage(HttpServletRequest req) {
        return "usr/member/mypage";
    }
    
//----------------------------------------------------------------------------------------------------  
   // 아이디 찾기 페이지로 가기
    @RequestMapping("/usr/member/findLoginId")
    public String showFindLoginId(HttpServletRequest req) {
        return "usr/member/findLoginId";
    }

    
    // 아이디 찾기
    @RequestMapping("/usr/member/doFindLoginId")
    public String doFindLoginId(HttpServletRequest req, String name, String email, String redirectUri) {
        if (Util.isEmpty(redirectUri)) {
            redirectUri = "/";
        }
        // 회원이름과 이메일로 어떤 회원인지 찾은 후
        Member member = memberService.getMemberByNameAndEmail(name, email);

        if (member == null) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }

        return Util.msgAndBack(req, String.format("회원님의 아이디는 `%s` 입니다.", member.getLoginId()));
    }
//----------------------------------------------------------------------------------------------------  
    // 비밀번호 찾기 페이지로 가기
    @RequestMapping("/usr/member/findLoginPw")
    public String showFindLoginPw(HttpServletRequest req) {
        return "usr/member/findLoginPw";
    }

    // 비밀번호 찾기
    @RequestMapping("/usr/member/doFindLoginPw")
    public String doFindLoginPw(HttpServletRequest req, String loginId, String name, String email, String redirectUri) {
        if (Util.isEmpty(redirectUri)) {
            redirectUri = "/";
        }
        // 로그인 아이디로 회원 누구인지 찾은 후
        Member member = memberService.getMemberByLoginId(loginId);

        if (member == null) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }

        if (member.getName().equals(name) == false) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }

        if (member.getEmail().equals(email) == false) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }
        // 메일로 임시비밀번호 발급!
        ResultData tempPw = memberService.notifyTempLoginPwByEmail(member);

        return Util.msgAndReplace(req, tempPw.getMsg(), redirectUri);
    }

//----------------------------------------------------------------------------------------------------  
   // 회원가입 페이지로 가기
    @RequestMapping("/usr/member/join")
    public String showJoin(HttpServletRequest req) {
        return "usr/member/join";
    }

    
    // 로그인 아이디 중복 체크하기
    @RequestMapping("/usr/member/getLoginIdDup")
    @ResponseBody
    public ResultData getLoginIdDup(HttpServletRequest req, String loginId) {
        Member member = memberService.getMemberByLoginId(loginId);

        if (member != null) {
            return new ResultData("F-1", "해당 로그인아이디는 이미 사용중입니다.", "loginId", loginId);
        }

        return new ResultData("S-1", "사용가능한 로그인 아이디 입니다.", "loginId", loginId);
    }

    
    // 회원가입 하기.
    // 참고로 multipartRequest는 파일첨부를 위한 객체임
    @RequestMapping("/usr/member/doJoin")
    public String doJoin(HttpServletRequest req, String loginId, String loginPw, String name, String
            nickname, String cellphoneNo, String email, MultipartRequest multipartRequest) {
    	// 로그인 아이디를 통해 이미 있는 유저의 정보를 가져옴
        Member oldMember = memberService.getMemberByLoginId(loginId);

        if (oldMember != null) {
            return Util.msgAndBack(req, loginId + "(은)는 이미 사용중인 로그인아이디 입니다.");
        }

        oldMember = memberService.getMemberByNameAndEmail(name, email);

        if (oldMember != null) {
            return Util.msgAndBack(req, String.format("%s님은 이미 %s 메일주소로 %s 에 가입하셨습니다.", name, email, oldMember.getRegDate()));
        }
        // 새로가입하게 된 회원의 정보를 받음
        ResultData joinMember = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);

        if (joinMember.isFail()) {
            return Util.msgAndBack(req, joinMember.getMsg());
        }

        int newMemberId = (int)joinMember.getBody().get("id");

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        for (String fileInputName : fileMap.keySet()) {
            MultipartFile multipartFile = fileMap.get(fileInputName);

            if ( multipartFile.isEmpty() == false ) {
                samFileService.save(multipartFile, newMemberId);
            }
        }

        
        return Util.msgAndReplace(req, joinMember.getMsg(), "/");
    }

    
    // 비밀번호 체크 페이지로 가기
    @RequestMapping("/usr/member/checkPassword")
    public String showCheckPassword(HttpServletRequest req) {
        return "usr/member/checkPassword";
    }

    
    // 비밀번호 체크하기
    @RequestMapping("/usr/member/doCheckPassword")
    public String doCheckPassword(HttpServletRequest req, String loginPw, String redirectUri) {
        Member loginedMember = ((Rq) req.getAttribute("rq")).getLoginedMember();

        if (loginedMember.getLoginPw().equals(loginPw) == false) {
            return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다.");
        }

        String authCode = memberService.samCheckPasswordAuthCode(loginedMember.getId());

        redirectUri = Util.getNewUri(redirectUri, "checkPasswordAuthCode", authCode);

        return Util.msgAndReplace(req, "", redirectUri);
    }
    
//----------------------------------------------------------------------------------------------------  
    // 회원수정 시 로그인된 멤버가 누구인지 정보를 받아서 회원수정 페이지로 넘겨주는 역할
    // checkPasswordAuthCode : 체크비밀번호인증코드
    @RequestMapping("/usr/member/modify")
    public String showModify(HttpServletRequest req,  String checkPasswordAuthCode) {	
        Member loginedMember = ((Rq) req.getAttribute("rq")).getLoginedMember();
        // 이 회원이 방금 비밀번호 체크해서 인증코드를 발급받았고 그 인증코드가 유효한지 체크
        ResultData rd = memberService.checkValidCheckPasswordAuthCode(loginedMember.getId(), checkPasswordAuthCode);

        if ( rd.isFail() ) {
            return Util.msgAndBack(req, rd.getMsg());
        }

        return "usr/member/modify";
    }

    
    // 회원수정하기
    @RequestMapping("/usr/member/doModify")
    public String doModify(HttpServletRequest req, String loginPw, String name, String nickname, 
    										String cellphoneNo, String email, String checkPasswordAuthCode, MultipartRequest multipartRequest) {

        Member loginedMember = ((Rq) req.getAttribute("rq")).getLoginedMember();
        // authcode로 인증
        ResultData rd = memberService.checkValidCheckPasswordAuthCode(loginedMember.getId(), checkPasswordAuthCode);

        if ( rd.isFail() ) {
            return Util.msgAndBack(req, rd.getMsg());
        }

        if (loginPw != null && loginPw.trim().length() == 0) {
            loginPw = null;
        }
        // 인증 다 했으면 이제 회원정보 수정 해서 수정된 내용 객체에 담기
        int id = ((Rq) req.getAttribute("rq")).getLoginedMemberId();
        ResultData modifyRd = memberService.modify(id, loginPw, name, nickname, cellphoneNo, email);

        if (modifyRd.isFail()) {
            return Util.msgAndBack(req, modifyRd.getMsg());
        }

        if ( req.getParameter("deleteFile__member__0__extra__profileImg__1") != null ) {
            samFileService.deleteSamFile("member", loginedMember.getId(), "extra", "profileImg", 1);
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        for (String fileInputName : fileMap.keySet()) {
            MultipartFile multipartFile = fileMap.get(fileInputName);

            if ( multipartFile.isEmpty() == false ) {
                samFileService.save(multipartFile, loginedMember.getId());
            }
        }

        return Util.msgAndReplace(req, modifyRd.getMsg(), "/");
    }

}

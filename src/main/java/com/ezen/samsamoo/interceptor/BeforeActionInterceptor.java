package com.ezen.samsamoo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ezen.samsamoo.dto.Member;
import com.ezen.samsamoo.dto.Rq;
import com.ezen.samsamoo.service.MemberService;
import com.ezen.samsamoo.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/* 인터셉터는 웹 MVC 동작과정 중에서 할 수 있다. 예를 들어 컨트롤러를 실행하기 전과 같이
 * 특정 시점에서 원하는 기능을 실행 시킬 수 있다.
 */

/* <beforeActionInterceptor의 기능>
 * 세션에서 loginedMemberId 값이 있는지 검사한다.
 * 있다면, 꺼내서 관련 회원을 부른다.
 * 그 후 req에 회원정보를 넣는다.
 * 관리자 관련 항목으로 일반 사용자 접속을 걸러준다.
 * 요약하자면 세션 정보를 꺼내서 기존에 있는 정보를 정리해두는 정보강화
 */

// Spring(IOC) container에 Bean을 등록하도록 하는 메타데이터를 기입하는 어노테이션
// @Bean과 다른 점은 개발자가 직접 작성한 Class를 Bean으로 등록하기 위한 어노테이션이라는 점
@Component("beforeActionInterceptor") // 컴포넌트 이름 설정
public class BeforeActionInterceptor implements HandlerInterceptor {
	
    @Autowired
    private MemberService memberService;

  
    // ajax 구분하기 위한 메서드 
    // ajax 요청시 헤더에 값을 추가 해주어야 하고, 넘어온 400이라는 status에 대한 처리도 추가해주어야 하기 때문
   // request.getAttribute("isAjax") 이것만으로 해당 요청이 ajax인지 구분 가능
    //
    private boolean isAjax(HttpServletRequest req) {
        String[] pathBits = req.getRequestURI().split("/");

        String controllerTypeCode = "";
        String controllerSubject = "";
        String controllerActName = "";

        if (pathBits.length > 1) {
            controllerTypeCode = pathBits[1];
        }

        if (pathBits.length > 2) {
            controllerSubject = pathBits[2];
        }

        if (pathBits.length > 3) {
            controllerActName = pathBits[3];
        }

        boolean isAjax = false;

        String isAjaxParameter = req.getParameter("isAjax");

        if ( isAjax == false ) {
            if ( controllerActName.startsWith("get") ) {
                isAjax = true;
            }
        }

        if ( isAjax == false ) {
            if (controllerActName.endsWith("Ajax")) {
                isAjax = true;
            }
        }

        if ( isAjax == false ) {
            if (isAjaxParameter != null && isAjaxParameter.equals("Y")) {
                isAjax = true;
            }
        }

        return isAjax;
    }

    
    // 어떤 url이 들어오든 요청이 들어올 때 마다 이 녀석이 실행됨. 
    // preHandle() -> 컨트롤러를 실행하기 전에 실행되는 메서드로 페이지 이동 혹은 Ajax를 호출하여 데이터를 처리하기 전
    // 해당 사용자의 세션체크를 하는데 용이하다.
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Map<String, String> paramMap = Util.getParamMap(req);

        // 세션 객체 등장! 
        HttpSession session = req.getSession();

        Member loginedMember = null;
        int loginedMemberId = 0;

        // 로그인되어있으면 세션에 로그인 멤버아이디 넣어준다
        if (session.getAttribute("loginedMemberId") != null) {
            loginedMemberId = (int) session.getAttribute("loginedMemberId");
        }
        // 로그인되어있으면 세션에 로그인 멤버 정보를 넣어준다(제이슨문자열을 스트링으로 변환)
        if (loginedMemberId != 0) {
            String loginedMemberJsonStr = (String) session.getAttribute("loginedMemberJsonStr");

            loginedMember = Util.fromJsonStr(loginedMemberJsonStr, Member.class);
        }   
        // 현재 uri		 ex) http://localhost:8021/usr/home/main?
        String currentUri = req.getRequestURI();
        
        // 현재 쿼리스트링		ex) age=11
        String queryString = req.getQueryString();
       
        // 현재 uri + ? + 쿼리스트링
        if (queryString != null && queryString.length() > 0) {
            currentUri += "?" + queryString;
        }

        boolean needToChangePassword = false;

        // 현재 비밀번호 변경이 필요한지 여부를 넣어준다.
        if (loginedMember != null) {
            if (session.getAttribute("needToChangePassword") == null) {
                needToChangePassword = memberService.needToChangePassword(loginedMember.getId());

                session.setAttribute("needToChangePassword", needToChangePassword);
            }

            needToChangePassword = (boolean) session.getAttribute("needToChangePassword");
        }

        // 위에서 rq에 담은 정보들을 넣어준다.
        req.setAttribute("rq", new Rq(isAjax(req), memberService.isAdmin(loginedMember), loginedMember, currentUri, paramMap, needToChangePassword));

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
    
    
}
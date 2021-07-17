package com.ezen.samsamoo.interceptor;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ezen.samsamoo.dto.Rq;
import com.ezen.samsamoo.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//로그아웃 되어있는지 체크하는 인터셉터
@Component
public class NeedToLogoutInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Rq rq = (Rq) req.getAttribute("rq");

        //로그인 되어있다면 false를 리턴
        if (rq.isLogined()) {
            String resultCode = "F-B";
            String resultMsg = "로그아웃 후 이용해주세요.";

            if ( rq.isAjax() ) {
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().append("{\"resultCode\":\"" + resultCode + "\",\"msg\":\"" + resultMsg + "\"}");
            }
            else {
                resp.setContentType("text/html; charset=UTF-8");
                resp.getWriter().append(Util.msgAndBack(resultMsg));
            }
            return false;
        }

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
}

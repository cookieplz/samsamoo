package com.ezen.samsamoo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ezen.samsamoo.dto.Rq;
import com.ezen.samsamoo.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// 관리자인지 체크하는 인터셉터
@Component
public class NeedAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        Rq rq = (Rq) req.getAttribute("rq");
        
        // 관리자가 아니면 false를 리턴
        if (rq.isNotAdmin()) {
            String resultCode = "F-C";
            String resultMsg = "관리자만 이용할 수 있습니다.";

            if ( rq.isAjax() ) {
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().append("{\"resultCode\":\"" + resultCode + "\",\"msg\":\"" + resultMsg + "\"}");
            }
            else {
                resp.setContentType("text/html; charset=UTF-8");
                String afterLoginUri = rq.getEncodedCurrentUri();
                resp.getWriter().append(Util.msgAndReplace(resultMsg, "../member/login?afterLoginUri=" + afterLoginUri));
            }

            return false;
        }

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
}

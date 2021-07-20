package com.ezen.samsamoo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ezen.samsamoo.interceptor.BeforeActionInterceptor;
import com.ezen.samsamoo.interceptor.NeedAdminInterceptor;
import com.ezen.samsamoo.interceptor.NeedToLoginInterceptor;
import com.ezen.samsamoo.interceptor.NeedToLogoutInterceptor;

// WebMvcConfig에다가 설정을 하면은 여기에 있는 설정을 스프링 부트가 읽는다.
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
    BeforeActionInterceptor beforeActionInterceptor;

    @Autowired
    NeedToLoginInterceptor needToLoginInterceptor;

    @Autowired
    NeedAdminInterceptor needAdminInterceptor;

    @Autowired
    NeedToLogoutInterceptor needToLogoutInterceptor;


	    @Value("${custom.samFileDirPath}")	
		private String samFileDirPath;
	    
		//CORS 허용
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			 registry.addMapping("/**");
		}
	    
    // 인터셉터를 적용하는 역할을 담당
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // beforeActionInterceptor -> 모든 액션 실행전에 실행되도록 처리
        registry.addInterceptor(beforeActionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/resource/**")
                .excludePathPatterns("/sam/**")
                .excludePathPatterns("/error");

        // 로그인이 필요한 경우 
        registry.addInterceptor(needToLoginInterceptor)
                .addPathPatterns("/usr/article/write")
                .addPathPatterns("/usr/article/doWrite")
                .addPathPatterns("/usr/article/doDelete")
                .addPathPatterns("/usr/article/modify")
                .addPathPatterns("/usr/article/doModify")
                .addPathPatterns("/usr/reply/doWrite")
                .addPathPatterns("/usr/reply/doDelete")
                .addPathPatterns("/usr/reply/doDeleteAjax")
                .addPathPatterns("/usr/reply/modify")
                .addPathPatterns("/usr/reply/doModify")
                .addPathPatterns("/usr/member/modify")
                .addPathPatterns("/usr/member/doModify")
                .addPathPatterns("/usr/member/checkPassword")
                .addPathPatterns("/usr/member/doCheckPassword")
                .excludePathPatterns("/resource/**")
                .excludePathPatterns("/sam/**");

        // 로그아웃이 필요한 경우
        registry.addInterceptor(needToLogoutInterceptor)
                .addPathPatterns("/usr/member/findLoginId")
                .addPathPatterns("/usr/member/doFindLoginId")
                .addPathPatterns("/usr/member/findLoginPw")
                .addPathPatterns("/usr/member/doFindLoginPw")
                .addPathPatterns("/usr/member/login")
                .addPathPatterns("/usr/member/doLogin")
                .addPathPatterns("/usr/member/getLoginIdDup")
                .addPathPatterns("/usr/member/join")
                .addPathPatterns("/usr/member/doJoin")
                .addPathPatterns("/usr/member/findLoginId")
                .addPathPatterns("/usr/member/doFindLoginId")
                .addPathPatterns("/usr/member/findLoginPw")
                .addPathPatterns("/usr/member/doFindLoginPw")
                .excludePathPatterns("/resource/**")
                .excludePathPatterns("/sam/**");

        // 어드민 필요
        registry.addInterceptor(needAdminInterceptor)
                .addPathPatterns("/adm/**")
                .excludePathPatterns("/adm/member/findLoginId")
                .excludePathPatterns("/adm/member/doFindLoginId")
                .excludePathPatterns("/adm/member/findLoginPw")
                .excludePathPatterns("/adm/member/doFindLoginPw")
                .excludePathPatterns("/adm/member/login")
                .excludePathPatterns("/adm/member/doLogin")
                .excludePathPatterns("/adm/member/getLoginIdDup")
                .excludePathPatterns("/adm/member/join")
                .excludePathPatterns("/adm/member/doJoin")
                .excludePathPatterns("/adm/member/findLoginId")
                .excludePathPatterns("/adm/member/doFindLoginId")
                .excludePathPatterns("/adm/member/findLoginPw")
                .excludePathPatterns("/adm/member/doFindLoginPw")
                .excludePathPatterns("/resource/**")
                .excludePathPatterns("/sam/**");

        registry.addInterceptor(needToLogoutInterceptor)
                .addPathPatterns("/adm/member/findLoginId")
                .addPathPatterns("/adm/member/doFindLoginId")
                .addPathPatterns("/adm/member/findLoginPw")
                .addPathPatterns("/adm/member/doFindLoginPw")
                .addPathPatterns("/adm/member/login")
                .addPathPatterns("/adm/member/doLogin")
                .addPathPatterns("/adm/member/getLoginIdDup")
                .addPathPatterns("/adm/member/join")
                .addPathPatterns("/adm/member/doJoin")
                .addPathPatterns("/adm/member/findLoginId")
                .addPathPatterns("/adm/member/doFindLoginId")
                .addPathPatterns("/adm/member/findLoginPw")
                .addPathPatterns("/adm/member/doFindLoginPw")
                .excludePathPatterns("/resource/**")
                .excludePathPatterns("/sam/**");
    }

    
    // 정적 리소스 접근하기 위한 경로 설정
    // ResourceHandlerRegistry -> 리소스 등록 및 핸들러를 관리하는 객체
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/sam/**")	// 리소스와 매칭될 url 등록
        			  .addResourceLocations("file:///" + samFileDirPath + "/")	// 리소스의 위치 등록
        			  .setCachePeriod(20);	// 캐시가 얼마나 지속할지 정하는 메서드 (20초로 설정)
    }
    
    
}

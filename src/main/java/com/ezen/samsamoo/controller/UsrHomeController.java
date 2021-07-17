package com.ezen.samsamoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsrHomeController {
	// 메인페이지로 리다이렉트
	@RequestMapping("/")
	public String showMainRoot() {
		return "redirect:/usr/home/main";
	}
	
	
	// 메인페이지 uri
	@RequestMapping("/usr/home/main")
	public String showMain() {
		return "usr/home/main";
	}
}
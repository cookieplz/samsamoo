package com.ezen.samsamoo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdmHomeController {
	
	
	// 메인페이지로 리다이렉트
	@RequestMapping("/adm")
	public String showMainRoot() {
		return "redirect:/adm/home/main";
	}
	
	
	// 메인페이지 uri
	@RequestMapping("/adm/home/main")
	public String showMain() {
		return "adm/home/main";
	}
}
package com.cn.zm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author chenjl
 *
 */
@Controller
@RequestMapping("/")
@Slf4j
public class IndexController {
	
	@RequestMapping("/index")
	public String index() {
		log.info("-------------index------------");
		return "index";
	}

	@RequestMapping("/home")
	public String toHome() {
		log.info("===111-------------home------------");
		return "home";
	}

	@RequestMapping("/login")
	public String toLogin() {
		log.info("===111-------------login------------");
		return "login";
	}

	@RequestMapping("/{page}")
	public String toPage(@PathVariable("page") String page) {
		log.info("-------------toindex------------" + page);
		return page;
	}
}

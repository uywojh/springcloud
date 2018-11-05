package com.github.liuweijw.system.auth.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping("/")
	public String index(Authentication authentication) throws IOException {
		return "登录成功";
	}

	/**
	 * 获取用户信息
	 * 登录成功会返回：http://localhost:1004/user/info 此时成功加载受保护资源
	 */
	@RequestMapping("/user/info")
	public Object user(Authentication authentication) throws IOException {
		return authentication.getPrincipal();
	}

}

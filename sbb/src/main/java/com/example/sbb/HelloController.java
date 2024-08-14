package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller //컨트롤러의 역할 <- 정보 목적 어노테이션 
public class HelloController {
	@GetMapping("/hello") // 로컬 호스트에게 hello 메서드 요
	@ResponseBody
	public String Hello() {
		return "Hello SeungMin!";
	}

}

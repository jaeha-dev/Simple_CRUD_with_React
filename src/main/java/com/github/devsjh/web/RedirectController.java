package com.github.devsjh.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @title  : Redirect 컨트롤러 클래스
 * @author : jaeha-dev (Git)
 */
@Controller
public class RedirectController {

    @GetMapping("/private")
    public String redirectToRoot() {
        return "redirect:/";
    }
}
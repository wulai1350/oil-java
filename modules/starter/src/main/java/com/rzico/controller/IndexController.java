package com.rzico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  静态页入口地址
 * Created by macro on 2018/4/26.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping("")
    public String index() {
        return "forward:/index.html";
    }
//
//    @GetMapping("index.html")
//    public String redirect() {
//        return "redirect:/";
//    }

}

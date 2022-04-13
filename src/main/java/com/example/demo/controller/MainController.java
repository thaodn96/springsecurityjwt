package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/api/auth/")
    @ResponseBody
    public String welcome(){
        return "Welcome to main";
    }

    @RequestMapping("/api/auth/home")
    public String home(){
        return "index";
    }

    @RequestMapping("/api/test")
    public String test(){
        return "abc";
    }
}

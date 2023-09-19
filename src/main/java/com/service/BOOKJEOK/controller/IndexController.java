package com.service.BJBJ.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private Environment env;

    @GetMapping
    public String index(){
        return "ppppp";
    }

    @GetMapping("/health")
    public String health(){
        return "health";
    }

    @GetMapping("/test")
    public String test() { return "test"; }
}

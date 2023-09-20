package com.service.BOOKJEOK.controller;

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
        return "index";
    }

    @GetMapping("/info")
    public String info(){
        return env.getProperty("spring.datasource.url");
    }

    @GetMapping("/health")
    public String health(){
        return "health!";
    }

}

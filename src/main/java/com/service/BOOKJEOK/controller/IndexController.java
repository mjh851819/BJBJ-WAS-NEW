package com.service.BOOKJEOK.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

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

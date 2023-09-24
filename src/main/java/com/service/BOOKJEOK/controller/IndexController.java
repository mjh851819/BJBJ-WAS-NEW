package com.service.BOOKJEOK.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping
    public String index(){
        return "index";
    }


    @GetMapping("/tokenTest")
    public String tokenTest(@RequestParam String access_token, @RequestParam String refresh_token) {
        return access_token;
    }

    @GetMapping("/auth")
    public String auth(){
        return "Ok";
    }

}

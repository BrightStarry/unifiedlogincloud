package com.zuma.controller;

import com.alibaba.fastjson.JSONObject;
import com.zuma.dto.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * author:Administrator
 * datetime:2017/10/26 0026 16:42
 */
@RestController("/")
public class TestController {


    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String test(){
        String path = "http://unifiedLogin/verify";

//        LoginForm loginForm = LoginForm.builder()
//                .channel(1)
//                .id(1000L)
//                .username("aaaa")
//                .password("6fcda8a6cc39ec0904fc35103ec60933")
//                .build();

        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("channel", "1");
        requestEntity.add("id", "1000");
        requestEntity.add("username", "aaaa");
        requestEntity.add("password", "6fcda8a6cc39ec0904fc35103ec60933");



        ResponseEntity<String> response = restTemplate.postForEntity(path, requestEntity, String.class);

        return response.getBody();
    }
}

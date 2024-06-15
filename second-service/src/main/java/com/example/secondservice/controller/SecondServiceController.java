package com.example.secondservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/second-service")
public class SecondServiceController {

    private final Environment env;

    @GetMapping("/hello")
    public String hello() {
        return "hello second-service!!";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header) {
        log.info("request-header = {}", header);
        return "Hello world in second service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port = {}", request.getServerPort());
        return "Hi, there! This is a message from second service on port = " + env.getProperty("local.server.port");
    }
}
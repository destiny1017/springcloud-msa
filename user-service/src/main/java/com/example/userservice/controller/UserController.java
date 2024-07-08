package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final Environment env;
    private final Greeting greeting;
    private final UserService userService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in User Service"
                + ",\n port(local.server.port)=" + env.getProperty("local.server.port")
                + ",\n port(server.port)=" + env.getProperty("server.port")
                + ",\n gateway ip(env)=" + env.getProperty("gateway.ip")
                + ",\n message=" + env.getProperty("greeting.message")
                + ",\n token expiration time=" + env.getProperty("token.expiration_time")
                + ",\n token expiration time=" + env.getProperty("token.secret"));
    }


    @GetMapping("/welcome")
    public String greeting() {
        return greeting.getMessage();
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
        ResponseUser resUser = userService.createUser(UserDto.of(user));
        return ResponseEntity.status(HttpStatus.OK).body(resUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> users() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserByAll()
                        .stream().map(UserDto::toResponse)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> user(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserDto.toResponse(userService.getUserByUserId(userId)));
    }

}

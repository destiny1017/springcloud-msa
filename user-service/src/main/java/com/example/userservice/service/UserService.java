package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.ResponseUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    ResponseUser createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    List<UserDto> getUserByAll();
    UserDto getUserDetailsByEmail(String email);
}

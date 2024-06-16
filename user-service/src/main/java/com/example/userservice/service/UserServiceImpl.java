package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public ResponseUser createUser(UserDto userDto) {
        userDto.setPwd("encrypted pwd" + userDto.getPwd());
        userDto.setUserId(UUID.randomUUID().toString());
        userRepository.save(UserDto.toEntity(userDto));
        return UserDto.toResponse(userDto);
    }
}

package com.example.userservice.dto;

import com.example.userservice.jpa.UserEntity;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    public static UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .userId(dto.getUserId())
                .encryptedPwd(dto.getPwd())
                .build();
    }

    public static ResponseUser toResponse(UserDto dto) {
        return new ResponseUser(dto.getEmail(), dto.getName(), dto.getUserId());
    }

    public static UserDto of(RequestUser user) {
        return new UserDto(user.getEmail(), user.getName(), user.getPwd(), null, null);
    }
}

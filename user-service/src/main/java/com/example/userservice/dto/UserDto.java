package com.example.userservice.dto;

import com.example.userservice.jpa.UserEntity;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String decryptedPwd;

    private String encryptedPwd;

    private List<ResponseOrder> orders = new ArrayList<>();

    public static UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .userId(dto.getUserId())
                .encryptedPwd(dto.getPwd())
                .build();
    }

    public static ResponseUser toResponse(UserDto dto) {
//        return new ResponseUser(dto.getEmail(), dto.getName(), dto.getUserId(), dto.getOrders());
        return ResponseUser.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .userId(dto.getUserId())
                .orders(dto.getOrders())
                .build();
    }

    public static UserDto of(RequestUser user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .pwd(user.getPwd())
                .build();
    }

    public static UserDto of(UserEntity user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .userId(user.getUserId())
                .pwd(user.getEncryptedPwd())
                .encryptedPwd(user.getEncryptedPwd())
                .build();
    }
}

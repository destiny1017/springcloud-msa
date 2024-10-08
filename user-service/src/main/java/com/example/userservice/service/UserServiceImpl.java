package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final ErrorDecoder feignErrorDecoder;
    private final Environment env;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public ResponseUser createUser(UserDto userDto) {
        userDto.setPwd(passwordEncoder.encode(userDto.getPwd()));
        userDto.setUserId(UUID.randomUUID().toString());
        userRepository.save(UserDto.toEntity(userDto));
        return UserDto.toResponse(userDto);
    }

    public UserDto getUserByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDto userDto = UserDto.of(user);
        String orderUrl = String.format(env.getProperty("order-service.url"), userId);

//        // RestTemplate 사용
//        ResponseEntity<List<ResponseOrder>> orders = restTemplate.exchange(
//                orderUrl, HttpMethod.GET, null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {});
//        userDto.setOrders(orders.getBody());

        // Feign Client 사용
//        List<ResponseOrder> orders = null;
//        try {
//            orders = orderServiceClient.getOrders(userId);
//        } catch (FeignException e) {
//            log.error(e.getMessage());
//        }

        // Error decoder 적용
//        List<ResponseOrder> orders = orderServiceClient.getOrders(userId);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");
        List<ResponseOrder> orders = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        userDto.setOrders(orders);

        return userDto;
    }

    public List<UserDto> getUserByAll() {
        return userRepository.findAll()
                .stream().map(UserDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(user.getEmail(),
                        user.getEncryptedPwd(),
                        true, true, true, true,
                        new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        return UserDto.of(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
}

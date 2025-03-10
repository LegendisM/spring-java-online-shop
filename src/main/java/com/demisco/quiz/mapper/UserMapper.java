package com.demisco.quiz.mapper;

import com.demisco.quiz.dto.user.UserDto;
import com.demisco.quiz.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }


}

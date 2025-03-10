package com.demisco.quiz.service;

import com.demisco.quiz.dto.auth.RegisterRequestDto;
import com.demisco.quiz.dto.user.UserDto;
import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.entity.UserEntity_;
import com.demisco.quiz.exception.ResponseException;
import com.demisco.quiz.mapper.UserMapper;
import com.demisco.quiz.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserEntityRepository userEntityRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Make new user for the registration process
     * @param requestDto
     * @return UserEntity
     */
    @Transactional
    public UserEntity createUser(RegisterRequestDto requestDto) {
        var user = this.saveUser(
                UserEntity.builder()
                        .username(requestDto.username)
                        .email(requestDto.email)
                        .password(bCryptPasswordEncoder.encode(requestDto.password))
                        .build()
        );
        logger.info("New user created with id: {}", user.getId());
        return user;
    }

    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        return userEntityRepository.findOne((r, q, b) -> b.equal(r.get(UserEntity_.USERNAME), username))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public UserEntity findById(Long id) throws ResponseException {
        return userEntityRepository.findById(id).orElseThrow(() -> new ResponseException("user-not-found"));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) throws ResponseException {
        var user = this.findById(id);
        return userMapper.toDto(user);
    }

    @Transactional
    public UserEntity saveUser(UserEntity userEntity) {
        return userEntityRepository.save(userEntity);
    }

}

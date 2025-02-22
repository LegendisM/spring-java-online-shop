package com.demisco.quiz.service;

import com.demisco.quiz.entity.UserEntity;
import com.demisco.quiz.entity.UserEntity_;
import com.demisco.quiz.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    @Transactional
    public UserEntity createUser() {
        return this.saveUser(UserEntity.builder().build());
    }

    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        return userEntityRepository.findOne((r, q, b) -> b.equal(r.get(UserEntity_.USERNAME), username))
                .orElse(null);
    }

    @Transactional
    public UserEntity saveUser(UserEntity userEntity) {
        return userEntityRepository.save(userEntity);
    }

}

package com.appsdeveloperblog.app.ws.repository;

import com.appsdeveloperblog.app.ws.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);
}

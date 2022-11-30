package com.mobileappws.developerBlog.repository;

import com.mobileappws.developerBlog.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity,Long> {
    PasswordResetTokenEntity findByToken(String token);
}

package com.mobileappws.developerBlog.service;

import com.mobileappws.developerBlog.shared.dto.UserDto;

import javax.mail.MessagingException;


public interface UserService {
    UserDto createUser(UserDto user) throws MessagingException;
    UserDto getUser(String email);

    UserDto getUserByUserId(String id);

    UserDto updateUser(String id, UserDto userDto);
    void deleteUser(String id);

    boolean verifyEmail(String token);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);
}

package com.mobileappws.developerBlog.service.impl;

import com.mobileappws.developerBlog.constants.AppConstants;
import com.mobileappws.developerBlog.entity.PasswordResetTokenEntity;
import com.mobileappws.developerBlog.entity.UserEntity;
import com.mobileappws.developerBlog.exceptions.UserServiceException;
import com.mobileappws.developerBlog.model.response.ErrorMessages;
import com.mobileappws.developerBlog.repository.PasswordResetTokenRepository;
import com.mobileappws.developerBlog.repository.UserRepository;
import com.mobileappws.developerBlog.service.AddressService;
import com.mobileappws.developerBlog.service.EmailSenderService;
import com.mobileappws.developerBlog.service.UserService;
import com.mobileappws.developerBlog.shared.dto.AddressDTO;
import com.mobileappws.developerBlog.shared.dto.UserDto;
import com.mobileappws.developerBlog.shared.utils.AmazonSES;
import com.mobileappws.developerBlog.shared.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private AddressService addressService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserDto createUser(UserDto user) throws MessagingException {
        if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");
       for(int i=0;i<user.getAddresses().size();i++){
           AddressDTO addressDTO = user.getAddresses().get(i);
           addressDTO.setUser(user);
           addressDTO.setAddressId(utils.generateAddressId(20));
           user.getAddresses().set(i,addressDTO);
       }
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEmailVerificationStatus(Boolean.FALSE);
        userEntity.setEmailVerificationToken(utils.generateVerificationToken(publicUserId));
        UserEntity storedUserDetails =userRepository.save(userEntity);
        UserDto returnedValue = modelMapper.map(storedUserDetails,UserDto.class);
        // amazon account needed
       // new AmazonSES().verifyEmail(returnedValue);
        String mailBody = AppConstants.HTMLBODY.replace("$tokenValue",returnedValue.getEmailVerificationToken());
        emailSenderService.sendSimpleEmail(returnedValue.getEmail(),mailBody,AppConstants.SUBJECT);
        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity==null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String id, UserDto user) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if(userEntity==null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        UserEntity updatedUserDetails = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(updatedUserDetails,returnValue);
        return returnValue;
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if(userEntity==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userRepository.delete(userEntity);
    }

    @Override
    public boolean verifyEmail(String token) {
        boolean hasExpired = Utils.hasTokenExpired(token);
        if(!hasExpired){
            UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);
            if(userEntity==null) return false;
            userEntity.setEmailVerificationToken(null);
            userEntity.setEmailVerificationStatus(Boolean.TRUE);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean requestPasswordReset(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity==null) return false;
        String token = Utils.generatePasswordResetToken(userEntity.getUserId());
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserEntity(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
        return emailSenderService.sendPasswordResetRequest(userEntity.getFirstName(),userEntity.getEmail(),token);
    }
    @Override
    public boolean resetPassword(String token, String password) {
        if(Utils.hasTokenExpired(token)) return false;
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);
        if(passwordResetTokenEntity==null) return false;
        UserEntity userEntity = passwordResetTokenEntity.getUserEntity();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity updatedUserEntity =userRepository.save(userEntity);
        boolean returnValue = false;
        if(updatedUserEntity!=null && updatedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)){
            returnValue = true;
        }
        passwordResetTokenRepository.delete(passwordResetTokenEntity);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity==null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),
                userEntity.getEmailVerificationStatus(),true,true,true,new ArrayList<>());
    }
}

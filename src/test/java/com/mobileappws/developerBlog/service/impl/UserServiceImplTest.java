package com.mobileappws.developerBlog.service.impl;

import com.mobileappws.developerBlog.entity.UserEntity;
import com.mobileappws.developerBlog.repository.PasswordResetTokenRepository;
import com.mobileappws.developerBlog.repository.UserRepository;
import com.mobileappws.developerBlog.service.AddressService;
import com.mobileappws.developerBlog.service.EmailSenderService;
import com.mobileappws.developerBlog.shared.dto.AddressDTO;
import com.mobileappws.developerBlog.shared.dto.UserDto;
import com.mobileappws.developerBlog.shared.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Utils utils;
    @Mock
    private AddressService addressService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    UserEntity userEntity;
    String userId = "i7slirls";
    String encodedPassword="eylitxhsz7";
    String emailVerificationToken = "eyzxPToqrhg";
    String addressId = "eyzxPToqr";
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = UserEntity.builder()
                .id(1L)
                .userId(userId)
                .email("test@test.com")
                .firstName("Raihan")
                .encryptedPassword(encodedPassword)
                .emailVerificationToken(emailVerificationToken)
                .build();

    }
    @Test
    void getUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = userService.getUser("test@test.com");
        assertNotNull(userDto);
        assertEquals(userEntity.getFirstName(),userDto.getFirstName());
    }
    @Test
    void getUser_UsernameNotFoundException(){
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,()->{
            userService.getUser("test@test.com");
        });
    }

    @Test
    void test_createUser() throws MessagingException {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(utils.generateAddressId(anyInt())).thenReturn(addressId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(Utils.generateVerificationToken(anyString())).thenReturn(emailVerificationToken);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        doNothing().when(emailSenderService).sendSimpleEmail(anyString(),anyString(),anyString());

        UserDto newUser = getUserDto();
        UserDto userDto = userService.createUser(newUser);
        assertNotNull(userDto);
        assertEquals(userDto.getFirstName(),newUser.getFirstName());
        assertEquals(userDto.getLastName(),newUser.getLastName());

    }

    private UserDto getUserDto() {
       return UserDto.builder()
                .firstName("Raihan")
                .lastName("Uddin")
                .addresses(getAddressesDto())
                .build();
    }

    private List<AddressDTO> getAddressesDto() {
        List<AddressDTO> list = new ArrayList<>();
        AddressDTO shipping = AddressDTO.builder()
                .city("Dhaka")
                .country("Bangladesh")
                .type("shipping")
                .streetName("123 streatname")
                .postalCode("1111")
                .build();
        list.add(shipping);
        AddressDTO billing = AddressDTO.builder()
                .city("Dhaka")
                .country("Bangladesh")
                .type("shipping")
                .streetName("123 streatname")
                .postalCode("1111")
                .build();
        list.add(billing);
        return list;
    }


}
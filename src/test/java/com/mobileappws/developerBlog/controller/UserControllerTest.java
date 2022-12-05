package com.mobileappws.developerBlog.controller;

import com.mobileappws.developerBlog.entity.AddressEntity;
import com.mobileappws.developerBlog.entity.UserEntity;
import com.mobileappws.developerBlog.model.response.UserRest;
import com.mobileappws.developerBlog.service.impl.UserServiceImpl;
import com.mobileappws.developerBlog.shared.dto.AddressDTO;
import com.mobileappws.developerBlog.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @InjectMocks
    UserController userController;
    @Mock
    UserServiceImpl userService;
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
                .lastName("Uddin")
                .encryptedPassword(encodedPassword)
                .emailVerificationToken(emailVerificationToken)
                .addresses(getAddressesEntity())
                .build();
    }

    @Test
    void getUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(getUserDto());
        UserRest userRest = userController.getUser(userId).getBody();
        assertNotNull(userRest);
        assertEquals(userRest.getUserId(),userId);
    }
    private List<AddressEntity> getAddressesEntity() {
        List<AddressDTO> addressDTOList = getAddressesDto();
        Type listType = new TypeToken<List<AddressEntity>>(){}.getType();
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDTOList,listType);
    }
    private UserDto getUserDto() {
        return UserDto.builder()
                .userId(userId)
                .firstName("Raihan")
                .lastName("Uddin")
                .password("raihan")
                .email("test@test.com")
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
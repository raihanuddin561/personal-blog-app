package com.appsdeveloperblog.app.ws.controller;


import com.appsdeveloperblog.app.ws.enums.RequestOperationName;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.model.response.*;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @GetMapping(path = "/{id}",
    produces = {

            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public ResponseEntity<UserRest> getUser(@PathVariable String id){
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto,returnValue);
        return new ResponseEntity<>(returnValue,HttpStatus.OK);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserRest> createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto =modelMapper.map(userDetails,UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser,UserRest.class);
        return new ResponseEntity<>(returnValue,HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<UserRest> updateUser(@PathVariable String id,
                                               @RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserRest returnValue = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);
        UserDto updateddUser = userService.updateUser(id,userDto);
        BeanUtils.copyProperties(updateddUser,returnValue);
        return new ResponseEntity<>(returnValue,HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id)
    {
        OperationStatusModel  returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }
    @GetMapping(path = "/{id}/addresses",
            produces = {

                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            })
    public List<AddressRest> getUserAddresses(@PathVariable String id){
        List<AddressRest> addressRests = new ArrayList<>();
        List<AddressDTO> addressDTOS = addressService.getAddresses(id);
        ModelMapper modelMapper = new ModelMapper();
        if(addressDTOS!=null){
            Type listType = new TypeToken<List<AddressRest>>(){}.getType();
            addressRests = modelMapper.map(addressDTOS,listType);
        }
        return addressRests;
    }
}

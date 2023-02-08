package com.mobileappws.developerBlog.controller;


import com.mobileappws.developerBlog.constants.AppConstants;
import com.mobileappws.developerBlog.enums.RequestOperationName;
import com.mobileappws.developerBlog.exceptions.UserServiceException;
import com.mobileappws.developerBlog.model.request.PasswordResetModel;
import com.mobileappws.developerBlog.model.request.PasswordResetRequestModel;
import com.mobileappws.developerBlog.model.request.UserDetailsRequestModel;
import com.mobileappws.developerBlog.model.response.*;
import com.mobileappws.developerBlog.service.AddressService;
import com.mobileappws.developerBlog.service.UserService;
import com.mobileappws.developerBlog.shared.dto.AddressDTO;
import com.mobileappws.developerBlog.shared.dto.UserDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @GetMapping("/hello")
    public String helloUser(){
        return "Hello mobile ap ws";
    }
    @ApiOperation(
            value = "Get user by user id",
            notes = "This service will provide specific user according to user id and URL path ex: /users/x7yzdit5l"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "BEARER JWT TOKEN",paramType = "header")
    })
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
    @ApiOperation(
            value = "Update User information by specific user id",
            notes = "This service will update user information for specific user by user id, request body should contain all the update information. ex url: /users/x7yzdit5l " +
                    "request body: {UserDetailsRequestModel}"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "BEARER JWT TOKEN",paramType = "header")
    })
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
    @ApiOperation(
            value = "Delete User information by specific user id",
            notes = "This service will delete user information for specific user by user id, request body should contain all the update information. ex url: /users/x7yzdit5l "
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "BEARER JWT TOKEN",paramType = "header")
    })
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id)
    {
        OperationStatusModel  returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }
    @ApiOperation(
            value = "Get Address information by specific Address id",
            notes = "This service will provide address information address id"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization",value = "BEARER JWT TOKEN",paramType = "header")
    })
    @GetMapping(path = "/{id}/addresses",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    AppConstants.APPLICATION_HAL_JSON
            })
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable String id){
        List<AddressRest> addressRests = new ArrayList<>();
        List<AddressDTO> addressDTOS = addressService.getAddresses(id);
        ModelMapper modelMapper = new ModelMapper();
        if(addressDTOS!=null){
            Type listType = new TypeToken<List<AddressRest>>(){}.getType();
            addressRests = modelMapper.map(addressDTOS,listType);
        }
        for(AddressRest addressRest:addressRests){
            Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(id)).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
            addressRest.add(addressesLink,userLink);
        }
        return CollectionModel.of(addressRests);
    }
    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    AppConstants.APPLICATION_HAL_JSON
            })
    public EntityModel<AddressRest> getAddress(@PathVariable String userId, @PathVariable String addressId){
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        ModelMapper modelMapper = new ModelMapper();
        AddressRest addressRest = modelMapper.map(addressDTO,AddressRest.class);
        Link link = linkTo(methodOn(UserController.class).getAddress(userId,addressId)).withSelfRel();
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
        addressRest.add(link,addressesLink,userLink);
        return EntityModel.of(addressRest);
    }
    @GetMapping("/email-verification")
    public OperationStatusModel verifyEmail(@RequestParam("token") String token){
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        boolean isVerified = userService.verifyEmail(token);
        operationStatusModel.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
        if(isVerified) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else {
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return operationStatusModel;
    }
    @PostMapping("/password-reset-request")
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel){
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
        operationStatusModel.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        if(operationResult) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else {
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return operationStatusModel;
    }
    @PostMapping("/password-reset")
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel){
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        boolean operationResult = userService.resetPassword(passwordResetModel.getToken(),passwordResetModel.getPassword());
        operationStatusModel.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());
        if(operationResult) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }
        return operationStatusModel;
    }
}

package com.appsdeveloperblog.app.ws.controller;


import com.appsdeveloperblog.app.ws.constants.AppConstants;
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
}

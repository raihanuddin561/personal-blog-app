package com.appsdeveloperblog.app.ws.controller;


import com.appsdeveloperblog.app.ws.enums.RequestOperationName;
import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.model.response.UserRest;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;
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
        if(userDetails.getFirstName()==null
                ||userDetails.getFirstName().isEmpty()
                ||userDetails.getLastName()==null||userDetails.getLastName().isEmpty()
                ||userDetails.getEmail()==null||userDetails.getEmail().isEmpty()
                ||userDetails.getPassword()==null||userDetails.getPassword().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserRest returnValue = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser,returnValue);
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

}

package com.mobileappws.developerBlog.controller;

import com.mobileappws.developerBlog.model.request.UserLoginRequestModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @ApiOperation("User Login")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "Response Headers",
                            responseHeaders = {
                                    @ResponseHeader(name = "authorization",description = "Bearer <JWT value here>",
                                    response = String.class),
                                    @ResponseHeader(name = "userId",description = "<public user id value here>",
                                    response = String.class)
                            }
                    )
            }
    )
    @PostMapping("/login")
    public void fakeLogin(@RequestBody UserLoginRequestModel loginRequestModel){
        throw new IllegalStateException("This method should not be called. This method is implemented by Spring security");
    }
}

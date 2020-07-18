package com.datagrork.api.controller;

import com.datagrork.api.model.SigninResponse;
import com.datagrork.api.model.SignoutResponse;
import com.datagrork.api.model.SignupUserRequest;
import com.datagrork.api.model.SignupUserResponse;
import com.datagrork.service.business.UserBusinessService;
import com.datagrork.service.entity.*;
import com.datagrork.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    // Signup
    @RequestMapping(method = RequestMethod.POST, path = "/signup")
    public ResponseEntity<SignupUserResponse> signUp(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        final UserEntity createUserEntity =userBusinessService.signup(userEntity);
        SignupUserResponse userResponse=new SignupUserResponse().id(createUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }

    // Signin
    @RequestMapping (method= RequestMethod. POST, path= "/signin")
    public ResponseEntity<SigninResponse> authentication(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        UserAuthEntity userAuthEntity = userBusinessService.authenticate(decodedArray[0], decodedArray[1]);
        UserEntity user = userAuthEntity.getUser();
        SigninResponse signinResponse=new SigninResponse().id((user.getUuid())).message("SIGNED IN SUCCESSFULLY");
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("access-token", userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse,httpHeaders, HttpStatus.OK);
    }

    // Signout
    @RequestMapping(method=RequestMethod.POST, path="/signout")
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {
        String [] bearerToken = authorization.split("Bearer ");
        UserEntity userEntity = userBusinessService.signout(bearerToken[0]);
        SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }
}
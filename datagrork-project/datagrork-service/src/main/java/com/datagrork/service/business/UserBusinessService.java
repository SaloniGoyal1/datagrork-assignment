package com.datagrork.service.business;

import com.datagrork.service.common.JwtTokenProvider;
import com.datagrork.service.dao.UserDao;
import com.datagrork.service.entity.*;
import com.datagrork.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        UserEntity userNameEntity = userDao.checkUserName(userEntity.getUserName());
        UserEntity emailEntity = userDao.checkEmailid(userEntity.getEmail());
        // Go for user creation only if username are unique
        if (userNameEntity != null && userEntity.getUserName().equals(userNameEntity.getUserName())) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        // Go for user creation only if email id are unique
        if (emailEntity != null && userEntity.getEmail().equals(emailEntity.getEmail())) {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }
        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByUsername(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        String encryptedPwd = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedPwd.equals(userEntity.getPassword())) {
            JwtTokenProvider tokenProvider = new JwtTokenProvider(encryptedPwd);
            UserAuthEntity userAuthToken = new UserAuthEntity();
            userAuthToken.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setAccessToken(tokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);
            userAuthToken.setUuid(userEntity.getUuid());
            userDao.createUserAuth(userAuthToken);
            userAuthToken.setLogoutAt(null);
            return userAuthToken;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signout(String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity == null){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        else {
            final ZonedDateTime logoutAtDate = ZonedDateTime.now();
            userAuthEntity.setLogoutAt(logoutAtDate);
            userDao.updateUserAuth(userAuthEntity);
            return userAuthEntity.getUser();
        }
    }
}
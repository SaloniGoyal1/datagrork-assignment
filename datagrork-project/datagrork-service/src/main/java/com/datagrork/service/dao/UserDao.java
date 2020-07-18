package com.datagrork.service.dao;

import com.datagrork.service.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.time.ZonedDateTime;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;


    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    @Transactional
    public UserAuthEntity signOut(UserAuthEntity userAuthEntity){
        userAuthEntity.setLogoutAt(ZonedDateTime.now());
        return userAuthEntity;
    }

    public UserEntity checkUserName(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity checkEmailid(String emailid) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", emailid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUsername(String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByEmail(String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity createUserAuth(UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }


    public UserAuthEntity getUserAuthByAccesstoken(String accesstoken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccesstoken", UserAuthEntity.class).setParameter("accesstoken", accesstoken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity updateUserAuth(UserAuthEntity userAuthEntity) {
        return entityManager.merge(userAuthEntity);
    }
}

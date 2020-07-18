package com.datagrork.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "userByUsername", query = "select u from UserEntity u where u.userName = :username"),
        @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email = :email"),
        @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid =:uuid")
})

public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;


    @Column(name = "FIRST_NAME")
    @NotNull
    @Size(max = 30)
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotNull
    @Size(max = 30)
    private String lastName;

    @Column(name = "USERNAME", unique = true)
    @NotNull
    @Size(max = 30)
    private String userName;

    @Column(name = "EMAIL", unique = true)
    @NotNull
    @Size(max = 50)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @ToStringExclude
    @Size(max = 255)
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    private String salt;

    @Column(name = "COUNTRY")
    @Size(max = 30)
    private String country;

    @Column(name = "CONTACT_NUMBER")
    @Size(max = 30)
    private String contactNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}

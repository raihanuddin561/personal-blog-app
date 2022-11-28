package com.mobileappws.developerBlog.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="users")
public class UserEntity{
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false,length = 50)
    private String firstName;
    @Column(nullable = false,length = 50)
    private String lastName;
    @Column(nullable = false,length = 120)
    private String email;
    @Column(nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;
    @Column(nullable = false)
    private Boolean emailVerificationStatus = false;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;
}

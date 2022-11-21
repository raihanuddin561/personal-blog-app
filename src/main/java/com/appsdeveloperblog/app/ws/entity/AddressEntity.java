package com.appsdeveloperblog.app.ws.entity;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String addressId;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;
}

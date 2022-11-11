package com.appsdeveloperblog.app.ws.entity;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@Entity(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String addressId;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
            name="userId",
            referencedColumnName = "userId"
    )
    private UserEntity userDetails;
}

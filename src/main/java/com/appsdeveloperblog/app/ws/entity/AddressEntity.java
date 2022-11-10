package com.appsdeveloperblog.app.ws.entity;

import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Long id;
    @Column(length = 30,nullable = false)
    private String addressId;
    @Column(length = 20,nullable = false)
    private String city;
    @Column(length = 20,nullable = false)
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="users_id",referencedColumnName = "id")
    @ToString.Exclude
    private UserEntity userDetails;
}

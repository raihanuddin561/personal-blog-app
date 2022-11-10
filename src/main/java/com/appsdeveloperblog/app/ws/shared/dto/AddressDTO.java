package com.appsdeveloperblog.app.ws.shared.dto;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    private UserDto userDetails;
}

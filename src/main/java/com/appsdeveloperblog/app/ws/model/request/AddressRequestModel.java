package com.appsdeveloperblog.app.ws.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequestModel {
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}

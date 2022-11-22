package com.appsdeveloperblog.app.ws.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRest extends RepresentationModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest> addresses;
}

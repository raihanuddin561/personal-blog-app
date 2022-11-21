package com.appsdeveloperblog.app.ws.service;

import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO save(AddressDTO address);

    List<AddressDTO> getAddresses(String id);
}

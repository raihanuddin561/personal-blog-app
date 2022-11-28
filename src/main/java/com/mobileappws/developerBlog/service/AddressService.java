package com.mobileappws.developerBlog.service;

import com.mobileappws.developerBlog.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO save(AddressDTO address);

    List<AddressDTO> getAddresses(String id);

    AddressDTO getAddressById(String addressId);
}

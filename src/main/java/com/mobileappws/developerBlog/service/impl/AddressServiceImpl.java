package com.mobileappws.developerBlog.service.impl;

import com.mobileappws.developerBlog.entity.AddressEntity;
import com.mobileappws.developerBlog.entity.UserEntity;
import com.mobileappws.developerBlog.repository.AddressRepository;
import com.mobileappws.developerBlog.repository.UserRepository;
import com.mobileappws.developerBlog.service.AddressService;
import com.mobileappws.developerBlog.shared.dto.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AddressDTO save(AddressDTO address) {
        ModelMapper modelMapper = new ModelMapper();
        AddressEntity addressEntity = modelMapper.map(address,AddressEntity.class);
        UserEntity userEntity = modelMapper.map(address.getUser(),UserEntity.class);
        addressEntity.setUser(userEntity);
        AddressEntity returnValue = addressRepository.save(addressEntity);
        return modelMapper.map(returnValue,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses(String id) {
        List<AddressDTO> returnValue = new ArrayList<>();
        UserEntity userEntity = userRepository.findByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        Iterable<AddressEntity> addresses = addressRepository.findAllByUser(userEntity);
        for(AddressEntity addressEntity:addresses){
            returnValue.add(modelMapper.map(addressEntity,AddressDTO.class));
        }
        return returnValue;
    }

    @Override
    public AddressDTO getAddressById(String addressId) {
        AddressEntity addressEntity= addressRepository.findByAddressId(addressId);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressEntity,AddressDTO.class);
    }
}

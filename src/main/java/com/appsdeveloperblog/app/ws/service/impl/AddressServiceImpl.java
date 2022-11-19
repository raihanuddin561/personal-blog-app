package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repository.AddressRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressDTO save(AddressDTO address) {
        ModelMapper modelMapper = new ModelMapper();
        AddressEntity addressEntity = modelMapper.map(address,AddressEntity.class);
        UserEntity userEntity = modelMapper.map(address.getUserDetails(),UserEntity.class);
        addressEntity.setUserEntity(userEntity);
        AddressEntity returnValue = addressRepository.save(addressEntity);
        return modelMapper.map(returnValue,AddressDTO.class);
    }
}

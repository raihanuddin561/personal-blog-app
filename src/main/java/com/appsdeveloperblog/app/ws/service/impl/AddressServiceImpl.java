package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.entity.UserEntity;
import com.appsdeveloperblog.app.ws.repository.AddressRepository;
import com.appsdeveloperblog.app.ws.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
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
}

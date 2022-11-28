package com.mobileappws.developerBlog.repository;

import com.mobileappws.developerBlog.entity.AddressEntity;
import com.mobileappws.developerBlog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity,Long> {
    List<AddressEntity> findAllByUser(UserEntity userEntity);

    AddressEntity findByAddressId(String addressId);
}

package com.appsdeveloperblog.app.ws.repository;

import com.appsdeveloperblog.app.ws.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity,Long> {
}

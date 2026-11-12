package com.eberandoak.api.repository;
import com.eberandoak.api.entity.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findByUser_IdAndAddressLineAndCityAndPincode(Long userId,String addressLine,String city,String pincode);
}

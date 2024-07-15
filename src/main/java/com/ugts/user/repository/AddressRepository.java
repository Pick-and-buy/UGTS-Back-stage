package com.ugts.user.repository;

import java.util.Optional;

import com.ugts.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Finds an address by the user ID and whether it is the default address.
     *
     * @param  userId    the ID of the user
     * @param  isDefault whether the address is the default address or not
     * @return           an optional containing the address if found, or an empty optional if not found
     */
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefault = :isDefault")
    Optional<Address> findByUserIdAndDefault(String userId, boolean isDefault);

    /**
     * A description of the entire Java function.
     *
     * @param  isDefault	description of parameter
     * @return         	description of return value
     */
    @Query("SELECT a FROM Address a WHERE a.isDefault= :isDefault")
    Optional<Address> findByDefault(boolean isDefault);
}

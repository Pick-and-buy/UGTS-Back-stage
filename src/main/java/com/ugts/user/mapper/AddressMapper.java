package com.ugts.user.mapper;

import com.ugts.user.dto.response.AddressResponse;
import com.ugts.user.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse toAddress(Address address);
}

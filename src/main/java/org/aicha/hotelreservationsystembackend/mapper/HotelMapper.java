package org.aicha.hotelreservationsystembackend.mapper;

import org.aicha.hotelreservationsystembackend.domain.Hotel;
import org.aicha.hotelreservationsystembackend.dto.HotelDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

    @Mapping(source = "owner.id", target = "ownerId")
    HotelDTO hotelToHotelDTO(Hotel hotel);

    @Mapping(source = "ownerId", target = "owner.id")
    Hotel hotelDTOToHotel(HotelDTO hotelDTO);
}
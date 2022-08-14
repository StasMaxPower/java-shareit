package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMaper;
import ru.practicum.shareit.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMaper.class})
public interface BookingMaper {

    @Mapping(source = "booker", target = "booker.id")
    Booking toBooking(ResponseDto bookingDtoToIn);

    @Mapping(source = "booker.id", target = "bookerId")
    ItemDto.shortBookingDto toShortBookingDto(Booking booking);


    BookingDto toBookingDto(Booking booking);
}

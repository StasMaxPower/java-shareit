package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemMaper;
import ru.practicum.shareit.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMaper.class})
public interface BookingMaper {

    Booking toBooking(BookingDtoToIn bookingDtoToIn);

    @Mapping(source = "booker", target = "bookerId")
    ShortBooking toShortBooking(Booking booking);

    @Mapping(source = "bookerUser", target = "booker")
    BookingDtoToOut toBookingDtoToOut(Booking booking);
}

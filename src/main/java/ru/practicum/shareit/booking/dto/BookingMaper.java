package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;

@Mapper(componentModel = "spring")
public interface BookingMaper {

    Booking toBooking(BookingDtoToIn bookingDtoToIn);
    @Mapping(source = "booker", target = "bookerId")
    public ShortBooking toShortBooking(Booking booking);
}

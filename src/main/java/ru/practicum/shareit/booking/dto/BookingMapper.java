package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.UserRepository;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto){
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBooker(),
                bookingDto.getStatus()
                );
    }

    public static BookingDto toBookingDto(Booking booking){
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

}

package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseDto;

import java.util.List;

public interface BookingService {
    BookingDto create(ResponseDto bookingDto, int booker);

    BookingDto verificateStatus(int bookingId, boolean approved, int userId);

    BookingDto getBookingById(int bookingId, int userId);

    List<BookingDto> getAllBookingByUser(int userId, State state, int from, int size);

    List<BookingDto> getAllBookingByOwner(int ownerId, State state, int from, int size);
}

package ru.practicum.shareit.booking;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseDto;


import java.util.Collection;

public interface BookingService {
    BookingDto create(ResponseDto bookingDto, int booker);

    BookingDto verificateStatus(int bookingId, boolean approved, int userId);

    BookingDto getBookingById(int bookingId, int userId);

    Collection<BookingDto> getAllBookingByUser(int userId, State state);

    Collection<BookingDto> getAllBookingByOwner(int ownerId, State state);
}

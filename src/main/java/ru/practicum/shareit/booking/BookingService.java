package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoToOut;

import java.util.Collection;

public interface BookingService {
    BookingDto create(BookingDto bookingDto, int booker, int userId);

    BookingDtoToOut verificateStatus(int bookingId, boolean approved, int userId);

    BookingDtoToOut getBookingById(int bookingId, int userId);

    Collection<BookingDtoToOut> getAllBookingByUser(int userId, State state);

    Collection<BookingDtoToOut> getAllBookingByOwner(int ownerId, State state);
}

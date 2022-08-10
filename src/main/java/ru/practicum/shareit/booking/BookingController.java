package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoToIn;
import ru.practicum.shareit.booking.dto.BookingDtoToOut;

import java.util.Collection;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoToOut createBooking(@RequestBody BookingDtoToIn bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") int booker,
                                    @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.create(bookingDto, booker, userId);
    }

    @PatchMapping("/{bookingId}{approved}")
    public BookingDtoToOut verificateStatus(@PathVariable int bookingId,
                                            @RequestParam boolean approved,
                                            @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.verificateStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoToOut getBookingById(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDtoToOut> getAllBookingByUser(@RequestParam(defaultValue = "ALL") State state,
                                                           @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getAllBookingByUser(userId, state);
    }


    @GetMapping("/owner")
    public Collection<BookingDtoToOut> getAllBookingByOwner(@RequestParam(defaultValue = "ALL") State state,
                                                           @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return bookingService.getAllBookingByOwner(ownerId, state);
    }
}

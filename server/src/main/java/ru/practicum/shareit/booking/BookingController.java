package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody ResponseDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") int booker) {
        return bookingService.create(bookingDto, booker);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto verificateStatus(@PathVariable int bookingId,
                                       @RequestParam boolean approved,
                                       @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.verificateStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingByUser(@RequestParam(defaultValue = "ALL") State state,
                                                @RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBookingByUser(userId, state, from, size);
    }


    @GetMapping("/owner")
    public List<BookingDto> getAllBookingByOwner(@RequestParam(defaultValue = "ALL") State state,
                                                 @RequestHeader("X-Sharer-User-Id") int ownerId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return bookingService.getAllBookingByOwner(ownerId, state, from, size);
    }
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoToIn;
import ru.practicum.shareit.booking.dto.BookingDtoToOut;
import ru.practicum.shareit.booking.dto.BookingMaper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMaper bookingMaper;

    @Override
    public BookingDtoToOut create(BookingDtoToIn bookingDto, int booker, int userId) {
        log.info("Запрос на создание букинга получен");
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найдена вещь"));
        if (booker == item.getOwner())
            throw new NotFoundException("Нельзя брать в аренду вещь у самого себя");
        if (!item.getAvailable())
            throw new ValidateException("Вещь недоступна для аренды");
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getStart() == bookingDto.getEnd())
            throw new ValidateException("Некоректные дата и время начала или окончания бронирования");
        if (!itemRepository.existsById(bookingDto.getItemId()) || !(userRepository.existsById(userId)))
            throw new NotFoundException("Не найдена вещь или пользователь");
        Booking booking = bookingMaper.toBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setBookerUser(userRepository.findById(booker).orElseThrow());
        booking.setStatus(Status.WAITING);
        return bookingMaper.toBookingDtoToOut(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoToOut verificateStatus(int bookingId, boolean approved, int userId) {
        log.info("Запрос на подтверждение статуса букинга получен");
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найден booking c ID = " + bookingId));
        if (booking.getStatus() == Status.APPROVED)
            throw new ValidateException("Подтвержденный статус изменить нельзя");
        User booker = userRepository.findById(booking.getBooker())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(booking.getItemId()).orElseThrow();//проверка на владельца
        if (item.getOwner() != userId)
            throw new NotFoundException("Только владелец может менять статус бронирования");

        booking.setBookerUser(booker);
        if (approved)
            booking.setStatus(Status.APPROVED);
        else
            booking.setStatus(Status.REJECTED);
        booking = bookingRepository.save(booking);

        return bookingMaper.toBookingDtoToOut(booking);
    }

    @Override
    public BookingDtoToOut getBookingById(int bookingId, int userId) {
        log.info("Запрос на поиск букинга с ID {} получен", bookingId);
        Booking booking = bookingRepository.getBookingById(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Не найден booking c ID = " + bookingId));
        return bookingMaper.toBookingDtoToOut(booking);
    }

    @Override
    public Collection<BookingDtoToOut> getAllBookingByUser(int userId, State state) {
        log.info("Запрос на поиск всех бронирований пользователя с ID {} получен", userId);
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Не найден пользователь с ID = " + userId);
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = bookingRepository.findBookingByBookerOrderByStartDesc(userId);
                break;
            case PAST:
                result = bookingRepository.findBookingByBookerAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.findBookingByBookerAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case CURRENT:
                result = bookingRepository.findBookingByBookerAndStartBeforeAndEndIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case WAITING:
                result = bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
        }

        return result.stream().map(bookingMaper::toBookingDtoToOut).collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDtoToOut> getAllBookingByOwner(int ownerId, State state) {
        log.info("Запрос на поиск всех бронирований владельца с ID {} получен", ownerId);
        if (!userRepository.existsById(ownerId))
            throw new NotFoundException("Не найден пользователь с ID = " + ownerId);
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllBookingByOwner(ownerId);
                break;
            case PAST:
                result = bookingRepository.findBookingByOwnerPast(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                result = bookingRepository.findBookingByOwnerFuture(ownerId, LocalDateTime.now());
                break;
            case CURRENT:
                result = bookingRepository.findBookingByOwnerCurrent(ownerId,
                        LocalDateTime.now(), LocalDateTime.now());
                break;
            case WAITING:
                result = bookingRepository.findBookingByOwnerWaiting(ownerId, Status.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findBookingByOwnerWaiting(ownerId, Status.REJECTED);
                break;
        }

        return result.stream().map(bookingMaper::toBookingDtoToOut).collect(Collectors.toList());
    }
}

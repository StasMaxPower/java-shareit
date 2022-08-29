package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMaper;
import ru.practicum.shareit.booking.dto.ResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
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
    public BookingDto create(ResponseDto bookingDto, int booker) {
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
        if (!userRepository.existsById(booker))
            throw new NotFoundException("Не найдена вещь или пользователь");
        Booking booking = bookingMaper.toBooking(bookingDto);
        //booking.setBooker(booker);
        booking.setItem(item);
        booking.setBooker(userRepository.findById(booker).orElseThrow());
        booking.setStatus(Status.WAITING);
        return bookingMaper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto verificateStatus(int bookingId, boolean approved, int userId) {
        log.info("Запрос на подтверждение статуса букинга получен");
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найден booking c ID"));
        if (booking.getStatus() == Status.APPROVED)
            throw new ValidateException("Подтвержденный статус изменить нельзя");
        User booker = userRepository.findById(booking.getBooker().getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(booking.getItemId()).orElseThrow();//проверка на владельца
        if (item.getOwner() != userId)
            throw new NotFoundException("Только владелец может менять статус бронирования");

        booking.setBooker(booker);
        if (approved)
            booking.setStatus(Status.APPROVED);
        else
            booking.setStatus(Status.REJECTED);
        booking = bookingRepository.save(booking);

        return bookingMaper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        log.info("Запрос на поиск букинга с ID {} получен", bookingId);
        Booking booking = bookingRepository.getBookingById(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Не найден booking c ID"));
        return bookingMaper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getAllBookingByUser(int userId, State state, int from, int size) {
        log.info("Запрос на поиск всех бронирований пользователя с ID {} получен", userId);
        User booker = userRepository.findById(userId).orElseThrow();
/*        if (!userRepository.existsById(userId))
            throw new NotFoundException("Не найден пользователь с ID = " + userId);*/
        Page<Booking> result = null;

        from = from - 1 < 0 ? -1 : from - 1;
        Pageable p;
        if (from != -100 && size != -100)
             p = PageRequest.of(from, size);
        else
             p = Pageable.unpaged();

        switch (state) {
            case ALL:
                booker = userRepository.findById(userId).orElseThrow();
                result = bookingRepository.findBookingByBookerOrderByStartDesc(booker, p);
                break;
            case PAST:
                result = bookingRepository
                        .findBookingByBookerAndEndIsBeforeOrderByStartDesc(booker, LocalDateTime.now(), p);
                break;
            case FUTURE:
                result = bookingRepository
                        .findBookingByBookerAndStartAfterOrderByStartDesc(booker, LocalDateTime.now(), p);
                break;
            case CURRENT:
                result = bookingRepository.findBookingByBookerAndStartBeforeAndEndIsAfterOrderByStartDesc(booker,
                        LocalDateTime.now(), LocalDateTime.now(), p);
                break;
            case WAITING:
                result = bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(booker,
                        Status.WAITING, p);
                break;
            case REJECTED:
                result = bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(booker,
                        Status.REJECTED, p);
                break;
        }

        return result.stream().map(bookingMaper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public Collection<BookingDto> getAllBookingByOwner(int ownerId, State state, int from, int size) {
        log.info("Запрос на поиск всех бронирований владельца с ID {} получен", ownerId);
        if (!userRepository.existsById(ownerId))
            throw new NotFoundException("Не найден пользователь с ID = " + ownerId);
        Page<Booking> result = null;

        Pageable p;
        if (from != -100 && size != -100)
            p = PageRequest.of(from, size);
        else
            p = Pageable.unpaged();

        switch (state) {
            case ALL:
                result = bookingRepository.findAllBookingByOwner(ownerId, p);
                break;
            case PAST:
                result = bookingRepository.findBookingByOwnerPast(ownerId, LocalDateTime.now(), p);
                break;
            case FUTURE:
                result = bookingRepository.findBookingByOwnerFuture(ownerId, LocalDateTime.now(), p);
                break;
            case CURRENT:
                result = bookingRepository.findBookingByOwnerCurrent(ownerId,
                        LocalDateTime.now(), LocalDateTime.now(), p);
                break;
            case WAITING:
                result = bookingRepository.findBookingByOwnerWaiting(ownerId, Status.WAITING, p);
                break;
            case REJECTED:
                result = bookingRepository.findBookingByOwnerWaiting(ownerId, Status.REJECTED, p);
                break;
        }

        return result.stream().map(bookingMaper::toBookingDto).collect(Collectors.toList());
    }
}

package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.time.LocalDateTime;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@Transactional(propagation = Propagation.REQUIRED)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {


    private final BookingService bookingService;
    @MockBean
    private final BookingRepository bookingRepository;
    @MockBean
    private final UserRepository userRepository;
    @MockBean
    private final ItemRepository itemRepository;

    User user1 = User.builder().id(1).name("user 1").email("User1@mail.ru").build();
    User user2 = new User(2, "User 2", "User2@mail.ru");
    Booking booking = Booking.builder().booker(user1).status(Status.WAITING).build();
    Booking rejectedBooking = Booking.builder().booker(user1).status(Status.REJECTED).build();
    Booking approvedBooking = Booking.builder().booker(user2).status(Status.APPROVED).build();


    Item item = new Item(1, "дрель", "аккумуляторная", true, 1,
            0, null, null, null);
    Item unAvailableItem = new Item(1, "дрель", "аккумуляторная", false, 1,
            0, null, null, null);
    ResponseDto responseDto = new ResponseDto(
            1,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            1,
            1,
            Status.WAITING);

    ResponseDto failedResponseDto = new ResponseDto(
            1,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(2),
            1,
            1,
            Status.WAITING);

    @Test
    void createBookingWithoutItemShouldBeFailed() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        Exception ex = Assertions.assertThrows(NotFoundException.class, () -> bookingService.create(responseDto, 1));
        assertEquals(ex.getMessage(), "Не найдена вещь");
    }

    @Test
    void createBookingWithoutUserShouldBeFailed() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(responseDto, 2));
        assertEquals(ex.getMessage(), "Не найдена вещь или пользователь");
    }

    @Test
    void createBookingWithUnavailableItemShouldBeFailed() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(unAvailableItem));
        ValidateException ex = Assertions.assertThrows(ValidateException.class,
                () -> bookingService.create(responseDto, 2));
        assertEquals(ex.getMessage(), "Вещь недоступна для аренды");
    }

    @Test
    void createBookingWithUserIsBookerShouldBeFailed() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(responseDto, 1));
        assertEquals(ex.getMessage(), "Нельзя брать в аренду вещь у самого себя");
    }

    @Test
    void saveBookingIsFailed() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.save(any())).thenReturn(booking);
        ValidateException ex = assertThrows(ValidateException.class,
                () -> bookingService.create(failedResponseDto, 2));
    }

    @Test
    void saveBookingIsOk() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto saved = bookingService.create(responseDto, 2);
        assertNotNull(saved);
        assertEquals(saved.getStatus(), Status.WAITING);
    }

    @Test
    void verificateStatusWithoutBookingIsFail() {
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.verificateStatus(1, true, 1));
        assertEquals(ex.getMessage(), "Не найден booking c ID");
    }

    @Test
    void verificateStatusWithApprovesStatusIsFail() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(approvedBooking));
        ValidateException ex = Assertions.assertThrows(ValidateException.class,
                () -> bookingService.verificateStatus(1, true, 1));
        assertEquals(ex.getMessage(), "Подтвержденный статус изменить нельзя");
    }

    @Test
    void verificateStatusWithoutUserIsFail() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(booking));
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.verificateStatus(1, true, 1));
        assertEquals(ex.getMessage(), "Пользователь не найден");
    }

    @Test
    void verificateStatusWithUserIsNotBookerIsFail() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.verificateStatus(1, true, 2));
        assertEquals(ex.getMessage(), "Только владелец может менять статус бронирования");
    }

    @Test
    void verificateStatusIsOk() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(booking));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto saved = bookingService.verificateStatus(1, true, 1);
        assertEquals(booking.getStatus(), saved.getStatus());
    }

    @Test
    void verificateStatusIsOkWhenStatusRejected() {
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.ofNullable(rejectedBooking));
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any())).thenReturn(booking);
        BookingDto saved = bookingService.verificateStatus(1, false, 1);
        assertEquals(booking.getStatus(), saved.getStatus());
    }

    @Test
    void getBookingByIdIsOk() {
        when(bookingRepository.getBookingById(anyInt(), anyInt())).thenReturn(Optional.ofNullable(booking));
        BookingDto saved = bookingService.getBookingById(1, 1);
        assertEquals(booking.getStatus(), saved.getStatus());
    }

    @Test
    void getBookingByIdIsFail() {
        when(bookingRepository.getBookingById(anyInt(), anyInt())).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(1, 1));
        assertEquals(ex.getMessage(), "Не найден booking c ID");
    }

    @Test
    void getAllBookingByUserIsOk() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        Collection<BookingDto> result;
        for (State state : State.values()) {
            switch (state) {
                case ALL:
                    when(bookingRepository.findBookingByBookerOrderByStartDesc(any(), any()))
                            .thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByUser(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case PAST:
                    when(bookingRepository.findBookingByBookerAndEndIsBeforeOrderByStartDesc(any(), any(), any()))
                            .thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByUser(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case FUTURE:
                    when(bookingRepository
                            .findBookingByBookerAndStartAfterOrderByStartDesc(any(), any(), any()))
                            .thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByUser(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case CURRENT:
                    when(bookingRepository.findBookingByBookerAndStartBeforeAndEndIsAfterOrderByStartDesc(any(),
                            any(), any(), any())).thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByUser(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case WAITING:
                    when(bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(any(),
                            any(), any())).thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByUser(1, state, 1, 10);
                    assertEquals(result.size(), 1);

                    break;
                case REJECTED:
                    when(bookingRepository.findBookingByBookerAndStatusOrderByStartDesc(any(),
                            any(), any())).thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByUser(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;
            }
        }
    }

    @Test
    void getAllBookingByOwner() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        Collection<BookingDto> result;
        for (State state : State.values()) {
            switch (state) {
                case ALL:
                    when(bookingRepository.findAllBookingByOwner(anyInt(), any()))
                            .thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByOwner(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case PAST:
                    when(bookingRepository.findBookingByOwnerPast(anyInt(), any(), any()))
                            .thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByOwner(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case FUTURE:
                    when(bookingRepository
                            .findBookingByOwnerFuture(anyInt(), any(), any()))
                            .thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByOwner(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case CURRENT:
                    when(bookingRepository.findBookingByOwnerCurrent(anyInt(),
                            any(), any(), any())).thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByOwner(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;

                case WAITING:
                    when(bookingRepository.findBookingByOwnerWaiting(anyInt(),
                            any(), any())).thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByOwner(1, state, 1, 10);
                    assertEquals(result.size(), 1);

                    break;
                case REJECTED:
                    when(bookingRepository.findBookingByOwnerWaiting(anyInt(),
                            any(), any())).thenReturn(new PageImpl<>(List.of(booking)));
                    result = bookingService
                            .getAllBookingByOwner(1, state, 1, 10);
                    assertEquals(result.size(), 1);
                    break;
            }
        }
    }

    @Test
    void getAllBookingByUserMustBeFail() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.findBookingByBookerOrderByStartDesc(any(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Exception ex = assertThrows(NullPointerException.class, () -> bookingService
                .getAllBookingByUser(1, null, 1, 10));
    }

    @Test
    void getAllBookingByOwnerMustBeFail() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(bookingRepository.findAllBookingByOwner(anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(booking)));
        Exception ex = assertThrows(NullPointerException.class, () -> bookingService
                .getAllBookingByOwner(1, null, 1, 10));
    }

    @Test
    void getAllBookingByOwnerFail() {
        when(userRepository.existsById(anyInt())).thenReturn(false);
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingByOwner(1, null, 1, 1));
    }

    @Test
    void getAllBookingByOwnerFailWithoutPag() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.getAllBookingByOwner(1, null, -100, -100));
    }

}
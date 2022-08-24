package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional(propagation = Propagation.REQUIRED)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    Booking booking = new Booking();
    //BookingDto bookingDto = new BookingDto();
    User user1 = new User(1, "User 1", "User1@mail.ru");
    User user2 = new User(2, "User 2", "User2@mail.ru");
    Item item = new Item(1,"дрель", "аккумуляторная", true, 1,
            0, null, null, null);
    ResponseDto responseDto = new ResponseDto(
            1,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            1,
            1,
            Status.WAITING);

/*    @BeforeAll
    static void beforeAll(){
        itemRepository.save(item);
    }*/


/*    @BeforeEach
    void beforeEach(){
        itemRepository.save(item);
        userRepository.save(user1);
        userRepository.save(user2);
    }*/

/*    @AfterEach
    void afterAll(){
        itemRepository.setAutoincrement();
        userRepository.setAutoincrement();
    }*/

    @Test
    void createBookingWithoutItemShouldBeFailed() {
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(responseDto, 1));
        assertEquals(ex.getMessage(), "Не найдена вещь");
    }

    @Test
    void createBookingWithoutUserShouldBeFailed() {
        Item i = itemRepository.save(item);
        responseDto.setItemId(i.getId());
        List<Item> list = itemRepository.findAll();
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(responseDto, 2));
        assertEquals(ex.getMessage(), "Не найдена вещь или пользователь");
    }

    @Test
    void createBookingWithUserIsBookerShouldBeFailed() {
        itemRepository.save(item);
        Exception ex = Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.create(responseDto, 1));
        assertEquals(ex.getMessage(), "Нельзя брать в аренду вещь у самого себя");
    }

    @Test
    void saveBookingIsOk() {
        Item i = itemRepository.save(item);
        responseDto.setItemId(i.getId());
        itemRepository.save(item);
        bookingService.create(responseDto, 2);
        Booking booking1 = bookingRepository.findById(1).get();
        assertNotNull(booking1);
    }

/*    @Test
    void saveBookingIsOk1() {

    }*/

}
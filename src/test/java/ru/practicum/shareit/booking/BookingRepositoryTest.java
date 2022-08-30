package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository repository;
    private Integer ownerId;
    private Integer bookerId;
    Booking booking;
    Booking lastBooking;
    Booking nextBooking;
    Item item;
    Pageable p = PageRequest.of(0, 10);

    @BeforeEach
    public void before() {
        User user1 = User.builder().name("test1").email("test1@test1.ru").build();
        User user2 = User.builder().name("test2").email("test2@test2.ru").build();
        em.getEntityManager().persist(user1);
        em.getEntityManager().persist(user2);
        item = Item.builder().name("item").description("description").available(true)
                .owner(user1.getId()).requestId(1).build();
        em.getEntityManager().persist(item);
        booking = Booking.builder().booker(user2).itemId(item.getId()).start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2)).item(item).status(Status.WAITING).build();
        nextBooking = Booking.builder().booker(user2).itemId(item.getId()).start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(4)).item(item).status(Status.WAITING).build();
        lastBooking = Booking.builder().booker(user2).itemId(item.getId()).start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusHours(1)).item(item).status(Status.WAITING).build();
        em.getEntityManager().persist(booking);
        em.getEntityManager().persist(lastBooking);
        em.getEntityManager().persist(nextBooking);

        bookerId = user2.getId();
        ownerId = user1.getId();
    }

    @Test
    public void shouldFindAllByBookerId() {
        Page<Booking> page = repository.findAllBookingByOwner(ownerId, p);
        assertEquals(1, page.getTotalPages());
    }

    @Test
    public void shouldFindAllByOwnerId() {
        Page<Booking> page = repository.findAllBookingByOwner(bookerId, p);
        assertEquals(0, page.getTotalPages());
    }

    @Test
    public void shouldFindFindBookingById() {
        Booking b = repository.getBookingById(booking.getId(), bookerId).orElseThrow();
        Assertions.assertNotNull(b);
        assertEquals(b.getItemId(), item.getId());
    }

    @Test
    public void shouldFindBookingByOwnerPast() {
        booking.setEnd(LocalDateTime.now().minusDays(1));
        Page<Booking> page = repository.findBookingByOwnerPast(ownerId, LocalDateTime.now(), p);
        Assertions.assertNotNull(page);
        assertEquals(page.getTotalElements(), 2);
        assertEquals(page.stream().findFirst().get(), booking);
    }

    @Test
    public void shouldFindBookingByOwnerFuture() {
        booking.setEnd(LocalDateTime.now().plusDays(1));
        Page<Booking> page = repository.findBookingByOwnerFuture(ownerId, LocalDateTime.now(), p);
        Assertions.assertNotNull(page);
        assertEquals(page.getTotalElements(), 2);
        assertTrue(page.stream().collect(Collectors.toList()).contains(booking));
    }

    @Test
    public void shouldFindBookingByOwnerCurrent() {
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        Page<Booking> page = repository.findBookingByOwnerCurrent(ownerId, LocalDateTime.now(),
                LocalDateTime.now(), p);
        Assertions.assertNotNull(page);
        assertEquals(page.getTotalElements(), 1);
        assertEquals(page.stream().findFirst().get(), booking);
    }

    @Test
    public void shouldFindBookingByOwnerWaiting() {
        Page<Booking> page = repository.findBookingByOwnerWaiting(ownerId, Status.WAITING, p);
        Assertions.assertNotNull(page);
        assertEquals(page.getTotalElements(), 3);
        assertTrue(page.stream().collect(Collectors.toList()).contains(booking));
    }

    @Test
    public void shouldGetNextBookingToItem() {
        List<Booking> list = repository.getNextBookingToItem(item.getId(), LocalDateTime.now());
        Assertions.assertNotNull(list);
        assertEquals(list.size(), 2);
        assertTrue(list.contains(nextBooking));
    }

    @Test
    public void shouldGetLastBookingToItem() {
        List<Booking> list = repository.getLastBookingToItem(item.getId(), LocalDateTime.now());
        Assertions.assertNotNull(list);
        assertEquals(list.size(), 1);
        assertTrue(list.contains(lastBooking));
    }
}
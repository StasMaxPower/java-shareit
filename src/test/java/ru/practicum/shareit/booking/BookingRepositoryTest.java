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


@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository repository;
    private Integer ownerId;
    private Integer bookerId;

    @BeforeEach
    public void before() {
        User user1 = User.builder().name("test1").email("test1@test1.ru").build();
        User user2 = User.builder().name("test2").email("test2@test2.ru").build();
        Item item = Item.builder().name("item").description("description").available(true)
                .owner(1).requestId(1).build();
        Booking booking = Booking.builder().booker(user2).itemId(1).start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2)).item(item).build();

        em.getEntityManager().persist(user1);
        em.getEntityManager().persist(user2);
        em.getEntityManager().persist(item);
        em.getEntityManager().persist(booking);
        bookerId = user2.getId();
        ownerId = user1.getId();
    }

    @Test
    public void shouldFindAllByBookerId() {
        Pageable p = PageRequest.of(0, 10);
        Page<Booking> list = repository.findAllBookingByOwner(ownerId, p);
        Assertions.assertEquals(1, list.getTotalPages());
    }

/*    @Test
    public void shouldFindAllByOwnerId() {
        Pageable pageable = PageRequest.of(0, 6);
        List<Booking> list = repository.findAllBookingByOwner(bookerId, pageable);
        Assertions.assertEquals(0, list.size());
    }*/
}
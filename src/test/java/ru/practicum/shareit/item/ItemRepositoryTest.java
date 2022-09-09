package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository repository;
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
    }

    @Test
    void searchIsOk() {
        Page<Item> page = repository.search("item", p);
        assertEquals(page.getTotalPages(), 1);
        assertEquals(page.stream().findAny().get(), item);
    }

    @Test
    void searchIsEmpty() {
        Page<Item> page = repository.search("abc", p);
        assertEquals(page.getTotalPages(), 0);
    }

}